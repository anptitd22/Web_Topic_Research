// src/components/ArticleDetail.jsx
import React, { useEffect, useState } from 'react';
import { useLocation, Link } from 'react-router-dom';
import { API_BASE } from '../api';

const ArticleDetail = () => {
  const { search } = useLocation();
  const articleId = new URLSearchParams(search).get('articleId');

  const [article, setArticle] = useState(null);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState('');

  // trạng thái cho file
  const [fileUrl, setFileUrl] = useState('');
  const [fileLoading, setFileLoading] = useState(false);
  const [fileErr, setFileErr] = useState('');

  useEffect(() => {
    if (!articleId) { setErr('Thiếu articleId'); setLoading(false); return; }

    let ignore = false;
    (async () => {
      setLoading(true); setErr('');
      try {
        const res = await fetch(
          `${API_BASE}/articles?articleId=${encodeURIComponent(articleId)}`,
          { credentials: 'include' }
        );
        const d = await res.json().catch(() => ({}));
        if (!res.ok) throw new Error(d?.message || 'Không tải được bài viết');
        const payload = d?.data ?? d;
        if (!ignore) setArticle(payload || null);
      } catch (e) {
        if (!ignore) { setErr(e.message || 'Có lỗi xảy ra'); setArticle(null); }
      } finally {
        if (!ignore) setLoading(false);
      }
    })();

    return () => { ignore = true; };
  }, [articleId]);

  // khi đã có article → luôn xin presigned URL từ BE
  useEffect(() => {
    if (!article) return;

    // Key lưu trong DB (đặt theo BE của bạn)
    const key =
      article.fileKey ||
      article.articleKey ||
      article.documentKey ||
      article.file_path ||
      '';

    if (!key) {
      setFileUrl('');
      setFileErr('Bài viết chưa có file đính kèm');
      return;
    }
    getPresignedUrl(key);
  }, [article]);

  const getPresignedUrl = async (key) => {
    setFileLoading(true);
    setFileErr('');
    try {
      // SỬA endpoint đúng với BE: /articles/file-url?key=...
      const res = await fetch(
        `${API_BASE}/uploads/file-url?key=${encodeURIComponent(key)}`,
        { credentials: 'include' }
      );
      const d = await res.json().catch(() => ({}));
      if (!res.ok) throw new Error(d?.message || 'Không lấy được link file');

      const url = d?.data?.url || d?.data?.fileUrl || d?.url || d?.fileUrl;
      if (!url) throw new Error('Phản hồi thiếu URL');

      setFileUrl(url);
    } catch (e) {
      setFileUrl('');
      setFileErr(e.message || 'Không lấy được link file');
    } finally {
      setFileLoading(false);
    }
  };

  const openInNewTab = () => {
    if (!fileUrl) return;
    window.open(fileUrl, '_blank', 'noopener,noreferrer');
  };

  if (loading) return <div>Đang tải...</div>;
  if (err) return <div style={{ color: 'red' }}>Lỗi: {err}</div>;
  if (!article) return <div>Không tìm thấy bài viết.</div>;

  // chỉ để hiển thị tên file/đuôi nếu cần
  const keyForExt =
    article?.fileKey || article?.articleKey || article?.documentKey || '';
  const ext = (keyForExt.split('.').pop() || '').toLowerCase();

  return (
    <div>
      <h1>{article.title}</h1>
      <h3>By {article.author}</h3>
      {article.content && <p>{article.content}</p>}

      <div style={{ marginTop: 16 }}>
        <h4>Tài liệu đính kèm</h4>

        {fileLoading && <div>Đang lấy link tài liệu…</div>}
        {fileErr && <div style={{ color: 'red' }}>{fileErr}</div>}

        {!fileLoading && !fileErr && fileUrl && (
          <div>
            {/* Luôn mở TAB MỚI cho cả PDF/DOCX/… */}
            <button onClick={openInNewTab}>Mở tài liệu trong tab mới</button>
            {' '}|{' '}
            <a href={fileUrl} target="_blank" rel="noopener noreferrer">
              Mở bằng link
            </a>
            <div style={{ marginTop: 8, fontSize: 12, color: '#666' }}>
              File: {ext ? `.${ext}` : 'không rõ đuôi'}
            </div>
          </div>
        )}

        {!fileUrl && !fileLoading && !fileErr && (
          <div>Không có tài liệu để hiển thị.</div>
        )}
      </div>

      <div style={{ marginTop: 16 }}>
        <Link to="/">Quay lại danh sách</Link>{' '}
        <Link to={`/articles/edit?articleId=${encodeURIComponent(article.id || article._id)}`}>
          Sửa
        </Link>
      </div>
    </div>
  );
};

export default ArticleDetail;
