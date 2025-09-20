// src/apiClient.js
import axios from 'axios';
import { API_BASE } from './api';

export const api = axios.create({
  baseURL: API_BASE,
  withCredentials: true, // nếu dùng cookie HttpOnly
});

// Đính Bearer nếu bạn vẫn lưu trong localStorage
api.interceptors.request.use((config) => {
  const t = localStorage.getItem('token');
  if (t) {
    config.headers.Authorization = `Bearer ${t}`;
    config.headers['X-Auth-Type'] = 'user';
  }
  return config;
});

// Auto-logout khi 401/403
api.interceptors.response.use(
  (res) => res,
  (err) => {
    const s = err?.response?.status;
    if (s === 401 || s === 403) {
      localStorage.removeItem('token');
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(err);
  }
);
