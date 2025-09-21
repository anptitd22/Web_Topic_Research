// src/auth/AuthContext.jsx
import React, { createContext, useContext, useEffect, useState } from 'react';
import { API_BASE } from '../api/api';

const Ctx = createContext(null);
export const useAuth = () => useContext(Ctx);

export const AuthProvider = ({ children }) => {
  const [ready, setReady] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [role, setRole] = useState(null);   // 'USER' | 'ADMIN' | null
  const [profile, setProfile] = useState(null);

  // Bootstrap phiên: thử user trước, rồi admin
  useEffect(() => {
    (async () => {
      try {
        const r1 = await fetch(`${API_BASE}/users/me`, { credentials: 'include' });
        if (r1.ok) {
          const d = await r1.json().catch(() => ({}));
          setIsLoggedIn(true);
          setRole(d?.data?.role || 'USER');
          setProfile(d?.data ?? null);
          return;
        }
      } catch {}
      try {
        const r2 = await fetch(`${API_BASE}/admin/me`, { credentials: 'include' });
        if (r2.ok) {
          const d = await r2.json().catch(() => ({}));
          setIsLoggedIn(true);
          setRole('ADMIN');
          setProfile(d?.data ?? null);
          return;
        }
      } catch {}
      setIsLoggedIn(false); setRole(null); setProfile(null);
    })().finally(() => setReady(true));
  }, []);

  // Cho phép truyền role nếu BE bắt buộc
  const loginAsUser = async (account, password, roleOpt) => {
    const body = roleOpt ? { account, password, role: roleOpt } : { account, password };
    const res = await fetch(`${API_BASE}/auth/login`, {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });
    const d = await res.json().catch(() => ({}));
    if (!res.ok) throw new Error(d?.message || 'Đăng nhập thất bại');

    // lấy profile & role
    const me = await fetch(`${API_BASE}/users/me`, { credentials: 'include' });
    const md = await me.json().catch(() => ({}));
    if (!me.ok) throw new Error(md?.message || 'Không lấy được thông tin người dùng');

    setIsLoggedIn(true);
    setRole(md?.data?.role || 'USER');
    setProfile(md?.data || null);
    return true;
  };

  const loginAsAdmin = async (account, password) => {
    const res = await fetch(`${API_BASE}/admin/login`, {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ account, password, role: 'ADMIN' }) // nếu BE cần
    });
    const d = await res.json().catch(() => ({}));
    if (!res.ok) throw new Error(d?.message || 'Đăng nhập admin thất bại');

    const me = await fetch(`${API_BASE}/admin/me`, { credentials: 'include' });
    const md = await me.json().catch(() => ({}));
    if (!me.ok) throw new Error(md?.message || 'Không lấy được thông tin admin');

    setIsLoggedIn(true);
    setRole('ADMIN');
    setProfile(md?.data || null);
    return true;
  };

  const logout = async () => {
    localStorage.removeItem('token');
    try { await fetch(`${API_BASE}/auth/logout`,  { method: 'POST', credentials: 'include' }); } catch {}
    try { await fetch(`${API_BASE}/auth/logout`, { method: 'POST', credentials: 'include' }); } catch {}
    setIsLoggedIn(false);
    setRole(null);
    setProfile(null);
  };

  return (
    <Ctx.Provider value={{ ready, isLoggedIn, role, profile, loginAsUser, loginAsAdmin, logout }}>
      {children}
    </Ctx.Provider>
  );
};
