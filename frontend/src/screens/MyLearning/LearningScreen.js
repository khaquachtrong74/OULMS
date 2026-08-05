import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Row, Col, Card, Button, Spinner,  Alert } from 'react-bootstrap';
import { CameraVideo, PersonVideo3,  InfoCircle } from 'react-bootstrap-icons';
import CourseHeader from '../../components/CourseHeader';
import courseApi from '../../configs/api/courseApi';

const LearningScreen = () => {
    const { courseId } = useParams();
    const nav = useNavigate();
    const [course, setCourse] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const mockMeetUrl = "https://meet.google.com/abc-xyz-def";

    const loadCourseData = useCallback(async () => {
        setLoading(true);
        try {
            const res = await courseApi.getDetail(courseId);
            setCourse(res.data);
        } catch (err) {
            console.error("Lỗi khi tải dữ liệu học tập:", err);
            setError("Không thể tải thông tin khóa học. Vui lòng thử lại sau.");
        } finally {
            setLoading(false);
        }
    }, [courseId]);

    useEffect(() => {
        loadCourseData();
    }, [loadCourseData]);

    if (loading) {
        return (
            <Container className="min-vh-100 d-flex justify-content-center align-items-center">
                <Spinner animation="border" variant="primary" />
                <span className="ms-3 fw-medium text-secondary">Đang tải không gian học tập...</span>
            </Container>
        );
    }

    if (error || !course) {
        return (
            <Container className="py-5">
                <Alert variant="danger">{error || "Khóa học không tồn tại."}</Alert>
                <Button variant="outline-primary" onClick={() => nav('/my-learning')}>Quay lại Góc học tập</Button>
            </Container>
        );
    }

    return (
        <div className="min-vh-100 bg-light pt-4 pb-5">
            <Container>
               <CourseHeader 
                    course={course}
                    onBack={() => nav('/my-learning')}
                    backText="Quay lại Góc học tập"
                />

                <Row className="g-4">
                    {/* CỘT TRÁI: Thông tin giảng viên & Khóa học */}
                    <Col lg={8}>
                        <Card className="border-0 shadow-sm rounded-4 mb-4">
                            <Card.Body className="p-4">
                                <h5 className="fw-bold mb-4 d-flex align-items-center gap-2 text-dark">
                                    <PersonVideo3 className="text-primary"/> Thông Tin Giảng Viên
                                </h5>
                                <div className="d-flex align-items-center p-3 bg-light rounded-3 border">
                                    <div 
                                        className="bg-primary text-white rounded-circle d-flex justify-content-center align-items-center fw-bold fs-4"
                                        style={{ width: '60px', height: '60px' }}
                                    >
                                       GV
                                    </div>
                                    <div className="ms-3">
                                        <h6 className="fw-bold mb-1 text-dark">{course.lecturerName || 'Đang cập nhật'}</h6>
                                        <span className="text-secondary small">Giảng viên phụ trách chuyên môn</span>
                                    </div>
                                </div>
                            </Card.Body>
                        </Card>

                        <Card className="border-0 shadow-sm rounded-4">
                            <Card.Body className="p-4">
                                <h5 className="fw-bold mb-3 d-flex align-items-center gap-2 text-dark">
                                    <InfoCircle className="text-info"/> Tổng Quan Khóa Học
                                </h5>
                                <div 
                                    className="text-dark" 
                                    style={{ lineHeight: '1.8' }}
                                    dangerouslySetInnerHTML={{ __html: course.description || '<p class="text-muted">Chưa có mô tả chi tiết.</p>' }}
                                />
                            </Card.Body>
                        </Card>
                    </Col>

                    {/* CỘT PHẢI: Live Class / Google Meet */}
                    <Col lg={4}>
                        <Card className="border-0 shadow-sm rounded-4 sticky-top" style={{ top: '90px' }}>
                            <Card.Body className="p-4 text-center">
                                <div className="mb-3 d-flex justify-content-center">
                                    <div className="bg-danger bg-opacity-10 p-3 rounded-circle text-danger">
                                        <CameraVideo size={32} />
                                    </div>
                                </div>
                                <h5 className="fw-bold text-dark mb-2">Lớp Học Trực Tuyến</h5>
                                <p className="text-secondary small mb-4">
                                    Tham gia buổi học trực tiếp qua Google Meet cùng giảng viên và các học viên khác.
                                </p>
                                
                                <a 
                                    href={mockMeetUrl} 
                                    target="_blank" 
                                    rel="noopener noreferrer" 
                                    className="btn btn-primary w-100 fw-bold py-2 rounded-pill shadow-sm"
                                >
                                    Tham Gia Cùng Giảng Viên
                                </a>

                                <hr className="my-4 text-secondary opacity-25" />
                                
                                <div className="text-start">
                                    <h6 className="fw-bold small text-dark mb-3">Lưu ý khi tham gia:</h6>
                                    <ul className="text-secondary small ps-3 mb-0" style={{ lineHeight: '1.7' }}>
                                        <li>Sử dụng email sinh viên để đăng nhập.</li>
                                        <li>Tắt micro khi không phát biểu.</li>
                                        <li>Vào lớp đúng giờ theo lịch đã thông báo.</li>
                                    </ul>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
        </div>
    );
};

export default LearningScreen;