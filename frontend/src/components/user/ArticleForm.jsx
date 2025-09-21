// src/components/user/ArticleForm.jsx
import React, { useEffect, useState } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import { API_BASE } from '../../api/api';

const MAX_TAGS = 5;
const MIN_TAGS = 1;

const ArticleForm = () => {
  const history = useHistory();
  const { search } = useLocation();
  const articleId = new URLSearchParams(search).get('articleId');
  const isEdit = Boolean(articleId);

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [author, setAuthor] = useState('');

  // file
  const [articleKey, setArticleKey] = useState(''); // ⬅️ tên trùng với BE
  const [articleUrl, setArticleUrl] = useState('');

  // tags
  const [allTags, setAllTags] = useState([]);           // [{id,name}]
  const [tagIds, setTagIds] = useState([]);             // mảng id đã chọn

  // ui state
  const [msg, setMsg] = useState('');
  const [loading, setLoading] = useState(isEdit);
  const [saving, setSaving] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [loadingTags, setLoadingTags] = useState(true);

  // load tags (luôn)
  useEffect(() => {
    let ignore = false;
    (async () => {
      setLoadingTags(true);
      try {
        const res = await fetch(`${API_BASE}/tags`, { credentials: 'include' });
        const d = await res.json().catch(() => ({}));
        if (!res.ok) throw new Error(d?.message || 'Không tải được danh sách tag');
        const list = Array.isArray(d?.data) ? d.data : (Array.isArray(d) ? d : []);
        if (!ignore) setAllTags(list);
      } catch (e) {
        if (!ignore) setMsg(e.message || 'Lỗi tải tag');
      } finally {
        if (!ignore) setLoadingTags(false);
      }
    })();
    return () => { ignore = true; };
  }, []);

  // nếu edit: load bài viết
  useEffect(() => {
    if (!isEdit) return;
    let ignore = false;
    (async () => {
      setLoading(true);
      try {
        const res = await fetch(`${API_BASE}/articles?articleId=${encodeURIComponent(articleId)}`, {
          credentials: 'include'
        });
        const d = await res.json().catch(() => ({}));
        if (!res.ok) throw new Error(d?.message || 'Không tải được bài viết');
        const a = d?.data ?? d;
        if (!ignore && a) {
          setTitle(a.title || '');
          setContent(a.content || '');
          setAuthor(a.author || '');

          // tên thuộc tính từ BE: articleKey/articleUrl/tagIds
          setArticleKey(a.articleKey || '');
          setArticleUrl(a.articleUrl || '');
          setTagIds(Array.isArray(a.tagIds) ? a.tagIds : []);
        }
      } catch (e) {
        if (!ignore) setMsg(e.message || 'Có lỗi xảy ra khi load bài viết');
      } finally {
        if (!ignore) setLoading(false);
      }
    })();
    return () => { ignore = true; };
  }, [articleId, isEdit]);

  // upload file -> nhận { fileKey, fileUrl } từ BE (map sang articleKey/articleUrl)
  const handleFileChange = async (e) => {
    const f = e.target.files?.[0];
    if (!f) return;
    setMsg('');
    setUploading(true);
    try {
      const form = new FormData();
      form.append('file', f);
      const res = await fetch(`${API_BASE}/uploads/article`, {
        method: 'POST',
        credentials: 'include',
        body: form, // KHÔNG tự set Content-Type
      });
      const d = await res.json().catch(() => ({}));
      if (!res.ok) throw new Error(d?.message || 'Upload thất bại');
      const data = d?.data ?? d;
      // BE trả "fileKey" & "fileUrl" -> gán vào articleKey/articleUrl
      setArticleKey(data.fileKey);
      setArticleUrl(data.fileUrl || '');
      setMsg('Upload file thành công');
    } catch (err) {
      setMsg(err.message || 'Upload thất bại');
      setArticleKey('');
      setArticleUrl('');
    } finally {
      setUploading(false);
    }
  };

  const toggleTag = (id) => {
    setMsg('');
    setTagIds((prev) => {
      const has = prev.includes(id);
      if (has) return prev.filter(x => x !== id);
      // thêm mới: kiểm soát tối đa 5
      if (prev.length >= MAX_TAGS) {
        setMsg(`Tối đa ${MAX_TAGS} tag cho mỗi bài viết.`);
        return prev;
      }
      return [...prev, id];
    });
  };

  const canSubmit = () => {
    if (!articleKey) return false;
    if (tagIds.length < MIN_TAGS || tagIds.length > MAX_TAGS) return false;
    return title.trim() && author.trim();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg('');

    if (!articleKey) {
      setMsg('Thiếu file. Hãy chọn file và upload.');
      return;
    }
    if (tagIds.length < MIN_TAGS) {
      setMsg(`Cần chọn ít nhất ${MIN_TAGS} tag.`);
      return;
    }
    if (tagIds.length > MAX_TAGS) {
      setMsg(`Chỉ được chọn tối đa ${MAX_TAGS} tag.`);
      return;
    }

    setSaving(true);
    try {
      const url = isEdit
        ? `${API_BASE}/articles?articleId=${encodeURIComponent(articleId)}`
        : `${API_BASE}/articles`;
      const method = isEdit ? 'PUT' : 'POST';

      // body TRÙNG TÊN BE
      const body = {
        title,
        content,
        author,
        articleKey,
        articleUrl,
        tagIds, // ⬅️ mảng id tag
      };

      const res = await fetch(url, {
        method,
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      });
      const d = await res.json().catch(() => ({}));
      if (!res.ok) throw new Error(d?.message || 'Lưu thất bại');

      setMsg('Lưu thành công!');
      history.replace(isEdit ? `/articles?articleId=${encodeURIComponent(articleId)}` : '/');
    } catch (err) {
      setMsg(err.message || 'Lưu thất bại');
    } finally {
      setSaving(false);
    }
  };

  if (loading || loadingTags) return <div>Đang tải...</div>;

  return (
    <form onSubmit={handleSubmit}>
      <h2>{isEdit ? 'Chỉnh sửa bài viết' : 'Tạo bài viết'}</h2>

      <div style={{ marginTop: 12 }}>
        <label>Title:</label>
        <input value={title} onChange={e => setTitle(e.target.value)} required />
      </div>

      <div>
        <label>Content:</label>
        <textarea value={content} onChange={e => setContent(e.target.value)} rows={6} required />
      </div>

      <div>
        <label>Author:</label>
        <input value={author} onChange={e => setAuthor(e.target.value)} required />
      </div>

      <div>
        <label>Tài liệu (bắt buộc): </label>{' '}
        <input
          type="file"
          accept=".pdf,.doc,.docx,.ppt,.pptx,.xls,.xlsx"
          onChange={handleFileChange}
        />
      </div>

      {articleKey && (
        <div style={{ marginTop: 6 }}>
          Đã có file: <code>{articleKey}</code>{' '}
          {articleUrl && (
            <a href={articleUrl} target="_blank" rel="noreferrer">Xem nhanh</a>
          )}
        </div>
      )}

      {/* TAGS */}
      <div style={{ marginTop: 12 }}>
        <label>Tags (chọn 1–5):</label>
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', marginTop: 6 }}>
          {allTags.map(t => {
            const id = t.id || t._id;
            const checked = tagIds.includes(id);
            return (
              <label key={id} style={{ border: '1px solid #eee', padding: '4px 8px', borderRadius: 6 }}>
                <input
                  type="checkbox"
                  checked={checked}
                  onChange={() => toggleTag(id)}
                />{' '}
                {t.name}
              </label>
            );
          })}
        </div>
        <div style={{ fontSize: 12, color: tagIds.length < MIN_TAGS || tagIds.length > MAX_TAGS ? 'red' : '#555' }}>
          Đã chọn {tagIds.length}/{MAX_TAGS}
        </div>
      </div>

      <button type="submit" disabled={saving || uploading || !canSubmit()}>
        {saving ? 'Đang lưu...' : (isEdit ? 'Cập nhật' : 'Tạo mới')}
      </button>

      {msg && <div style={{ marginTop: 8, color: /thành công|lưu thành công/i.test(msg) ? 'green' : 'red' }}>{msg}</div>}
    </form>
  );
};

export default ArticleForm;
