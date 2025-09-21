import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { API_BASE } from '../../api/api';
import { useAuth } from '../../auth/AuthContext';

const MyArticles = () => {
  const { profile } = useAuth(); // để biết id user, nếu cần
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [msg, setMsg] = useState('');

  const fetchMy = async () => {
    setLoading(true); setMsg('');
    try {
      const res = await fetch(`${API_BASE}/articles/my`, {
        credentials: 'include'
      });
      const d = await res.json().catch(() => ({}));
      if (!res.ok) throw new Error(d?.message || 'Không tải được danh sách');
      const payload = d?.data ?? d;
      setList(Array.isArray(payload) ? payload : []);
    } catch (e) {
      setMsg(e.message || 'Có lỗi xảy ra');
      setList([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchMy(); }, []);

  if (loading) return <div>Đang tải...</div>;
  if (msg) return <div style={{ color: 'red' }}>{msg}</div>;
  if (!list.length) return <div>Bạn chưa có bài viết nào.</div>;

  return (
    <div>
      <h2>Bài viết của tôi</h2>
      <ul>
        {list.map(a => {
          const id = a.id || a._id;
          const isOwner =
            (a.userId && profile?.id && a.userId === profile.id) ||
            (a.UserId && profile?.id && a.UserId === profile.id); // fallback nếu field BE đang là UserId

          return (
            <li key={id} style={{ marginBottom: 12 }}>
              <h3>
                <Link to={`/articles?articleId=${encodeURIComponent(id)}`}>
                  {a.title}
                </Link>
              </h3>
              {a.author && <p><strong>Tác giả:</strong> {a.author}</p>}
              {a.updatedAt && (
                <p style={{ fontSize: 12, color: '#666' }}>
                  Cập nhật: {new Date(a.updatedAt).toLocaleString()}
                </p>
              )}
              {/* Chỉ hiện link Sửa trong trang này (vì đã là bài của tôi) */}
              {isOwner && (
                <Link to={`/articles/edit?articleId=${encodeURIComponent(id)}`}>
                  Sửa
                </Link>
              )}
            </li>
          );
        })}
      </ul>
    </div>
  );
};

export default MyArticles;
