import React, { useContext } from 'react';
import { Navbar, Container, Nav, Image, Button } from 'react-bootstrap';
import { BookHalf } from 'react-bootstrap-icons';
import { MyUserContext } from '../configs/Contexts';
import Apis, { endpoints } from '../configs/Apis';
import { ACTION_TYPE } from '../configs/constants';
import { useNavigate, NavLink } from 'react-router-dom'; // 1. Thêm NavLink vào đây

const Header = () => {
  const [user, dispatch] = useContext(MyUserContext);
  const nav = useNavigate();
  
  const handleLogout = async () => {
    try {
      await Apis.post(endpoints['logout']);
      dispatch({ type: ACTION_TYPE.LOGOUT });
      nav('/login');
    } catch (error) {
      console.error("Logout failed:", error);
    }
  };

  return (
    <Navbar bg="white" sticky="top" className="border-bottom py-3" expand="md" style={{ zIndex: 1050 }}>
      <Container>
        {/* Chuyển sang điểu hướng bằng onClick của React Router để tránh load lại trang */}
        <Navbar.Brand onClick={() => nav('/')} className="d-flex align-items-center gap-2 m-0 p-0 cursor-pointer" style={{ cursor: 'pointer' }}>
          <div className="bg-oulms-brand p-2 rounded text-white d-flex align-items-center justify-content-center">
            <BookHalf size={20} />
          </div>
          <span className="fs-4 fw-bold text-oulms-brand ">OULMS</span>
        </Navbar.Brand>

        <Navbar.Collapse id="main-navbar-nav">
          <Nav className="ms-auto gap-3 gap-md-4 py-3 py-md-0 align-items-center">
            
            {/* 2. Trang chủ: Đổi sang as={NavLink} và to="/" */}
            <Nav.Link 
              as={NavLink} 
              to="/" 
              className={({ isActive }) => 
                isActive 
                  ? "fw-bold text-dark border-bottom border-2 border-dark pb-1 px-0" 
                  : "fw-medium text-secondary pb-1 px-0"
              }
              end // Thêm end để chỉ active chính xác khi ở trang chủ cội nguồn
            >
              Trang chủ
            </Nav.Link>

            {/* Khám phá */}
            <Nav.Link 
              as={NavLink} 
              to="/explore" 
              className={({ isActive }) => 
                isActive 
                  ? "fw-bold text-dark border-bottom border-2 border-dark pb-1 px-0" 
                  : "fw-medium text-secondary pb-1 px-0"
              }
            >
              Khám phá
            </Nav.Link>

            {/* Quản lý tài liệu */}
            {user && ['ADMIN', 'LECTURER', 'STUDENT'].includes(user.role) && (
              <Nav.Link 
                as={NavLink} 
                to="/documents-manage" 
                className={({ isActive }) => 
                  isActive 
                    ? "fw-bold text-dark border-bottom border-2 border-dark pb-1 px-0" 
                    : "fw-medium text-secondary pb-1 px-0"
                }
              >
                Quản lý tài liệu
              </Nav.Link>
            )}

            {/* Tài liệu */}
            <Nav.Link 
              as={NavLink} 
              to="/test" 
              className={({ isActive }) => 
                isActive 
                  ? "fw-bold text-dark border-bottom border-2 border-dark pb-1 px-0" 
                  : "fw-medium text-secondary pb-1 px-0"
              }
            >
              Tài liệu
            </Nav.Link>

            {/* Khu vực Profile người dùng */}
            {user ? (
              <div className="d-flex align-items-center gap-2 ms-md-3 mt-3 mt-md-0 ps-md-4 border-start border-2">
                <span className="fw-bold text-dark">
                  {user.firstName || (user.email ? user.email.split('@')[0] : 'Người dùng')}
                </span>

                <Image
                  src={user.avatar || 'https://res.cloudinary.com/dsjxgri1t/image/upload/v1780048454/nxi0lpgryarwav9i5lej.png'}
                  roundedCircle
                  width={42}
                  height={42}
                  className="object-fit-cover shadow-sm border"
                  alt="User Avatar"
                  style={{ cursor: 'pointer' }}
                />
                <Button variant="outline-danger" onClick={handleLogout}>Đăng xuất</Button>
              </div>
            ) : (
              <Button variant="primary" onClick={() => nav('/login')}>Đăng nhập</Button>
            )}

          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;
