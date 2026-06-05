import React from 'react';
import { Container, Button } from 'react-bootstrap';
import { CheckCircleFill } from 'react-bootstrap-icons';
import { useNavigate } from 'react-router-dom';

const PaymentSuccessScreen = () => {
    const nav = useNavigate();
    // const [searchParams] = useSearchParams();

    return (
        <Container className="min-vh-100 d-flex flex-column align-items-center justify-content-center text-center">
            <CheckCircleFill size={80} className="text-success mb-4" />
            <h2 className="fw-bold mb-3">Thanh Toán Thành Công!</h2>
            <p className="text-secondary mb-4">
                Hệ thống đang xử lý giao dịch. Khóa học sẽ được thêm vào tài khoản của bạn trong giây lát.
            </p>
            <Button variant="primary" className="rounded-pill px-4" onClick={() => nav('/explore')}>
                Trở về danh sách khóa học
            </Button>
        </Container>
    );
};

export default PaymentSuccessScreen;