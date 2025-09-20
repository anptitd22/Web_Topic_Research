// src/components/ArticleList.jsx
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { API_BASE } from '../api';

const ArticleList = () => {
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState('');

  const fetchArticles = async () => {
    setLoading(true);
    setErr('');
    try {
      const res = await fetch(`${API_BASE}/articles`, { credentials: 'include' });
      const d = await res.json().catch(() => ({}));
      if (!res.ok) throw new Error(d?.message || 'Không tải được danh sách');
      const payload = d?.data ?? d; // backend trả ObjectResponse thì lấy d.data
      setArticles(Array.isArray(payload) ? payload : []);
    } catch (e) {
      setErr(e.message || 'Có lỗi xảy ra');
      setArticles([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchArticles(); }, []);

  if (loading) return <div>Đang tải...</div>;
  if (err) return <div style={{ color: 'red' }}>Lỗi: {err}</div>;
  if (!articles.length) return <div>Chưa có bài viết nào.</div>;

  return (
    <div>
      <h2>Research Articles</h2>
      <ul>
        {articles.map((a) => {
          const key = a.id || a._id;
          return (
            <li key={key}>
              <h3>
                <Link to={`/articles?articleId=${encodeURIComponent(key)}`}>
                  {a.title}
                </Link>
              </h3>
              {a.author && <p><strong>Author:</strong> {a.author}</p>}
              {a.content && <p>{a.content}</p>}
            </li>
          );
        })}
      </ul>

      <button onClick={fetchArticles}>Tải lại</button>
    </div>
  );
};

export default ArticleList;
