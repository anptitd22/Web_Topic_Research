// src/components/AdminLogin.jsx
import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { API_BASE } from '../../api/api'
import { useAuth } from '../../auth/AuthContext';

const AdminLogin = () => {
  const history = useHistory();
  const [account, setAccount] = useState('');
  const [password, setPassword] = useState('');
  const [msg, setMsg] = useState('');
    const { loginAsAdmin } = useAuth();
    const [role] = useState('ADMIN');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg('');
    try {
      const res = await fetch(`${API_BASE}/admin/login`, {
        method: 'POST',
        credentials: 'include',                 // rất quan trọng để set cookie
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ account, password, role })
      });
      const d = await res.json().catch(() => ({}));
      if (!res.ok) throw new Error(d?.message || 'Đăng nhập thất bại');
      await loginAsAdmin(account, password, role);
      history.replace('/admin');
    } catch (err) {
      setMsg(err.message || 'Đăng nhập thất bại');
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: 360 }}>
      <h2>Admin Login</h2>
      <div>
        <label>Tài khoản</label>
        <input value={account} onChange={e => setAccount(e.target.value)} required />
      </div>
      <div>
        <label>Mật khẩu</label>
        <input type="password" value={password} onChange={e => setPassword(e.target.value)} required />
      </div>
      <button type="submit">Đăng nhập</button>
      {msg && <div style={{ color: 'red', marginTop: 8 }}>{msg}</div>}
    </form>
  );
};

export default AdminLogin;
