import React, { useState, useEffect, useContext } from 'react';
import { Container, Row, Col, Spinner, Alert } from 'react-bootstrap';
import { Book } from 'react-bootstrap-icons';
import { useNavigate } from 'react-router-dom';
import Apis, { endpoints } from '../../configs/Apis';
import { MyUserContext } from '../../configs/Contexts';
import CourseCard from '../../components/CourseCard';

const MyLearningScreen = () => {
    const [user] = useContext(MyUserContext);
    const nav = useNavigate();
    const [myCourses, setMyCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Bảo vệ route: Nếu chưa đăng nhập thì đẩy về trang login
        if (!user) {
            nav('/login');
            return;
        }

        const fetchMyCourses = async () => {
            try {
                const res = await Apis.get(endpoints['myCourses']);
                setMyCourses(res.data);
            } catch (err) {
                console.error("Lỗi khi tải khóa học của tôi:", err);
                setError("Không thể tải danh sách khóa học. Vui lòng thử lại sau." + err);
            } finally {
                setLoading(false);
            }
        };

        fetchMyCourses();
    }, [user, nav]);

    return (
        <Container fluid className="py-5 bg-light min-vh-100">
            <Container>
                <div className="mb-4 pb-2 border-bottom border-secondary border-opacity-25">
                    <h2 className="fw-bold text-dark d-flex align-items-center gap-2">
                        <Book size={28} className="text-primary" /> Góc học tập của tôi
                    </h2>
                    <p className="text-secondary">Nơi lưu trữ các khóa học bạn đã ghi danh.</p>
                </div>

                {loading ? (
                    <div className="text-center py-5">
                        <Spinner animation="border" variant="primary" />
                        <p className="text-secondary mt-2 fw-medium">Đang tải dữ liệu học tập...</p>
                    </div>
                ) : error ? (
                    <Alert variant="danger">{error}</Alert>
                ) : myCourses.length === 0 ? (
                    <div className="text-center py-5 bg-white rounded shadow-sm border border-secondary border-opacity-25">
                        <p className="text-secondary mb-3 fw-medium fs-5">Bạn chưa tham gia khóa học nào.</p>
                        <button className="btn btn-primary fw-bold" onClick={() => nav('/explore?tab=course')}>
                            Khám phá khóa học ngay
                        </button>
                    </div>
                ) : (
                    <Row xs={1} md={2} lg={3} className="g-4">
                        {myCourses.map(course => (
                            <Col key={course.id}>
                                {/* Khi click vào sẽ đưa thẳng vào màn hình học nội dung chi tiết */}
                                <CourseCard 
                                    course={course} 
                                    onClick={() => nav(`/my-learning/${course.id}`)} 
                                />
                            </Col>
                        ))}
                    </Row>
                )}
            </Container>
        </Container>
    );
};

export default MyLearningScreen;