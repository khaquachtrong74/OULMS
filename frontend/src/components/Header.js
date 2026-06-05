import React, { useContext } from 'react';
import { Navbar, Container, Nav, Image, Button, Dropdown } from 'react-bootstrap';
import { BookHalf } from 'react-bootstrap-icons';
import { MyUserContext } from '../configs/Contexts';
import Apis, { endpoints } from '../configs/Apis';
import { ACTION_TYPE } from '../configs/constants';
import { useNavigate, NavLink } from 'react-router-dom';

import './Header.css'
const Header = () => {
    const [user, dispatch] = useContext(MyUserContext);
    const nav = useNavigate();

    const handleLogout = async () => {
        try {
            await Apis.post(endpoints['logout']);
            dispatch({ type: ACTION_TYPE.LOGOUT });
            nav('/login');
        } catch (error) {
            console.error(error);
        }
    };

    const getNavLinkClass = ({ isActive }) =>
        isActive
            ? 'header-link active-link'
            : 'header-link';

    return (
        <Navbar
    expand="lg"
    sticky="top"
    className="header-navbar shadow-sm"
>
            <Container>

                <Navbar.Brand
                    onClick={() => nav('/')}
                    className="d-flex align-items-center gap-3 m-0"
                    style={{ cursor: 'pointer' }}
                >
                    <div className="brand-icon">
                        <BookHalf size={20} />
                    </div>

                    <div>
                        <div className="brand-title">
                            OULMS
                        </div>

                        <div className="brand-subtitle">
                            Academic Knowledge Hub
                        </div>
                    </div>
                </Navbar.Brand>

                <Navbar.Toggle
                    aria-controls="main-navbar-nav"
                    className="border-0 shadow-none"
                />
                <Navbar.Collapse id="main-navbar-nav">
                    <Nav className="ms-auto align-items-center gap-lg-3">

                        <Nav.Link
                            as={NavLink}
                            to="/"
                            end
                            className={getNavLinkClass}
                        >
                            Trang chủ
                        </Nav.Link>

                        <Nav.Link
                            as={NavLink}
                            to="/documents"
                            className={getNavLinkClass}
                        >
                            Khám phá
                        </Nav.Link>

                        <Nav.Link
                            as={NavLink}
                            to="/forum"
                            className={getNavLinkClass}
                        >
                            Cộng đồng
                        </Nav.Link>

                        {user && ['ADMIN', 'LECTURER'].includes(user.role) && (
                            <Nav.Link
                                as={NavLink}
                                to="/documents-manage"
                                className={getNavLinkClass}
                            >
                                Quản trị
                            </Nav.Link>
                        )}

                        {user && (
                            <Nav.Link
                                as={NavLink}
                                to="/my-learning"
                                className={getNavLinkClass}
                            >
                                Góc học tập
                            </Nav.Link>
                        )}

                        {user ? (
                            <Dropdown align="end" className="ms-lg-3">

                                <Dropdown.Toggle
                                    as="div"
                                    className="user-trigger"
                                >
                                    <Image
                                        src={
                                            user.avatar ||
                                            'https://res.cloudinary.com/dsjxgri1t/image/upload/v1780048454/nxi0lpgryarwav9i5lej.png'
                                        }
                                        roundedCircle
                                        width={42}
                                        height={42}
                                        className="object-fit-cover"
                                    />

                                    <div className="d-none d-md-block">
                                        <div className="user-name">
                                            {user.firstName ||
                                                user.email?.split('@')[0]}
                                        </div>

                                        <div className="user-role">
                                            {user.role}
                                        </div>
                                    </div>
                                </Dropdown.Toggle>

                                <Dropdown.Menu
                                    className="border-0 shadow-lg mt-3"
                                >
                                    <Dropdown.Item
                                        onClick={() => nav('/profile')}
                                    >
                                        Hồ sơ cá nhân
                                    </Dropdown.Item>

                                    <Dropdown.Item
                                        onClick={() => nav('/transactions')}
                                    >
                                        Lịch sử giao dịch
                                    </Dropdown.Item>

                                    <Dropdown.Divider />

                                    <Dropdown.Item
                                        onClick={handleLogout}
                                        className="text-danger fw-semibold"
                                    >
                                        Đăng xuất
                                    </Dropdown.Item>

                                </Dropdown.Menu>

                            </Dropdown>
                        ) : (
                            <Button
                                className="login-btn ms-lg-3"
                                onClick={() => nav('/login')}
                            >
                                Đăng nhập
                            </Button>
                        )}

                    </Nav>
                </Navbar.Collapse>

            </Container>
        </Navbar>
    );
};

export default Header;