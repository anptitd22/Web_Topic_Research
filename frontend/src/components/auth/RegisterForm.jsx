import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { API_BASE } from '../../api/api';

const RegisterForm = () => {
    const [account, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [retypePassword, setRetypePassword] = useState('');
    const [role] = useState('USER');
    const [message, setMessage] = useState('');
    const history = useHistory();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const res = await fetch(`${API_BASE}/auth/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                mode: 'cors',
                body: JSON.stringify({ account, password , retypePassword , role }),
            });
            const data = await res.json();
            if (res.ok) {
                history.push('/login', { registered: true });
            } else {
                setMessage(data.message || 'Đăng ký thất bại');
            }
        } catch {
            setMessage('Lỗi kết nối server');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Đăng ký</h2>
            <input value={account} onChange={e => setUsername(e.target.value)} placeholder="Tên đăng nhập" required />
            <input type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Mật khẩu" required />
            <input type="password" value={retypePassword} onChange={e => setRetypePassword(e.target.value)} placeholder="Nhập lại mật khẩu" required />
            <button type="submit">Đăng ký</button>
            {message && <div>{message}</div>}
        </form>
    );
};

export default RegisterForm;