import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../../auth/AuthContext';
import './Header.css';

const Header = () => {
  const { user, logout } = useAuth();
  const location = useLocation();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  const isActivePath = (path) => {
    return location.pathname === path;
  };

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
  };

  const closeMobileMenu = () => {
    setIsMobileMenuOpen(false);
  };

  return (
    <header className="header">
      <nav className="navbar">
        <div className="navbar-container">
          {/* Logo */}
          <Link to="/" className="navbar-brand" onClick={closeMobileMenu}>
            <div className="logo">
              <span className="logo-icon">📚</span>
              <span className="logo-text">Research Hub</span>
            </div>
          </Link>

          {/* Mobile Menu Toggle */}
          <button 
            className={`mobile-menu-toggle ${isMobileMenuOpen ? 'active' : ''}`}
            onClick={toggleMobileMenu}
          >
            <span></span>
            <span></span>
            <span></span>
          </button>

          {/* Navigation Links */}
          <div className={`navbar-menu ${isMobileMenuOpen ? 'active' : ''}`}>
            <div className="navbar-nav">
              <Link 
                to="/" 
                className={`nav-link ${isActivePath('/') ? 'active' : ''}`}
                onClick={closeMobileMenu}
              >
                <span className="nav-icon">🏠</span>
                <span>Trang chủ</span>
              </Link>

              {user ? (
                <>
                  {user.role === 'USER' && (
                    <>
                      <Link 
                        to="/my-articles" 
                        className={`nav-link ${isActivePath('/my-articles') ? 'active' : ''}`}
                        onClick={closeMobileMenu}
                      >
                        <span className="nav-icon">📝</span>
                        <span>Bài viết của tôi</span>
                      </Link>
                      
                      <Link 
                        to="/articles/new" 
                        className={`nav-link ${isActivePath('/articles/new') ? 'active' : ''}`}
                        onClick={closeMobileMenu}
                      >
                        <span className="nav-icon">➕</span>
                        <span>Tạo bài viết</span>
                      </Link>
                    </>
                  )}

                  {user.role === 'ADMIN' && (
                    <Link 
                      to="/admin" 
                      className={`nav-link ${isActivePath('/admin') ? 'active' : ''}`}
                      onClick={closeMobileMenu}
                    >
                      <span className="nav-icon">⚙️</span>
                      <span>Quản trị</span>
                    </Link>
                  )}
                </>
              ) : (
                <>
                  <Link 
                    to="/login" 
                    className={`nav-link ${isActivePath('/login') ? 'active' : ''}`}
                    onClick={closeMobileMenu}
                  >
                    <span className="nav-icon">🔑</span>
                    <span>Đăng nhập</span>
                  </Link>
                </>
              )}
            </div>

            {/* User Menu */}
            <div className="navbar-user">
              {user ? (
                <div className="user-dropdown">
                  <div className="user-info">
                    <div className="user-avatar">
                      <span>👤</span>
                    </div>
                    <div className="user-details">
                      <span className="user-name">{user.username}</span>
                      <span className="user-role">
                        {user.role === 'ADMIN' ? '👑 Quản trị viên' : '👨‍💼 Người dùng'}
                      </span>
                    </div>
                  </div>
                  
                  <div className="user-actions">
                    <button 
                      onClick={() => {
                        logout();
                        closeMobileMenu();
                      }} 
                      className="btn btn-logout"
                    >
                      <span>🚪</span>
                      Đăng xuất
                    </button>
                  </div>
                </div>
              ) : (
                <div className="auth-buttons">
                  <Link 
                    to="/register" 
                    className="btn btn-primary"
                    onClick={closeMobileMenu}
                  >
                    <span>🎯</span>
                    Đăng ký
                  </Link>
                </div>
              )}
            </div>
          </div>
        </div>
      </nav>
    </header>
  );
};

export default Header;