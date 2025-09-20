// src/components/ArticleList.jsx
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { API_BASE } from '../api';

const ArticleList = () => {
  const [articles, setArticles] = useState([]);
  const [q, setQ] = useState('');
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState('');

  const parsePayload = async (res) => {
    const d = await res.json().catch(() => ({}));
    if (!res.ok) throw new Error(d?.message || 'Có lỗi xảy ra');
    return d?.data ?? d; // ObjectResponse<data> | data
  };

  const fetchAll = async () => {
    setLoading(true); setErr('');
    try {
      const res = await fetch(`${API_BASE}/articles`, { credentials: 'include' });
      const payload = await parsePayload(res);
      setArticles(Array.isArray(payload) ? payload : []);
    } catch (e) {
      setErr(e.message || 'Không tải được danh sách');
      setArticles([]);
    } finally {
      setLoading(false);
    }
  };

  const search = async () => {
    const key = q.trim();
    if (!key) { await fetchAll(); return; }
    setLoading(true); setErr('');
    try {
      const url = `${API_BASE}/articles/search?key=${encodeURIComponent(key)}`;
      const res = await fetch(url, { credentials: 'include' });
      const payload = await parsePayload(res);
      setArticles(Array.isArray(payload) ? payload : []);
    } catch (e) {
      setErr(e.message || 'Tìm kiếm thất bại');
      setArticles([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchAll(); }, []);

  const onSubmit = (e) => { e.preventDefault(); search(); };

  if (loading) return <div>Đang tải...</div>;
  return (
    <div>
      <h2>Research Articles</h2>

      {/* Thanh tìm kiếm */}
      <form onSubmit={onSubmit} style={{ marginBottom: 12 }}>
        <input
          type="search"
          placeholder="Tìm theo tiêu đề hoặc tác giả..."
          value={q}
          onChange={(e) => setQ(e.target.value)}
          style={{ minWidth: 280, marginRight: 8 }}
        />
        <button type="submit">Tìm</button>
        <button type="button" onClick={fetchAll} style={{ marginLeft: 8 }}>
          Tất cả
        </button>
      </form>

      {err && <div style={{ color: 'red', marginBottom: 8 }}>Lỗi: {err}</div>}
      {!articles.length ? (
        <div>Không có bài viết nào.</div>
      ) : (
        <ul>
          {articles.map((a) => {
            const key = a.id || a._id;
            return (
              <li key={key} style={{ marginBottom: 16 }}>
                <h3>
                  <Link to={`/articles?articleId=${encodeURIComponent(key)}`}>
                    {a.title}
                  </Link>
                </h3>
                {a.author && <p><strong>Author:</strong> {a.author}</p>}
                {a.content && <p>{a.content}</p>}
                {(a.updatedAt || a.createdAt) && (
                  <p style={{ fontSize: 12, color: '#666' }}>
                    <em>
                      {a.updatedAt
                        ? `Updated: ${new Date(a.updatedAt).toLocaleString()}`
                        : `Created: ${new Date(a.createdAt).toLocaleString()}`}
                    </em>
                  </p>
                )}
              </li>
            );
          })}
        </ul>
      )}

      <button onClick={fetchAll}>Tải lại</button>
    </div>
  );
};

export default ArticleList;
