import React, { useState, useEffect } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import { API_BASE } from '../../api/api';
import { useAuth } from '../../auth/AuthContext';

const LoginForm = ({ onLogin }) => {
    const [account, setAccount] = useState('');
    const [password, setPassword] = useState('');
    const [role] = useState('USER');
    const [error, setError] = useState('');
    const history = useHistory();
    const location = useLocation();
    const [info, setInfo] = useState('');
    const { loginAsUser } = useAuth();

    // Hiển thị thông báo nếu được chuyển hướng từ trang đăng ký

    useEffect(() => {
        if (location.state?.registered) {
            setInfo('Đăng ký thành công! Hãy đăng nhập.');
            // xoá state để refresh trang không lặp lại
            history.replace('/login');
        }
    }, [location.state, history]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {   
            const res = await fetch(`${API_BASE}/auth/login`, {
                method: 'POST',
                mode: 'cors',
                credentials: 'include', 
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ account, password, role })
            });
            const data = await res.json();
            if (res.ok && data.data) {
                localStorage.setItem('token', data.data);
                onLogin?.();
                await loginAsUser(account, password , role);
                history.replace('/');
            } else {
                setError(data.message || 'Đăng nhập thất bại');
            }
        } catch {
            setError('Lỗi kết nối server');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Đăng nhập</h2>
            <input value={account} onChange={e => setAccount(e.target.value)} placeholder="Tên đăng nhập" required />
            <input type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Mật khẩu" required />
            <button type="submit">Đăng nhập</button>
            {error && <div style={{color:'red'}}>{error}</div>}
        </form>
    );
};

export default LoginForm;