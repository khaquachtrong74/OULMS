import React, { useContext } from 'react';
import { Navbar, Container, Nav, Image } from 'react-bootstrap';
import { BookHalf } from 'react-bootstrap-icons';
import { MyUserContext } from '../configs/Contexts';

const Header = () => {
  const [user] = useContext(MyUserContext);
  
  return (
    <Navbar bg="white" sticky="top" className="border-bottom py-3" expand="md" style={{ zIndex: 1050 }}>
      <Container>
        <Navbar.Brand href="#" className="d-flex align-items-center gap-2 m-0 p-0 cursor-pointer">
          <div className="bg-oulms-brand p-2 rounded text-white d-flex align-items-center justify-content-center">
            <BookHalf size={20} />
          </div>
          <span className="fs-4 fw-bold text-oulms-brand ">OULMS</span>
        </Navbar.Brand>

        {/* <Navbar.Toggle aria-controls="main-navbar-nav" /> */}

        <Navbar.Collapse id="main-navbar-nav">
          
          {/* Thêm align-items-center để toàn bộ các nút trên thanh Nav được căn giữa theo trục Y */}
          <Nav className="ms-auto gap-3 gap-md-4 py-3 py-md-0 align-items-center">
            <Nav.Link href="#" className="fw-bold border-dark text-dark border-bottom border-2 pb-1 px-0">
              Trang chủ
            </Nav.Link>
            <Nav.Link href="#" className="fw-medium text-secondary">Khám phá</Nav.Link>
            <Nav.Link href="#" className="fw-medium text-secondary">Quản lý</Nav.Link>
            <Nav.Link href="#" className="fw-medium text-secondary">Tài liệu</Nav.Link>
            
            {/* Khu vực Profile người dùng */}
            {user && (
              <div className="d-flex align-items-center gap-2 ms-md-3 mt-3 mt-md-0 ps-md-4 border-start border-2">
                {/* Hiển thị tên: Lấy từ user.name, nếu không có thì lấy phần đầu của email */}
                <span className="fw-bold text-dark">
                  {user.firstName || (user.email ? user.email.split('@')[0] : 'Người dùng')}
                </span>
                
                {/* Component Image tự động render thành thẻ <img src="..." /> */}
                <Image 
                  src={user.avatar || 'https://res.cloudinary.com/dsjxgri1t/image/upload/v1780048454/nxi0lpgryarwav9i5lej.png'} 
                  roundedCircle // Biến ảnh vuông thành ảnh tròn trịa
                  width={42} 
                  height={42} 
                  className="object-fit-cover shadow-sm border" // Đảm bảo ảnh không bị méo tỷ lệ (aspect ratio) khi bị ép vào khung 42x42
                  alt="User Avatar"
                  style={{ cursor: 'pointer' }}
                />
              </div>
            )}
            
          </Nav>

        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;