// src/components/ArticleForm.jsx
import React, { useEffect, useState } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import { API_BASE } from '../api';

const ArticleForm = () => {
  const history = useHistory();
  const { search } = useLocation();
  const articleId = new URLSearchParams(search).get('articleId');
  const isEdit = Boolean(articleId);

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [author, setAuthor] = useState('');
  const [fileKey, setFileKey] = useState('');    
  const [fileUrl, setFileUrl] = useState('');
  const [msg, setMsg] = useState('');
  const [loading, setLoading] = useState(isEdit);
  const [saving, setSaving] = useState(false);
  const [uploading, setUploading] = useState(false);

  // Nếu là edit: load bài viết và prefill cả fileKey/fileUrl (nếu BE trả về)
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
          setFileKey(a.fileKey || '');    
          setFileUrl(a.fileUrl || '');    
        }
      } catch (e) {
        if (!ignore) setMsg(e.message || 'Có lỗi xảy ra');
      } finally {
        if (!ignore) setLoading(false);
      }
    })();
    return () => { ignore = true; };
  }, [articleId, isEdit]);

  // Upload file -> nhận { fileKey, fileUrl } từ BE
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
        body: form,                // ❗ KHÔNG set Content-Type
      });
      const d = await res.json().catch(() => ({}));
      if (!res.ok) throw new Error(d?.message || 'Upload thất bại');
      const data = d?.data ?? d;
      setFileKey(data.fileKey);    
      setFileUrl(data.fileUrl || ''); // nếu bucket private, đây có thể là presigned URL tạm
      setMsg('Upload file thành công');
    } catch (err) {
      setMsg(err.message || 'Upload thất bại');
      setFileKey('');
      setFileUrl('');
    } finally {
      setUploading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg('');

    // BẮT BUỘC CÓ fileKey
    if (!fileKey) {
      setMsg('Thiếu fileKey. Hãy chọn file và upload.');
      return;
    }

    setSaving(true);
    try {
      const url = isEdit
        ? `${API_BASE}/articles?articleId=${encodeURIComponent(articleId)}`
        : `${API_BASE}/articles`;
      const method = isEdit ? 'PUT' : 'POST';
      const body = { title, content, author, fileKey, fileUrl }; // GỬI KÈM fileKey

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

  if (loading) return <div>Đang tải...</div>;

  return (
    <form onSubmit={handleSubmit}>
      <h2>{isEdit ? 'Chỉnh sửa bài viết' : 'Tạo bài viết'}</h2>

      <div>
        <label>Tài liệu (bắt buộc): </label>{' '}
        <input type="file" accept=".pdf,.doc,.docx,.ppt,.pptx,.xls,.xlsx" onChange={handleFileChange} />
      </div>

      {fileKey && (
        <div style={{ marginTop: 6 }}>
          Đã có file: <code>{fileKey}</code>{' '}
          {fileUrl && (
            <a href={fileUrl} target="_blank" rel="noreferrer">Xem nhanh</a>
          )}
        </div>
      )}

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

      <button type="submit" disabled={saving || uploading}>
        {saving ? 'Đang lưu...' : (isEdit ? 'Cập nhật' : 'Tạo mới')}
      </button>

      {msg && <div style={{ marginTop: 8 }}>{msg}</div>}
    </form>
  );
};

export default ArticleForm;
