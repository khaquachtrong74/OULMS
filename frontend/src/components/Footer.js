import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';

const Footer = () => {
  return (
    <footer className="w-100 bg-white border-top py-5">
      <Container>
        <Row className="align-items-center gy-4">
          
          <Col md={6}>
            <h3 className="fs-5 fw-bold text-oulms-brand mb-2">OULMS</h3>
            <p className="small text-secondary mb-0">
              &copy; OULMS. Nền tảng quản lý tri thức học thuật dành cho giáo dục đại học.
            </p>
          </Col>
          
          <Col md={6}>
            <div className="d-flex flex-wrap gap-3 justify-content-md-end small fw-medium">
              <a href="#" className="text-secondary text-decoration-underline">Trung tâm hỗ trợ</a>
              <a href="#" className="text-secondary text-decoration-underline">Điều khoản sử dụng</a>
              <a href="#" className="text-secondary text-decoration-underline">Chính sách bảo mật</a>
              <a href="#" className="text-secondary text-decoration-underline">Liên hệ hỗ trợ</a>
            </div>
          </Col>

        </Row>
      </Container>
    </footer>
  );
};

export default Footer;