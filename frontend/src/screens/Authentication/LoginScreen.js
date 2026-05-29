import { useContext, useState } from "react";
import Apis, { endpoints } from "../../configs/Apis";
import { ACTION_TYPE, COOKIE_KEYS } from "../../configs/constants";
import { useNavigate, useSearchParams } from "react-router-dom";
import { MyUserContext } from "../../configs/Contexts";
import cookies from "react-cookies";
import { Alert, Button, Form, Spinner } from "react-bootstrap";
import { HttpStatusCode } from "axios";

import "./LoginPage.css";

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

        const emailRegex =
            /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailRegex.test(user.email)) {
            setErr("Email không hợp lệ!");
            return false;
        }

        if (!user.password?.trim()) {
            setErr("Vui lòng nhập mật khẩu!");
            return false;
        }

        if (user.password.length < 6) {
            setErr(
                "Mật khẩu phải có ít nhất 6 ký tự!"
            );
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

                let res = await Apis.post(endpoints["login"], {
                    ...user,
                });

                if (res.status === HttpStatusCode.Ok) {

                    let userProfile = await Apis.get(
                        endpoints["profile"]
                    );

                    cookies.save(
                        COOKIE_KEYS.USER,
                        userProfile.data
                    );

                    dispatch({
                        type: ACTION_TYPE.LOGIN,
                        payload: userProfile.data,
                    });

                    let next = q.get("next");

                    if (next)
                        nav(next);
                    else
                        nav("/");
                }

            } catch (ex) {
                console.error(ex);
                setErr("Sai email hoặc mật khẩu!");
            } finally {
                setLoading(false);
            }
        }
    };

    return (
        <div className="login-page">
            <div className="login-card">

                <div className="login-header">
                    <div className="login-logo">
                        O
                    </div>

                    <h1>
                        Chào mừng trở lại
                    </h1>

                    <p>
                        Đăng nhập để tiếp tục sử dụng hệ thống
                    </p>
                </div>

                {err && (
                    <Alert variant="danger">
                        {err}
                    </Alert>
                )}

                <Form onSubmit={handleLogin}>

                    {userInfo.map((u) => (
                        <Form.Group
                            className="mb-4"
                            key={u.field}
                        >
                            <Form.Label>
                                {u.label}
                            </Form.Label>

                            <Form.Control
                                type={u.type}
                                placeholder={u.label}
                                value={user[u.field] || ""}
                                onChange={(e) =>
                                    setUser({
                                        ...user,
                                        [u.field]:
                                            e.target.value,
                                    })
                                }
                                required
                            />
                        </Form.Group>
                    ))}

                    <div className="forgot-password">
                        <a href="#">
                            Quên mật khẩu?
                        </a>
                    </div>

                    <Button
                        type="submit"
                        className="login-btn"
                        disabled={loading}
                    >
                        {loading ? (
                            <>
                                <Spinner
                                    animation="border"
                                    size="sm"
                                />
                                <span className="ms-2">
                                    Đang đăng nhập...
                                </span>
                            </>
                        ) : (
                            "Đăng nhập"
                        )}
                    </Button>
                </Form>
            </div>
        </div>
    );
};

export default LoginPage;