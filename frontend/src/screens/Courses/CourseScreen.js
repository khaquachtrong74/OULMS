import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import { Button, Col, Container, Form, InputGroup, Row, Spinner } from "react-bootstrap";
import { Search } from "react-bootstrap-icons";
import CourseCard from "../../components/CourseCard";

const CourseScreen = () => {
    const nav = useNavigate();
    
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [kw, setKw] = useState('');
    const [debouncedKw, setDebouncedKw] = useState('');

    useEffect(() => {
        const timer = setTimeout(() => {
            setDebouncedKw(kw);
        }, 500);
        return () => clearTimeout(timer);
    }, [kw]);

    const loadCourses = useCallback(async () => {
        setLoading(true);
        try {
            const queryParams = new URLSearchParams();
            if (debouncedKw) queryParams.append('kw', debouncedKw);

            const res = await Apis.get(`${endpoints['course']}?${queryParams.toString()}`);
            
            setCourses(res.data);
        } catch (err) {
            console.error("Lỗi khi tải danh sách khóa học:", err);
        } finally {
            setLoading(false);
        }
    }, [debouncedKw]);

    useEffect(() => {
        loadCourses();
    }, [loadCourses]);

    return (
        <Container fluid className="py-4 bg-light min-vh-100">
            <Container>
                <Row className="mb-4 align-items-center">
                    <Col md={6}>
                        <h2 className="fw-bold text-dark m-0">Khám phá Khóa học</h2>
                        <p className="text-muted mt-1 mb-0">Tìm kiếm các khóa học phù hợp với định hướng của bạn</p>
                    </Col>
                    <Col md={6} className="mt-3 mt-md-0">
                        <InputGroup className="shadow-sm">
                            <InputGroup.Text className="bg-white border-end-0">
                                <Search className="text-secondary" />
                            </InputGroup.Text>
                            <Form.Control
                                type="text"
                                placeholder="Nhập tên khóa học..."
                                value={kw}
                                onChange={(e) => setKw(e.target.value)}
                                className="border-start-0 shadow-none py-2"
                            />
                            <Button variant="primary" onClick={loadCourses}>Tìm kiếm</Button>
                        </InputGroup>
                    </Col>
                </Row>

                {/* Vùng hiển thị dữ liệu */}
                {loading ? (
                    <div className="text-center py-5">
                        <Spinner animation="border" variant="primary" />
                        <p className="text-secondary mt-2 fw-medium">Đang đồng bộ dữ liệu khóa học...</p>
                    </div>
                ) : courses.length === 0 ? (
                    <div className="text-center py-5 bg-white rounded-4 shadow-sm border border-secondary border-opacity-25">
                        <p className="text-secondary mb-0 fw-medium">Không tìm thấy khóa học nào phù hợp.</p>
                    </div>
                ) : (
                    <Row xs={1} md={2} lg={3} className="g-4">
                        {courses.map(course => (
                            <Col key={course.id}>
                                <CourseCard 
                                    course={course} 
                                    onClick={() => nav(`/courses/${course.id}`)} 
                                />
                            </Col>
                        ))}
                    </Row>
                )}
            </Container>
        </Container>
    );
};

export default CourseScreen;