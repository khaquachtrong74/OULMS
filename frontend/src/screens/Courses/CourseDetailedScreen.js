import React, { useState, useEffect, useCallback, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Row, Col, Button, Spinner, Alert, Card } from 'react-bootstrap';
import { PlayCircle, ShieldCheck, CheckCircleFill, Clock, Person, ArrowLeft } from 'react-bootstrap-icons';
import Apis, { endpoints } from '../../configs/Apis';
import { MyUserContext } from '../../configs/Contexts';

import courseImageStatic from '../../asserts/images/Screenshot_20260529_121918.png'

// =====================================================================
// 1. DATA LAYER (Custom Hook giữ nguyên)
// =====================================================================
const useCourseDetail = (courseId) => {
    const [course, setCourse] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const loadCourseDetail = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            const res = await Apis.get(`${endpoints['courses']}/${courseId}`);
            setCourse(res.data);
        } catch (err) {
            console.error(err);
            setError("Không thể tải thông tin khóa học. Vui lòng kiểm tra lại kết nối.");
        } finally {
            setLoading(false);
        }
    }, [courseId]);

    useEffect(() => {
        loadCourseDetail();
    }, [loadCourseDetail]);

    return { course, loading, error, reloadCourse: loadCourseDetail };
};

// =====================================================================
// 2. UI COMPONENTS
// =====================================================================

/**
 * Cột trái: Nút Quay lại, Tiêu đề, Thông tin cơ bản và Mô tả chi tiết
 */
const CourseMainContent = ({ course, onBack }) => {
    const courseName = course.name || course.title || 'Đang cập nhật tên';
    const lecturerName = course.lecturerName || course.instructorName || 'Chưa cập nhật';
    const updatedAt = course.updatedAt || course.createdAt || Date.now();

    return (
        <div className="pe-lg-4">
            {/* Nút Quay lại đơn giản */}
            <Button
                variant="link"
                className="text-secondary text-decoration-none px-0 mb-4 d-inline-flex align-items-center gap-2"
                onClick={onBack}
            >
                <ArrowLeft size={18} /> Quay lại
            </Button>

            {/* Tiêu đề chính */}
            <h1 className="fw-bold mb-4 text-dark" style={{ fontSize: '2.5rem', letterSpacing: '-0.5px' }}>
                {courseName}
            </h1>

            {/* Meta info (Giảng viên, Cập nhật) */}
            <div className="d-flex flex-wrap gap-4 mb-4 pb-4 border-bottom text-secondary small fw-medium">
                <div className="d-flex align-items-center gap-2">
                    <Person size={18} />
                    <span>Tác giả: <strong className="text-dark">{lecturerName}</strong></span>
                </div>
                <div className="d-flex align-items-center gap-2">
                    <Clock size={18} />
                    <span>Cập nhật gần nhất: {new Date(updatedAt).toLocaleDateString('vi-VN')}</span>
                </div>
            </div>

            {/* Mô tả chi tiết lấy từ Database */}
            <div className="course-description mb-5 text-dark" style={{ lineHeight: '1.8', fontSize: '1.05rem' }}>
                <h4 className="fw-bold mb-3">Mô tả khóa học</h4>
                {course.description ? (
                    <div dangerouslySetInnerHTML={{ __html: course.description }} />
                ) : (
                    <p className="text-muted">Chưa có thông tin mô tả chi tiết cho khóa học này.</p>
                )}
            </div>
        </div>
    );
};

/**
 * Cột phải: Khối Sidebar dính (Sticky) chứa Video/Ảnh, Giá và Action Mua
 */
const CourseSidebar = ({ course, onEnroll, enrolling }) => {
    const coursePrice = course.price || 0;

    return (
        <div className="sticky-top" style={{ top: '30px', zIndex: 10 }}>
            <Card className="border-0 shadow-sm rounded-4 overflow-hidden">
                <div
                    className="position-relative bg-light d-flex justify-content-center align-items-center"
                    style={{ height: '220px', cursor: 'pointer' }}
                >
                    <div className="position-relative bg-light border-bottom" style={{ height: '220px' }}>
                        {/* 2. Truyền biến đã import vào src */}
                        <img
                            src={courseImageStatic}
                            alt="Course cover"
                            className="w-100 h-100"
                            style={{ objectFit: 'cover' }}
                        />
                    </div>
                </div>

                <Card.Body className="p-4">
                    <div className="mb-4">
                        <h2 className="fw-bold text-primary mb-1">
                            {coursePrice > 0 ? `${new Intl.NumberFormat('vi-VN').format(coursePrice)} ₫` : 'Miễn phí'}
                        </h2>
                    </div>

                    <Button
                        variant="primary"
                        size="lg"
                        className="w-100 py-3 fw-bold rounded-pill mb-3"
                        onClick={onEnroll}
                        disabled={enrolling}
                    >
                        {enrolling ? <Spinner size="sm" animation="border" /> : 'Đăng ký học ngay'}
                    </Button>

                    <ul className="list-unstyled mb-0 mt-4 small text-secondary">
                        <li className="d-flex align-items-center gap-3 mb-3">
                            <ShieldCheck size={20} className="text-success" />
                            <span>Truy cập trọn đời</span>
                        </li>
                        <li className="d-flex align-items-center gap-3 mb-3">
                            <CheckCircleFill size={18} className="text-success ms-1" />
                            <span>Học mọi lúc, mọi nơi</span>
                        </li>
                    </ul>
                </Card.Body>
            </Card>
        </div>
    );
};

// =====================================================================
// 3. MAIN COMPONENT
// =====================================================================
const CourseDetailedScreen = () => {
    const { courseId } = useParams();
    const nav = useNavigate();
    const [user] = useContext(MyUserContext);

    const { course, loading, error, reloadCourse } = useCourseDetail(courseId);
    const [enrolling, setEnrolling] = useState(false);

    const handleEnroll = async () => {
        if (!user) {
            nav(`/login?next=/courses/${courseId}`);
            return;
        }

        setEnrolling(true);
        try {
            const enrollEndpoint = `/api/courses/${courseId}/enroll`;
            await Apis.post(enrollEndpoint, {});
            alert("Ghi danh thành công!");
            reloadCourse();
        } catch (err) {
            console.error(err);
            alert(err.response?.data?.message || "Đã xảy ra lỗi khi đăng ký. Vui lòng thử lại.");
        } finally {
            setEnrolling(false);
        }
    };

    if (loading) {
        return (
            <div className="min-vh-100 d-flex flex-column justify-content-center align-items-center bg-white">
                <Spinner animation="border" variant="primary" />
                <p className="mt-3 fw-medium text-secondary">Đang tải dữ liệu khóa học...</p>
            </div>
        );
    }

    if (error || !course) {
        return (
            <Container className="min-vh-100 py-5 d-flex align-items-center justify-content-center bg-white">
                <Alert variant="danger" className="text-center rounded-4 shadow-sm p-5 w-100" style={{ maxWidth: '500px' }}>
                    <h4 className="fw-bold mb-3">Lỗi tải trang</h4>
                    <p className="mb-4">{error || "Khóa học này không tồn tại."}</p>
                    <Button variant="primary" className="rounded-pill px-4" onClick={() => nav('/courses')}>
                        Quay về danh sách
                    </Button>
                </Alert>
            </Container>
        );
    }

    return (
        <div className="min-vh-100 bg-white pt-4 pb-5">
            <Container>
                <Row className="g-5 flex-column-reverse flex-lg-row">
                    <Col lg={8}>
                        {/* Truyền hàm điều hướng vào onBack */}
                        <CourseMainContent
                            course={course}
                            onBack={() => nav(-1)}
                        />
                    </Col>

                    <Col lg={4}>
                        <CourseSidebar
                            course={course}
                            onEnroll={handleEnroll}
                            enrolling={enrolling}
                        />
                    </Col>
                </Row>
            </Container>
        </div>
    );
};

export default CourseDetailedScreen;