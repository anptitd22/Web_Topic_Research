import axios from 'axios';
import { API_BASE } from '../api';

const http = axios.create({
  baseURL: API_BASE,
  withCredentials: true, // để gửi cookie HttpOnly
});

// (tuỳ chọn) nếu vẫn dùng Bearer:
// http.interceptors.request.use((config) => {
//   const t = localStorage.getItem('token');
//   if (t) config.headers.Authorization = `Bearer ${t}`;
//   return config;
// });

export default http;
