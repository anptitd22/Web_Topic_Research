import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

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

export default RoleRoute;