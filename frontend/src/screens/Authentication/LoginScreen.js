import { useContext, useState } from "react";
import { ACTION_TYPE, COOKIE_KEYS } from "../../configs/constants";
import { useNavigate, useSearchParams } from "react-router-dom";
import { MyUserContext } from "../../configs/Contexts";
import cookies from "react-cookies";
import { Alert, Button, Form, Spinner } from "react-bootstrap";
import { HttpStatusCode } from "axios";
import { GoogleLogin } from "@react-oauth/google";
import "./LoginPage.css";
import authApi from "../../configs/api/authApi";

const LoginPage = () => {
  const userInfo = [
    {
      field: "email",
      label: "Email đăng nhập",
      type: "text",
    },
    {
      field: "password",
      label: "Mật khẩu",
      type: "password",
    },
  ];

  const [user, setUser] = useState({});
  const [err, setErr] = useState("");
  const [loading, setLoading] = useState(false);

  const [, dispatch] = useContext(MyUserContext);
  const [q] = useSearchParams();

  const nav = useNavigate();

  const validate = () => {
    if (!user.email?.trim()) {
      setErr("Vui lòng nhập email!");
      return false;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailRegex.test(user.email)) {
      setErr("Email không hợp lệ!");
      return false;
    }

    if (!user.password?.trim()) {
      setErr("Vui lòng nhập mật khẩu!");
      return false;
    }

    if (user.password.length < 6) {
      setErr("Mật khẩu phải có ít nhất 6 ký tự!");
      return false;
    }

    setErr("");
    return true;
  };

  const handleLogin = async (e) => {
    e.preventDefault();

    if (validate()) {
      try {
        setLoading(true);

        let res = await authApi.login({ ...user });

        if (res.status === HttpStatusCode.Ok) {
          let userProfile = await authApi.getProfile();
          cookies.save(COOKIE_KEYS.USER, userProfile.data);

          dispatch({
            type: ACTION_TYPE.LOGIN,
            payload: userProfile.data,
          });

          let next = q.get("next");

          if (next) nav(next);
          else nav("/");
        }
      } catch (ex) {
        console.error(ex);
        setErr("Sai email hoặc mật khẩu!");
      } finally {
        setLoading(false);
      }
    }
  };

  const handleLoginGoogle = async (credentialRespone) => {
    let res = await authApi.loginGoogle(
      {
        idToken: credentialRespone.credential,
      },
      {
        headers: {
          "Content-type": "application/json",
        },
      },
    );
    if (res.status === HttpStatusCode.Ok || res.status === 200) {
      let userProfile = await authApi.getProfile();

      cookies.save(COOKIE_KEYS.USER, userProfile.data);

      dispatch({
        type: ACTION_TYPE.LOGIN,
        payload: userProfile.data,
      });

      let next = q.get("next");
      if (next) {
        nav(next);
      } else {
        nav("/");
      }
    }
  };
  return (
    <div className="login-page">
      <div className="login-card">
        <div className="login-header">
          <div className="login-logo">O</div>

          <h1>Chào mừng trở lại</h1>

          <p>Đăng nhập để tiếp tục sử dụng hệ thống</p>
        </div>

        {err && <Alert variant="danger">{err}</Alert>}

        <Form onSubmit={handleLogin}>
          {userInfo.map((u) => (
            <Form.Group className="mb-4" key={u.field}>
              <Form.Label>{u.label}</Form.Label>

              <Form.Control
                type={u.type}
                placeholder={u.label}
                value={user[u.field] || ""}
                onChange={(e) =>
                  setUser({
                    ...user,
                    [u.field]: e.target.value,
                  })
                }
                required
              />
            </Form.Group>
          ))}

          <div className="forgot-password">
            <a href="#">Quên mật khẩu?</a>
          </div>

          <div className="auth-actions">
            <Button type="submit" className="login-btn" disabled={loading}>
              {loading ? (
                <>
                  <Spinner animation="border" size="sm" />
                  <span className="ms-2">Đang đăng nhập...</span>
                </>
              ) : (
                "Đăng nhập"
              )}
            </Button>

            <Button
              variant="outline-primary"
              className="register-btn"
              onClick={() => nav("/register")}
            >
              Đăng ký
            </Button>
          </div>
        </Form>
        <div className="login-divider">
          <span>Hoặc</span>
        </div>

        <div className="google-login-wrapper">
          <GoogleLogin
            onSuccess={handleLoginGoogle}
            onError={() => {
              console.log("Login Failed");
            }}
            width="380"
          />
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
