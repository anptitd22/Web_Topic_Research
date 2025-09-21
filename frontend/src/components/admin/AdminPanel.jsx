// src/components/admin/AdminPanel.jsx
import React, { useEffect, useState } from 'react';
import { API_BASE } from '../../api/api';
import { Link } from 'react-router-dom';

const AdminPanel = () => {
  const [me, setMe] = useState(null);
  const [tags, setTags] = useState([]);
  const [loading, setLoading] = useState(true);
  const [msg, setMsg] = useState('');

  // form tạo mới
  const [newName, setNewName] = useState('');

  // form sửa
  const [editingId, setEditingId] = useState(null);
  const [editName, setEditName] = useState('');

  const commonInit = {
    credentials: 'include',
    headers: { 'X-Auth-Type': 'admin', 'Accept': 'application/json' }
  };

  const fetchMe = async () => {
    const res = await fetch(`${API_BASE}/admin/me`, { ...commonInit });
    const d = await res.json().catch(() => ({}));
    if (!res.ok) throw new Error(d?.message || 'Không xác thực được admin');
    return d?.data ?? d;
  };

  const fetchTags = async () => {
    const res = await fetch(`${API_BASE}/tags`, { credentials: 'include' });
    const d = await res.json().catch(() => ({}));
    if (!res.ok) throw new Error(d?.message || 'Không tải được danh sách tag');
    return Array.isArray(d?.data) ? d.data : (Array.isArray(d) ? d : []);
  };

  useEffect(() => {
    let ignore = false;
    (async () => {
      setLoading(true); setMsg('');
      try {
        const meRes = await fetchMe();
        const list = await fetchTags();
        if (!ignore) { setMe(meRes); setTags(list); }
      } catch (e) {
        if (!ignore) setMsg(e.message || 'Lỗi');
      } finally {
        if (!ignore) setLoading(false);
      }
    })();
    return () => { ignore = true; };
  }, []);

  const createTag = async (e) => {
    e.preventDefault();
    setMsg('');
    try {
      const res = await fetch(`${API_BASE}/tags`, {
        ...commonInit,
        method: 'POST',
        headers: { ...commonInit.headers, 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: newName.trim() })
      });
      const d = await res.json().catch(() => ({}));
      if (!res.ok) throw new Error(d?.message || 'Tạo tag thất bại');
      const created = d?.data ?? d;
      setTags(prev => [created, ...prev]);
      setNewName('');
      setMsg('Tạo tag thành công');
    } catch (e) {
      setMsg(e.message || 'Tạo tag thất bại');
    }
  };

  const startEdit = (t) => {
    const id = t.id || t._id;
    setEditingId(id);
    setEditName(t.name || '');
  };

  const cancelEdit = () => { setEditingId(null); setEditName(''); };

  const saveEdit = async (id) => {
    setMsg('');
    try {
      const res = await fetch(`${API_BASE}/tags?tagId=${encodeURIComponent(id)}`, {
        ...commonInit,
        method: 'PUT',
        headers: { ...commonInit.headers, 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: editName.trim() })
      });
      const d = await res.json().catch(() => ({}));
      if (!res.ok) throw new Error(d?.message || 'Cập nhật tag thất bại');
      const updated = d?.data ?? d;
      setTags(prev => prev.map(t => ((t.id || t._id) === id ? updated : t)));
      cancelEdit();
      setMsg('Cập nhật tag thành công');
    } catch (e) {
      setMsg(e.message || 'Cập nhật tag thất bại');
    }
  };

  const deleteTag = async (id) => {
    if (!window.confirm('Xóa tag này?')) return;
    setMsg('');
    try {
      const res = await fetch(`${API_BASE}/tags?tagId=${encodeURIComponent(id)}`, {
        ...commonInit, method: 'DELETE'
      });
      const d = await res.json().catch(() => ({}));
      if (!res.ok) throw new Error(d?.message || 'Xóa tag thất bại');
      setTags(prev => prev.filter(t => (t.id || t._id) !== id));
      setMsg('Xóa tag thành công');
    } catch (e) {
      setMsg(e.message || 'Xóa tag thất bại');
    }
  };

  if (loading) return <div>Đang tải...</div>;

  if (msg && /unauthorized|forbidden|401|403/i.test(msg)) {
    return (
      <div>
        <p style={{ color: 'red' }}>Bạn chưa đăng nhập admin hoặc không đủ quyền.</p>
        <Link to="/admin/login">Đến trang Admin Login</Link>
      </div>
    );
  }

  return (
    <div>
      <h2>Quản lý Tag</h2>
      {me && <p><em>Xin chào, {me.account || me.username || me.name || 'Admin'}.</em></p>}

      {/* Form tạo */}
      <form onSubmit={createTag} style={{ margin: '12px 0', padding: 12, border: '1px solid #eee' }}>
        <h4>Thêm Tag</h4>
        <input
          value={newName}
          onChange={(e) => setNewName(e.target.value)}
          placeholder="Tên tag"
          required
        />
        <button type="submit" style={{ marginLeft: 8 }} disabled={!newName.trim()}>
          Thêm
        </button>
      </form>

      {/* Danh sách */}
      {!tags.length ? (
        <div>Chưa có tag nào.</div>
      ) : (
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ textAlign: 'left', borderBottom: '1px solid #eee' }}>
              <th style={{ padding: 8 }}>Tên tag</th>
              <th style={{ padding: 8, width: 180 }}>Hành động</th>
            </tr>
          </thead>
          <tbody>
            {tags.map((t) => {
              const id = t.id || t._id;
              const isEditing = editingId === id;
              return (
                <tr key={id} style={{ borderBottom: '1px solid #f3f3f3' }}>
                  <td style={{ padding: 8 }}>
                    {isEditing ? (
                      <input value={editName} onChange={(e) => setEditName(e.target.value)} />
                    ) : (
                      t.name
                    )}
                  </td>
                  <td style={{ padding: 8 }}>
                    {isEditing ? (
                      <>
                        <button onClick={() => saveEdit(id)}>Lưu</button>
                        <button style={{ marginLeft: 6 }} onClick={cancelEdit} type="button">Hủy</button>
                      </>
                    ) : (
                      <>
                        <button onClick={() => startEdit(t)} type="button">Sửa</button>
                        <button style={{ marginLeft: 6 }} onClick={() => deleteTag(id)} type="button">Xóa</button>
                      </>
                    )}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      )}

      {msg && <div style={{ marginTop: 12, color: /thành công/i.test(msg) ? 'green' : 'red' }}>{msg}</div>}
    </div>
  );
};

export default AdminPanel;
