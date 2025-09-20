import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Switch, Redirect, Link } from 'react-router-dom';
import { API_BASE } from './api';
import ArticleList from './components/ArticleList';
import ArticleForm from './components/ArticleForm';
import ArticleDetail from './components/ArticleDetail';
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import AdminPanel from './components/AdminPanel';

// Guard cho các route cần login
const ProtectedRoute = ({ component: Comp, isLoggedIn, ...rest }) => (
  <Route
    {...rest}
    render={(props) =>
      isLoggedIn ? (
        <Comp {...props} />
      ) : (
        <Redirect to={{ pathname: '/login', state: { from: props.location } }} />
      )
    }
  />
);

// (Tuỳ chọn) chặn vào /login, /register khi đã đăng nhập
const GuestOnlyRoute = ({ component: Comp, isLoggedIn, redirectTo = '/', ...rest }) => (
  <Route
    {...rest}
    render={(props) =>
      isLoggedIn ? <Redirect to={redirectTo} /> : <Comp {...props} />
    }
  />
);

const App = () => {
  // Nếu bạn đang dùng token trong localStorage, giá trị khởi tạo sẽ là true/false theo đó.
  // Nếu bạn dùng cookie HttpOnly, ban đầu có thể false — sẽ chuyển thành true sau khi ping /auth/me.
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('token'));

  // Nếu dùng cookie HttpOnly: ping /auth/me để xác thực trạng thái đăng nhập
  useEffect(() => {
    async function check() {
      try {
        const r = await fetch(`${API_BASE}/auth/me`, { credentials: 'include' });
        if (r.ok) setIsLoggedIn(true);
        else {
          localStorage.removeItem('token');
          setIsLoggedIn(false);
        }
      } catch {
        localStorage.removeItem('token');
        setIsLoggedIn(false);
      }
    }
    check();
  }, []);

  const handleLogin = () => setIsLoggedIn(true);

  const handleLogout = async () => {
    localStorage.removeItem('token'); // nếu trước đó có lưu
    try {
      // nếu backend có xóa cookie ở /auth/logout
      await fetch(`${API_BASE}/auth/logout`, { method: 'POST', credentials: 'include' });
    } catch {}
    setIsLoggedIn(false);
    // Không cần điều hướng thủ công: nếu đang ở route bảo vệ, ProtectedRoute sẽ tự redirect sang /login
  };

  return (
    <Router>
      <div>
        <h1>Research Articles Management</h1>

        {isLoggedIn ? (
          <>
            <Link to="/">Trang chủ</Link> |{' '}
            <Link to="/articles/new">Tạo bài viết</Link> |{' '}
            {/* <Link to="/admin">Admin</Link> |{' '} */}
            <button onClick={handleLogout}>Đăng xuất</button>
          </>
        ) : (
          <>
            <Link to="/login">Đăng nhập</Link> | <Link to="/register">Đăng ký</Link>
          </>
        )}

        <Switch>
          {/* Trang chủ cũng yêu cầu đăng nhập */}
          <Route path="/" exact component={ArticleList}/>

          {/* Các route cần login */}
          <ProtectedRoute path="/articles/new" component={ArticleForm} isLoggedIn={isLoggedIn} />
          <ProtectedRoute path="/articles/edit" component={ArticleForm} isLoggedIn={isLoggedIn} />
          <ProtectedRoute path="/admin" component={AdminPanel} isLoggedIn={isLoggedIn} />

          {/* Route public */}
          <ProtectedRoute path="/articles" component={ArticleDetail} isLoggedIn={isLoggedIn}/>

          {/* Không cho vào login/register nếu đã đăng nhập */}
          <GuestOnlyRoute path="/login" component={() => <LoginForm onLogin={handleLogin} />} isLoggedIn={isLoggedIn} redirectTo="/" />
          <GuestOnlyRoute path="/register" component={RegisterForm} isLoggedIn={isLoggedIn} redirectTo="/" />

          {/* Fallback */}
          <Route render={() => <Redirect to="/" />} />
        </Switch>
      </div>
    </Router>
  );
};

export default App;
