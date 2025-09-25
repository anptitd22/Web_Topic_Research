// src/App.jsx
import React from 'react';
import { BrowserRouter as Router, Route, Switch, Redirect, Link } from 'react-router-dom';
import { API_BASE } from './api/api';
import MyArticles from './components/user/MyArticles';

// USER pages
import ArticleList from './components/user/ArticleList';
import ArticleForm from './components/user/ArticleForm';
import ArticleDetail from './components/user/ArticleDetail';

// AUTH pages
import LoginForm from './components/auth/LoginForm';
import RegisterForm from './components/auth/RegisterForm';

// ADMIN pages
import AdminPanel from './components/admin/AdminPanel';
import AdminLogin from './components/admin/AdminLogin';

import Header from './components/user/Header';
import Footer from './components/user/Footer';

import './index.css';

import { AuthProvider, useAuth } from './auth/AuthContext';

/* ---------- Guards ---------- */

// Chỉ cho khách vào
const GuestOnlyRoute = ({ component: Comp, redirectTo = '/', ...rest }) => {
  const { isLoggedIn } = useAuth();
  return (
    <Route
      {...rest}
      render={(props) => (isLoggedIn ? <Redirect to={redirectTo} /> : <Comp {...props} />)}
    />
  );
};

// Yêu cầu đăng nhập + đúng vai trò
const RoleRoute = ({ component: Comp, allowed = [], loginPath = '/login', ...rest }) => {
  const { ready, isLoggedIn, role } = useAuth();
  return (
    <Route
      {...rest}
      render={(props) => {
        if (!ready) return <div>Đang kiểm tra phiên đăng nhập…</div>;
        if (!isLoggedIn) return <Redirect to={{ pathname: loginPath, state: { from: props.location } }} />;
        if (allowed.length && !allowed.includes(role)) return <Redirect to="/403" />;
        return <Comp {...props} />;
      }}
    />
  );
};

/* ---------- Nav ---------- */
const Nav = () => {
  const { isLoggedIn, role, logout } = useAuth();

  return (
    <div style={{ marginBottom: 12 }}>
      <Link to="/">Trang chủ</Link>
      {role === 'USER'  && <> | <Link to="/articles/new">Tạo bài viết</Link></>}
      {/* {role === 'ADMIN' && <> | <Link to="/admin">Admin</Link></>} */}
      {/* {!isLoggedIn && <> | <Link to="/admin/login">Đăng nhập Admin</Link></>} */}
      {' '}|{' '}
      {role === 'USER' && <> | <Link to="/my-articles">Bài viết của tôi</Link></>}
      {role === 'ADMIN' && <> | <Link to="/admin">Quản Lý</Link></>}
      {isLoggedIn ? (
        <button onClick={logout}>Đăng xuất</button>
      ) : (
        <>
          <Link to="/login">Đăng nhập</Link> | <Link to="/register">Đăng ký</Link>
        </>
      )}
    </div>
  );
};

/* ---------- Nhóm route USER ---------- */
const UserRoutes = () => (
  <Switch>
    {/* Public */}
    <Route path="/" exact component={ArticleList} />
    <RoleRoute path="/articles" exact component={ArticleDetail} allowed={['USER', 'ADMIN']}/>

    {/* Cần login (USER hoặc ADMIN) */}
    <RoleRoute path="/articles/new" component={ArticleForm} allowed={['USER', 'ADMIN']} />
    <RoleRoute path="/articles/edit" component={ArticleForm} allowed={['USER', 'ADMIN']} />

    <RoleRoute path="/my-articles" component={MyArticles} allowed={['USER']} />

    <Route render={() => <Redirect to="/" />} />
  </Switch>
);

/* ---------- Nhóm route ADMIN (child) ---------- */
const AdminRoutes = ({ match }) => (
  <Switch>
    <RoleRoute
      path={`${match.path}`}
      exact
      component={AdminPanel}
      allowed={['ADMIN']}
      loginPath="/admin/login"   // <-- nếu chưa login admin, sang đúng trang login admin
    />
    <Route render={() => <Redirect to={match.path} />} />
  </Switch>
);

// AppBody
<Switch>
  {/* ĐẶT trang login admin TRƯỚC nhóm /admin */}
  <GuestOnlyRoute path="/admin/login" component={AdminLogin} />

  {/* KHÔNG exact ở đây để nhận cả /admin và /admin/... */}
  <Route path="/admin" render={(props) => <AdminRoutes {...props} />} />

  {/* ... các route khác */}
</Switch>

/* ---------- AppBody ---------- */
const AppBody = () => {
  const onLogin = () => { /* AuthContext sẽ cập nhật role/isLoggedIn */ };

  return (
    // <div className="app">
    //   <Header />
      <main className="main-content">
        <h1>Research Articles Management</h1>
        <Nav />
        <Switch>
          {/* 1) Đặt trang đăng nhập admin TRƯỚC nhóm /admin */}
          <GuestOnlyRoute path="/admin/login" component={AdminLogin} />

          {/* 2) Nhóm ADMIN (không exact để còn nhận /admin/...); bên trong dùng RoleRoute + loginPath */}
          <Route path="/admin" render={(props) => <AdminRoutes {...props} />} />

          {/* Auth thường */}
          <GuestOnlyRoute path="/login" component={() => <LoginForm onLogin={onLogin} />} />
          <GuestOnlyRoute path="/register" component={RegisterForm} />

          {/* Nhóm USER */}
          <Route path="/" component={UserRoutes} />

          <Route path="/403" render={() => <div>403 - Không đủ quyền</div>} />
        </Switch>
      </main>
      // <Footer />
    /* </div> */
  );
};

/* ---------- App ---------- */
const App = () => (
  <AuthProvider>
    <Router>
      <AppBody />
    </Router>
  </AuthProvider>
);

export default App;
