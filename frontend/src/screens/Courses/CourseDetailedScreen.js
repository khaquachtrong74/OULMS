import React, { useState, useEffect, useCallback, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Row, Col, Button, Spinner, Alert, Card, Badge } from 'react-bootstrap';
import { ShieldCheck, CheckCircleFill, Clock, Person, ArrowLeft, Tag } from 'react-bootstrap-icons';
import Apis, { endpoints } from '../../configs/Apis';
import { MyUserContext } from '../../configs/Contexts';

// [LƯU Ý]: Bạn cần tạo thư mục src/assets/images/ và bỏ 1 tấm ảnh tên default-course.jpg vào đó.
// Nếu chưa có file, bạn tạm thời comment dòng import này lại và dùng URL tĩnh ở dưới nhé.
// import defaultCourseImage from '../../assets/images/default-course.jpg'; 

// =====================================================================
// 1. DATA LAYER (Custom Hook - Single Responsibility Principle)
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
 * Cột trái: Đọc trực tiếp các field name, description, subjectName, lecturerName, updatedAt từ DTO
 */
const CourseMainContent = ({ course, onBack }) => {
    return (
        <div className="pe-lg-4">
            {/* Nút Quay lại */}
            <Button 
                variant="link" 
                className="text-secondary text-decoration-none px-0 mb-4 d-inline-flex align-items-center gap-2"
                onClick={onBack}
            >
                <ArrowLeft size={18} /> Quay lại
            </Button>

            {/* Badge Tên môn học */}
            <div className="mb-3">
                <Badge bg="info" className="text-dark px-3 py-2 rounded-pill fw-medium d-inline-flex align-items-center gap-2">
                    <Tag size={14} /> {course.subjectName}
                </Badge>
            </div>

            {/* Tiêu đề chính */}
            <h1 className="fw-bold mb-4 text-dark" style={{ fontSize: '2.5rem', letterSpacing: '-0.5px' }}>
                {course.name}
            </h1>

            {/* Meta info (Giảng viên, Cập nhật) */}
            <div className="d-flex flex-wrap gap-4 mb-4 pb-4 border-bottom text-secondary small fw-medium">
                <div className="d-flex align-items-center gap-2">
                    <Person size={18} />
                    <span>Giảng viên: <strong className="text-dark">{course.lecturerName}</strong></span>
                </div>
                <div className="d-flex align-items-center gap-2">
                    <Clock size={18} />
                    <span>Cập nhật gần nhất: {new Date(course.updatedAt).toLocaleDateString('vi-VN')}</span>
                </div>
            </div>

            {/* Mô tả chi tiết HTML */}
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
 * Cột phải: Đọc trực tiếp field price từ DTO
 */
const CourseSidebar = ({ course, onEnroll, enrolling }) => {
    // Nếu bạn chưa import file nội bộ, dùng link này để test tạm
    const fallbackImage = "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?q=80&w=1200&auto=format&fit=crop";

    return (
        <div className="sticky-top" style={{ top: '80px', zIndex: 10 }}>
            <Card className="border-0 shadow-sm rounded-4 overflow-hidden">
                {/* Khu vực Thumbnail */}
                <div 
                    className="position-relative bg-light border-bottom" 
                    style={{ height: '220px' }}
                >
                    <img 
                        // src={defaultCourseImage} // Bỏ comment dòng này nếu dùng ảnh import từ thư mục dự án
                        src={fallbackImage}
                        alt="Course cover" 
                        className="w-100 h-100" 
                        style={{ objectFit: 'cover' }} 
                    />
                </div>
                
                <Card.Body className="p-4">
                    <div className="mb-4">
                        <h2 className="fw-bold text-primary mb-1">
                            {course.price > 0 ? `${new Intl.NumberFormat('vi-VN').format(course.price)} ₫` : 'Miễn phí'}
                        </h2>
                    </div>
                    
                    <Button 
                        variant="primary" 
                        size="lg"
                        className="w-100 py-3 fw-bold rounded-pill mb-3 shadow-sm"
                        onClick={onEnroll}
                        disabled={enrolling}
                    >
                        {enrolling ? <Spinner size="sm" animation="border"/> : 'Đăng ký học ngay'}
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
            <div className="min-vh-100 d-flex flex-column justify-content-center align-items-center bg-light">
                <Spinner animation="border" variant="primary" />
                <p className="mt-3 fw-medium text-secondary">Đang tải dữ liệu khóa học...</p>
            </div>
        );
    }

    if (error || !course) {
        return (
            <Container className="min-vh-100 py-5 d-flex align-items-center justify-content-center bg-light">
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
        <div className="min-vh-100 bg-light pt-4 pb-5">
            <Container>
                <Row className="g-5 flex-column-reverse flex-lg-row">
                    <Col lg={8}>
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