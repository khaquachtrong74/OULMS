import React, { useState, useEffect, useCallback, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Row, Col, Button, Spinner, Alert, Card, Pagination } from 'react-bootstrap';
import { ShieldCheck, CheckCircleFill, Lightbulb } from 'react-bootstrap-icons';
import { MyUserContext } from '../../configs/Contexts';
import CourseCard from '../../components/CourseCard';
import CourseHeader from '../../components/CourseHeader';
import courseApi from '../../configs/api/courseApi';
import paymentApi from '../../configs/api/paymentApi';

const useCourseDetail = (courseId) => {
    const [course, setCourse] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const loadCourseDetail = useCallback(async () => {
        setLoading(true);
        setError(null);

        try {
            const res = await courseApi.getDetail(courseId);
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

const useRelatedCourses = (subjectId, currentCourseId) => {
    const [relatedCourses, setRelatedCourses] = useState([]);
    const [loadingRelated, setLoadingRelated] = useState(true);

    useEffect(() => {
        if (!subjectId) {
            setLoadingRelated(false);
            return;
        }
        const fetchRelated = async () => {
            setLoadingRelated(true);
            try {
                const courseParams = `?subjectId=${subjectId}`;
                const res = await courseApi.getAll(courseParams);

                const filtered = res.data.data.filter(
                    c => c.id.toString() !== currentCourseId.toString()
                );
             

                setRelatedCourses(filtered);
            } catch (err) {
                console.error("Lỗi khi tải khóa học liên quan:", err);
            } finally {
                setLoadingRelated(false);
            }
        };

        fetchRelated();
    }, [subjectId, currentCourseId]);

    return { relatedCourses, loadingRelated };
};

const CourseMainContent = ({ course, onBack }) => {
    const nav = useNavigate();
    return (
        <div className="pe-lg-4">
            <CourseHeader
                course={course}
                onBack={() => nav('/my-learning')}
                backText="Quay lại Góc học tập"
            />

            <div
                className="course-description mb-5 text-dark"
                style={{ lineHeight: '1.8', fontSize: '1.05rem' }}
            >
                <h4 className="fw-bold mb-3">Mô tả khóa học</h4>

                {course.description ? (
                    <div dangerouslySetInnerHTML={{ __html: course.description }} />
                ) : (
                    <p className="text-muted">
                        Chưa có thông tin mô tả chi tiết cho khóa học này.
                    </p>
                )}
            </div>
        </div>
    );
};

const RelatedCoursesSection = ({ subjectId, currentCourseId }) => {
    const { relatedCourses, loadingRelated } = useRelatedCourses(subjectId, currentCourseId);
    const nav = useNavigate();

    const [currentPage, setCurrentPage] = useState(1);
    const pageSize = 3; 

    useEffect(() => {
        setCurrentPage(1);
    }, [subjectId]);

    if (loadingRelated)
        return (
            <div className="text-center py-5 mt-4 border-top">
                <Spinner animation="border" variant="info" size="sm" />
                <span className="ms-2 text-secondary small">Đang tìm khóa học liên quan...</span>
            </div>
        );

    if (!relatedCourses?.length) return null;

    const totalItems = relatedCourses.length;
    const totalPages = Math.ceil(totalItems / pageSize); // Hàm trần (Ceil)
    const startIndex = (currentPage - 1) * pageSize;
    const endIndex = startIndex + pageSize;

    const currentData = relatedCourses.slice(startIndex, endIndex);

    return (
        <div className="mt-5 pt-4 border-top border-2">
            <h4 className="fw-bold mb-4 text-dark d-flex align-items-center gap-2">
                <Lightbulb className="text-warning" />
                Các khóa học cùng chuyên môn
            </h4>

            {/* Khối Grid render dữ liệu của trang hiện tại */}
            <Row xs={1} md={2} lg={3} className="g-4 mb-4">
                {currentData.map(rc => (
                    <Col key={rc.id}>
                        <CourseCard
                            course={rc}
                            onClick={() => {
                                window.scrollTo({ top: 0, behavior: 'smooth' });
                                nav(`/courses/${rc.id}`);
                            }}
                        />
                    </Col>
                ))}
            </Row>

            {/* UI Pagination (Chỉ hiển thị nếu có lớn hơn 1 trang) */}
            {totalPages > 1 && (
                <div className="d-flex justify-content-center mt-4">
                    <Pagination>
                        <Pagination.First
                            onClick={() => setCurrentPage(1)}
                            disabled={currentPage === 1}
                        />
                        <Pagination.Prev
                            onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                            disabled={currentPage === 1}
                        />

                        {/* Render linh động các số trang */}
                        {[...Array(totalPages)].map((_, index) => {
                            const pageNumber = index + 1;
                            return (
                                <Pagination.Item
                                    key={pageNumber}
                                    active={pageNumber === currentPage}
                                    onClick={() => setCurrentPage(pageNumber)}
                                >
                                    {pageNumber}
                                </Pagination.Item>
                            );
                        })}

                        <Pagination.Next
                            onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                            disabled={currentPage === totalPages}
                        />
                        <Pagination.Last
                            onClick={() => setCurrentPage(totalPages)}
                            disabled={currentPage === totalPages}
                        />
                    </Pagination>
                </div>
            )}
        </div>
    );
};

const CourseSidebar = ({ course, onEnroll, enrolling }) => {
    const fallbackImage =
        "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?q=80&w=1200&auto=format&fit=crop";

    return (
        <div className="sticky-top" style={{ top: '80px', zIndex: 10 }}>
            <Card className="border-0 shadow-sm rounded-4 overflow-hidden">
                <div
                    className="position-relative bg-light border-bottom"
                    style={{ height: '220px' }}
                >
                    <img
                        src={fallbackImage}
                        alt="Course cover"
                        className="w-100 h-100"
                        style={{ objectFit: 'cover' }}
                    />
                </div>

                <Card.Body className="p-4">
                    <div className="mb-4">
                        <h2 className="fw-bold text-primary mb-1">
                            {course.price > 0
                                ? `${new Intl.NumberFormat('vi-VN').format(course.price)} ₫`
                                : 'Miễn phí'}
                        </h2>
                    </div>

                    <Button
                        variant="primary"
                        size="lg"
                        className="w-100 py-3 fw-bold rounded-pill mb-3 shadow-sm"
                        onClick={onEnroll}
                        disabled={enrolling}
                    >
                        {enrolling ? (
                            <Spinner size="sm" animation="border" />
                        ) : (
                            'Đăng ký học ngay'
                        )}
                    </Button>

                    <ul className="list-unstyled mb-0 mt-4 small text-secondary">
                        <li className="d-flex align-items-center gap-3 mb-3">
                            <ShieldCheck size={20} className="text-success" />
                            <span>Truy cập trọn đời</span>
                        </li>

                        <li className="d-flex align-items-center gap-3 mb-3">
                            <CheckCircleFill
                                size={18}
                                className="text-success ms-1"
                            />
                            <span>Học mọi lúc, mọi nơi</span>
                        </li>
                    </ul>
                </Card.Body>
            </Card>
        </div>
    );
};

const CourseDetailedScreen = () => {
    const { courseId } = useParams();
    const nav = useNavigate();

    const [user] = useContext(MyUserContext);

    const {
        course,
        loading,
        error,
        reloadCourse
    } = useCourseDetail(courseId);

    const [enrolling, setEnrolling] = useState(false);

    const handleEnroll = async () => {
        if (!user) {
            nav(`/login?next=/courses/${courseId}`);
            return;
        }

        setEnrolling(true);

        try {
            if (course.price > 0) {
                const res = await paymentApi.createZaloPay({
                        amount: course.price,
                        courseId,
                        userId: user.id
                    });
                if (res.data?.order_url) {
                    window.location.href = res.data.order_url;
                    return;
                }

                alert("Không thể khởi tạo cổng thanh toán ZaloPay lúc này.");
            } else {
                courseApi.enroll(courseId,{})

                alert("Ghi danh thành công!");
                reloadCourse();
            }
        } catch (err) {
            console.error(err);

            alert(
                err.response?.data?.message ||
                "Đã xảy ra lỗi khi xử lý. Vui lòng thử lại."
            );
        } finally {
            setEnrolling(false);
        }
    };

    if (loading) {
        return (
            <div className="min-vh-100 d-flex flex-column justify-content-center align-items-center bg-light">
                <Spinner animation="border" variant="primary" />
                <p className="mt-3 fw-medium text-secondary">
                    Đang tải dữ liệu khóa học...
                </p>
            </div>
        );
    }

    if (error || !course) {
        return (
            <Container className="min-vh-100 py-5 d-flex align-items-center justify-content-center bg-light">
                <Alert
                    variant="danger"
                    className="text-center rounded-4 shadow-sm p-5 w-100"
                    style={{ maxWidth: '500px' }}
                >
                    <h4 className="fw-bold mb-3">Lỗi tải trang</h4>

                    <p className="mb-4">
                        {error || "Khóa học này không tồn tại."}
                    </p>

                    <Button
                        variant="primary"
                        className="rounded-pill px-4"
                        onClick={() => nav('/courses')}
                    >
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

                        <RelatedCoursesSection
                            subjectId={course.subjectId}
                            currentCourseId={courseId}
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