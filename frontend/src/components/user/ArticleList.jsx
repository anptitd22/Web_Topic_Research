// src/components/user/ArticleList.jsx
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { API_BASE } from '../../api/api';
import './ArticleList.css';

const TAGS_ENDPOINT = `${API_BASE}/tags`; // đổi nếu backend khác

const MAX_CHIPS = 5;

const ArticleList = () => {
  const [articles, setArticles] = useState([]);
  const [tagMap, setTagMap] = useState({}); // { tagId: tagName }
  const [q, setQ] = useState('');
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState('');

  const parsePayload = async (res) => {
    const d = await res.json().catch(() => ({}));
    if (!res.ok) throw new Error(d?.message || 'Có lỗi xảy ra');
    return d?.data ?? d; // ObjectResponse<data> | data
  };

  const fetchTags = async () => {
    const res = await fetch(TAGS_ENDPOINT, { credentials: 'include' });
    const payload = await parsePayload(res);
    const map = {};
    (Array.isArray(payload) ? payload : []).forEach(t => {
      const id = t?.id ?? t?._id;
      const name = t?.name ?? t?.title ?? String(t ?? '');
      if (id && name) map[id] = name;
    });
    return map;
  };

  const fetchArticles = async () => {
    const res = await fetch(`${API_BASE}/articles`, { credentials: 'include' });
    return await parsePayload(res);
  };

  const loadAll = async () => {
    setLoading(true); setErr('');
    try {
      const [arts, tags] = await Promise.all([fetchArticles(), fetchTags()]);
      setArticles(Array.isArray(arts) ? arts : []);
      setTagMap(tags);
    } catch (e) {
      setErr(e.message || 'Không tải được dữ liệu');
      setArticles([]);
    } finally {
      setLoading(false);
    }
  };

  const search = async () => {
    const key = q.trim();
    if (!key) { await loadAll(); return; }
    setLoading(true); setErr('');
    try {
      const url = `${API_BASE}/articles/search?key=${encodeURIComponent(key)}`;
      const [res, tags] = await Promise.all([
        fetch(url, { credentials: 'include' }),
        Object.keys(tagMap).length ? Promise.resolve(tagMap) : fetchTags()
      ]);
      const arts = await parsePayload(res);
      setArticles(Array.isArray(arts) ? arts : []);
      if (tags !== tagMap) setTagMap(tags);
    } catch (e) {
      setErr(e.message || 'Tìm kiếm thất bại');
      setArticles([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadAll(); }, []);
  const onSubmit = (e) => { e.preventDefault(); search(); };

  const deriveTagNames = (a) => {
    // Ưu tiên: a.tags (["AI","NLP"] hoặc [{id,name}]) → a.tagNames → map từ tagIds
    if (Array.isArray(a?.tags)) {
      return a.tags
        .map(t => (typeof t === 'string' ? t : (t?.name ?? t?.title ?? '')))
        .filter(Boolean);
    }
    if (Array.isArray(a?.tagNames)) return a.tagNames.filter(Boolean);
    if (Array.isArray(a?.tagIds)) {
      return a.tagIds.map(id => tagMap[id]).filter(Boolean);
    }
    return [];
  };

  if (loading) return <div>Đang tải...</div>;

  return (
    <div>
      <h2>Research Articles</h2>

      {/* Thanh tìm kiếm */}
      <form onSubmit={onSubmit} className="searchbar">
        <input
          type="search"
          placeholder="Tìm theo tiêu đề hoặc tác giả..."
          value={q}
          onChange={(e) => setQ(e.target.value)}
        />
        <button type="submit">Tìm</button>
        <button type="button" onClick={loadAll}>Tất cả</button>
      </form>

      {err && <div style={{ color: 'red', marginBottom: 8 }}>Lỗi: {err}</div>}

      {!articles.length ? (
        <div>Không có bài viết nào.</div>
      ) : (
        <ul className="article-list">
          {articles.map((a) => {
            const id = a.id || a._id;
            const tagNames = deriveTagNames(a);
            const shown = tagNames.slice(0, MAX_CHIPS);
            const hidden = Math.max(0, tagNames.length - shown.length);

            return (
              <li key={id} className="article-card">
                {/* Title 1 dòng */}
                <h3 className="article-title">
                  <Link className="article-title-link" to={`/articles?articleId=${encodeURIComponent(id)}`}>
                    {a.title}
                  </Link>
                </h3>

                {/* Author ngay dưới title */}
                {a.author && <div className="article-authorline">By {a.author}</div>}

                {/* Mô tả 2 dòng */}
                {a.content && <p className="article-excerpt">{a.content}</p>}

                {/* Footer: tags bên trái, thời gian bên phải */}
                <div className="article-footer">
                  <div className="tag-row">
                    {shown.map((name, i) => (
                      <span key={i} className="tag-chip">{name}</span>
                    ))}
                    {hidden > 0 && <span className="tag-chip tag-more">+{hidden}</span>}
                  </div>

                  {(a.updatedAt || a.createdAt) && (
                    <span className="article-meta">
                      {a.updatedAt
                        ? new Date(a.updatedAt).toLocaleString()
                        : new Date(a.createdAt).toLocaleString()}
                    </span>
                  )}
                </div>
              </li>
            );
          })}
        </ul>
      )}
    </div>
  );
};

export default ArticleList;
