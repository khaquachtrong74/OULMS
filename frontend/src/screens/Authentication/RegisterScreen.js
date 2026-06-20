import React, { useState, useRef } from "react";
import { Alert, Button, Form, Spinner, Image } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import { HttpStatusCode } from "axios";
import "./LoginPage.css"; 

const useRegisterForm = () => {
    const [user, setUser] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        confirmPassword: ""
    });
    const [avatar, setAvatar] = useState(null); 
    const [avatarPreview, setAvatarPreview] = useState(null); 
    const [err, setErr] = useState("");

    const validate = () => {
        if (!user.firstName.trim() || !user.lastName.trim()) {
            setErr("Vui lòng nhập đầy đủ họ và tên!");
            return false;
        }
        
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!user.email.trim() || !emailRegex.test(user.email)) {
            setErr("Email không hợp lệ!");
            return false;
        }
        
        if (!user.password.trim() || user.password.length < 6) {
            setErr("Mật khẩu phải có ít nhất 6 ký tự!");
            return false;
        }
        
        if (user.password !== user.confirmPassword) {
            setErr("Mật khẩu xác nhận không trùng khớp!");
            return false;
        }

        // [Suy luận] Validate cơ bản cho file avatar
        if (avatar) {
            if (!avatar.type.startsWith("image/")) {
                setErr("File avatar phải là định dạng hình ảnh!");
                return false;
            }
            if (avatar.size > 5 * 1024 * 1024) { // Giới hạn 5MB
                setErr("Kích thước ảnh không được vượt quá 5MB!");
                return false;
            }
        }
        
        setErr("");
        return true;
    };

    const handleChange = (field, value) => {
        setUser(prev => ({ ...prev, [field]: value }));
    };

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setAvatar(file);
            setAvatarPreview(URL.createObjectURL(file)); 
        }
    };

    return { user, avatar, avatarPreview, err, setErr, validate, handleChange, handleFileChange };
};

const RegisterScreen = () => {
    const { user, avatar, avatarPreview, err, setErr, validate, handleChange, handleFileChange } = useRegisterForm();
    const [loading, setLoading] = useState(false);
    const avatarInputRef = useRef(null);
    const nav = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        
        if (validate()) {
            try {
                setLoading(true);
                
                const formData = new FormData();
                formData.append("firstName", user.firstName);
                formData.append("lastName", user.lastName);
                formData.append("email", user.email);
                formData.append("password", user.password);
                
                if (avatar) {
                    formData.append("avatar", avatar);
                }

                let res = await Apis.post(endpoints["register"], formData);

                if (res.status === HttpStatusCode.Created || res.status === HttpStatusCode.Ok) {
                    nav("/login");
                }
            } catch (ex) {
                console.error(ex);
                setErr("Đã xảy ra lỗi trong quá trình tạo tài khoản. Vui lòng thử lại!");
            } finally {
                setLoading(false);
            }
        }
    };

    return (
        <div className="login-page">
            <div className="login-card" style={{ marginTop: '20px', marginBottom: '20px' }}>
                <div className="login-header">
                    <h1>Tạo tài khoản</h1>
                </div>
                
                {err && <Alert variant="danger">{err}</Alert>}
                
                <Form onSubmit={handleRegister}>
                    {/* Khu vực Upload Avatar */}
                    <div className="text-center mb-4">
                        <div 
                            style={{ width: '100px', height: '100px', borderRadius: '50%', backgroundColor: '#f0f2f5', margin: '0 auto', overflow: 'hidden', cursor: 'pointer', border: '1px solid #ddd' }}
                            onClick={() => avatarInputRef.current.click()}
                        >
                            {avatarPreview ? (
                                <Image src={avatarPreview} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                            ) : (
                                <div style={{ lineHeight: '100px', color: '#6c757d' }}>+ Ảnh đại diện</div>
                            )}
                        </div>
                        <Form.Control
                            type="file"
                            accept="image/*"
                            ref={avatarInputRef}
                            style={{ display: 'none' }}
                            onChange={handleFileChange}
                        />
                    </div>

                    <div className="d-flex gap-2 mb-3">
                        <Form.Group className="flex-fill">
                            <Form.Control
                                type="text"
                                placeholder="Họ"
                                value={user.lastName}
                                onChange={(e) => handleChange("lastName", e.target.value)}
                                required
                            />
                        </Form.Group>
                        <Form.Group className="flex-fill">
                            <Form.Control
                                type="text"
                                placeholder="Tên"
                                value={user.firstName}
                                onChange={(e) => handleChange("firstName", e.target.value)}
                                required
                            />
                        </Form.Group>
                    </div>

                    <Form.Group className="mb-3">
                        <Form.Control
                            type="email"
                            placeholder="Nhập email"
                            value={user.email}
                            onChange={(e) => handleChange("email", e.target.value)}
                            required
                        />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Control
                            type="password"
                            placeholder="Nhập mật khẩu"
                            value={user.password}
                            onChange={(e) => handleChange("password", e.target.value)}
                            required
                        />
                    </Form.Group>

                    <Form.Group className="mb-4">
                        <Form.Control
                            type="password"
                            placeholder="Nhập lại mật khẩu"
                            value={user.confirmPassword}
                            onChange={(e) => handleChange("confirmPassword", e.target.value)}
                            required
                        />
                    </Form.Group>

                    <Button type="submit" className="login-btn w-100" disabled={loading}>
                        {loading ? <Spinner animation="border" size="sm" /> : "Đăng ký ngay"}
                    </Button>
                </Form>

                 <div className="login-divider">
                    <span>Hoặc</span>
                </div>
                
                <div className="text-center mt-3">
                    <span className="text-secondary">Đã có tài khoản? </span>
                    <span 
                        style={{ color: '#0d6efd', cursor: 'pointer', fontWeight: '600' }} 
                        onClick={() => nav("/login")}
                    >
                        Đăng nhập
                    </span>
                </div>
            </div>
        </div>
    );
};

export default RegisterScreen;