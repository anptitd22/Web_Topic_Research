import React from 'react';
import { Link } from 'react-router-dom';
import './Footer.css';

const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="footer">
      <div className="footer-container">
        {/* Footer Top */}
        <div className="footer-top">
          <div className="footer-section">
            <div className="footer-brand">
              <div className="footer-logo">
                <span className="footer-logo-icon">📚</span>
                <span className="footer-logo-text">Research Hub</span>
              </div>
              <p className="footer-description">
                Nền tảng chia sẻ và quản lý các bài viết nghiên cứu khoa học, 
                kết nối cộng đồng nghiên cứu Việt Nam.
              </p>
              <div className="footer-social">
                <a href="#" className="social-link" title="Facebook">
                  📘
                </a>
                <a href="#" className="social-link" title="Twitter">
                  🐦
                </a>
                <a href="#" className="social-link" title="LinkedIn">
                  💼
                </a>
                <a href="#" className="social-link" title="YouTube">
                  📺
                </a>
              </div>
            </div>
          </div>

          <div className="footer-section">
            <h4 className="footer-title">🔗 Liên kết nhanh</h4>
            <ul className="footer-links">
              <li><Link to="/">🏠 Trang chủ</Link></li>
              <li><Link to="/articles">📚 Bài viết</Link></li>
              <li><Link to="/about">ℹ️ Giới thiệu</Link></li>
              <li><Link to="/contact">📞 Liên hệ</Link></li>
            </ul>
          </div>

          <div className="footer-section">
            <h4 className="footer-title">📖 Danh mục</h4>
            <ul className="footer-links">
              <li><a href="/?category=technology">💻 Công nghệ</a></li>
              <li><a href="/?category=science">🔬 Khoa học</a></li>
              <li><a href="/?category=medicine">⚕️ Y học</a></li>
              <li><a href="/?category=education">🎓 Giáo dục</a></li>
              <li><a href="/?category=environment">🌱 Môi trường</a></li>
            </ul>
          </div>

          <div className="footer-section">
            <h4 className="footer-title">🚀 Tính năng</h4>
            <ul className="footer-links">
              <li><Link to="/register">✨ Đăng ký miễn phí</Link></li>
              <li><Link to="/articles/new">📝 Viết bài</Link></li>
              <li><a href="#search">🔍 Tìm kiếm nâng cao</a></li>
              <li><a href="#mobile">📱 Ứng dụng di động</a></li>
            </ul>
          </div>

          <div className="footer-section">
            <h4 className="footer-title">📧 Liên hệ</h4>
            <div className="footer-contact">
              <div className="contact-item">
                <span className="contact-icon">📍</span>
                <span>Hà Nội, Việt Nam</span>
              </div>
              <div className="contact-item">
                <span className="contact-icon">📞</span>
                <span>+84 123 456 789</span>
              </div>
              <div className="contact-item">
                <span className="contact-icon">📧</span>
                <span>info@researchhub.vn</span>
              </div>
              <div className="contact-item">
                <span className="contact-icon">🌐</span>
                <span>www.researchhub.vn</span>
              </div>
            </div>
          </div>
        </div>

        {/* Newsletter */}
        <div className="footer-newsletter">
          <div className="newsletter-content">
            <div className="newsletter-info">
              <h4>📩 Đăng ký nhận tin</h4>
              <p>Nhận thông báo về các bài viết mới và cập nhật từ Research Hub</p>
            </div>
            <form className="newsletter-form">
              <input 
                type="email" 
                placeholder="Nhập email của bạn..."
                className="newsletter-input"
              />
              <button type="submit" className="newsletter-btn">
                🚀 Đăng ký
              </button>
            </form>
          </div>
        </div>

        {/* Footer Bottom */}
        <div className="footer-bottom">
          <div className="footer-bottom-content">
            <div className="footer-copyright">
              <p>© {currentYear} Research Hub. Tất cả quyền được bảo lưu.</p>
            </div>
            
            <div className="footer-legal">
              <a href="/privacy">🔒 Chính sách bảo mật</a>
              <span className="separator">•</span>
              <a href="/terms">📋 Điều khoản sử dụng</a>
              <span className="separator">•</span>
              <a href="/cookies">🍪 Cookie</a>
            </div>

            <div className="footer-stats">
              <div className="stat-item">
                <span className="stat-number">1000+</span>
                <span className="stat-label">Bài viết</span>
              </div>
              <div className="stat-item">
                <span className="stat-number">500+</span>
                <span className="stat-label">Tác giả</span>
              </div>
              <div className="stat-item">
                <span className="stat-number">50+</span>
                <span className="stat-label">Chuyên ngành</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Back to Top Button */}
      <button 
        className="back-to-top"
        onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}
        title="Về đầu trang"
      >
        ⬆️
      </button>
    </footer>
  );
};

export default Footer;