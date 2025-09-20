import React, { useEffect, useState } from 'react';
import { API_BASE } from '../api';

const AdminPanel = () => {
    const [users, setUsers] = useState([]);
    const token = localStorage.getItem('token');

    useEffect(() => {
        fetch(`${API_BASE}/admin/users`, {
            headers: { 'Authorization': `Bearer ${token}` }
        })
        .then(res => res.json())
        .then(data => setUsers(data));
    }, [token]);

    const handleDelete = async (id) => {
        await fetch(`${API_BASE}/admin/users/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });
        setUsers(users.filter(u => u.id !== id && u._id !== id));
    };

    return (
        <div>
            <h2>Quản lý người dùng</h2>
            <ul>
                {users.map(user => (
                    <li key={user._id || user.id}>
                        {user.username}
                        <button onClick={() => handleDelete(user._id || user.id)}>Xóa</button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default AdminPanel;