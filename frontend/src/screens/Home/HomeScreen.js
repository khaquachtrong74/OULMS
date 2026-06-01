import React, { useState, useEffect } from 'react';
import {
    Button,
    Card,
    Col,
    Container,
    Form,
    InputGroup,
    Row,
    Badge,
    Spinner
} from "react-bootstrap";
import {
    Search,
    BookOpen,
    Cpu,
    Database,
    Globe,
    Brain,
    FileText,
    Clock,
    TrendingUp
} from "lucide-react";
import "./HomePage.css";
import Apis, { endpoints } from '../../configs/Apis'; // Import instance Axios public của bạn

const HomeScreen = () => {
    // 1. Quản lý trạng thái dữ liệu từ API và bộ lọc tìm kiếm
    const [latestDocuments, setLatestDocuments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [kw, setKw] = useState(''); // Từ khóa tìm kiếm (Đồng bộ 'kw' với Backend)

    // 2. Hàm fetch tài liệu công khai từ Database
    const fetchLatestDocs = async (searchKey = '') => {
        try {
            setLoading(true);
            const params = {};
            if (searchKey.trim() !== '') params.kw = searchKey;

            // Gọi endpoint public '/documents'
            const response = await Apis.get(endpoints['document'], { params });
            if (response.data) {
                setLatestDocuments(response.data);
            }
        } catch (error) {
            console.error("Lỗi khi nạp tài liệu trang chủ:", error);
        } finally {
            setLoading(false);
        }
    };

    // Tự động nạp dữ liệu khi mở trang đầu tiên
    useEffect(() => {
        fetchLatestDocs();
    }, []);

    // Xử lý sự kiện khi bấm nút Tìm kiếm
    const handleSearchSubmit = (e) => {
        e.preventDefault();
        fetchLatestDocs(kw);
    };

    const categories = [
        { title: "Trí tuệ nhân tạo", icon: <Brain size={28} />, docs: 128 },
        { title: "Khoa học dữ liệu", icon: <Database size={28} />, docs: 94 },
        { title: "Lập trình Web", icon: <Globe size={28} />, docs: 176 },
        { title: "Kỹ thuật phần mềm", icon: <Cpu size={28} />, docs: 112 }
    ];

    return (
        <div className="homepage">
            {/* HERO SECTION */}
            <section className="hero-section">
                <Container>
                    <div className="hero-card">
                        <Badge className="hero-badge">OULMS Academic Portal</Badge>
                        <h1 className="hero-title">Khám phá tri thức, nâng tầm học thuật</h1>
                        <p className="hero-description">
                            Tìm kiếm tài liệu, môn học và nguồn học thuật chất lượng cao từ cộng đồng học tập.
                        </p>
                        <div className="quote-box">
                            <blockquote>
                                “Học vấn do người siêng năng đạt được, tài sản do người tinh khôn sở hữu, quyền lực do người dũng cảm nắm giữ.”
                            </blockquote>
                            <span>— Hành trang tri thức</span>
                        </div>

                        {/* THANH TÌM KIẾM HOÀN CHỈNH */}
                        <Form onSubmit={handleSearchSubmit} className="search-wrapper">
                            <InputGroup size="lg">
                                <Form.Control
                                    placeholder="Nhập tiêu đề tài liệu học tập cần tìm kiếm..."
                                    value={kw}
                                    onChange={(e) => setKw(e.target.value)}
                                />
                                <Button type="submit" variant="warning" className="search-btn">
                                    <Search size={20} />
                                    <span>Tìm kiếm</span>
                                </Button>
                            </InputGroup>
                        </Form>
                    </div>
                </Container>
            </section>

            {/* CATEGORY SECTION */}
            <section className="section-spacing">
                <Container>
                    <div className="section-header">
                        <h2>Danh mục môn học</h2>
                        <p>Khám phá tài liệu theo từng chuyên môn</p>
                    </div>
                    <Row className="g-4">
                        {categories.map((c, index) => (
                            <Col lg={3} md={6} key={index}>
                                <Card className="category-card h-100">
                                    <Card.Body>
                                        <div className="category-icon">{c.icon}</div>
                                        <h5>{c.title}</h5>
                                        <p>{c.docs} tài liệu</p>
                                    </Card.Body>
                                </Card>
                            </Col>
                        ))}
                    </Row>
                </Container>
            </section>

            {/* DOCUMENT SECTION */}
            <section className="section-spacing pt-0">
                <Container>
                    <div className="section-header">
                        <h2>Tài liệu mới cập nhật</h2>
                        <p>Cập nhật kiến thức mới nhất từ cộng đồng</p>
                    </div>

                    {/* Vùng điều kiện: Hiển thị loading hoặc danh sách dữ liệu thật */}
                    {loading ? (
                        <div className="text-center py-5">
                            <Spinner animation="border" variant="primary" className="mb-2" />
                            <div className="text-secondary">Đang nạp kho tài nguyên...</div>
                        </div>
                    ) : (
                        <Row className="g-4">
                            {latestDocuments.length > 0 ? (
                                latestDocuments.slice(0, 6).map((doc) => ( // Lấy tối đa 6 tài liệu mới nhất
                                    <Col lg={4} md={6} key={doc.id}>
                                        <Card className="document-card h-100">
                                            <Card.Body className="d-flex flex-column">
                                                <div className="document-top">
                                                    <div className="document-icon">
                                                        <FileText size={22} />
                                                    </div>
                                                    <Badge bg="info" className="text-uppercase">
                                                        {doc.type}
                                                    </Badge>
                                                </div>

                                                <h5 className="document-title mt-3 flex-grow-1">
                                                    {doc.title}
                                                </h5>

                                                <div className="document-meta mt-auto pt-3">
                                                    <div>
                                                        <BookOpen size={16} />
                                                        <span>Mã môn: #{doc.topicId}</span>
                                                    </div>
                                                    <div>
                                                        <Clock size={16} />
                                                        <span>
                                                            {doc.updatedAt 
                                                                ? new Date(doc.updatedAt).toLocaleDateString('vi-VN') 
                                                                : new Date(doc.createdAt).toLocaleDateString('vi-VN')}
                                                        </span>
                                                    </div>
                                                </div>

                                                <Button variant="outline-primary" className="w-100 mt-4">
                                                    Xem tài liệu
                                                </Button>
                                            </Card.Body>
                                        </Card>
                                    </Col>
                                ))
                            ) : (
                                <Col xs={12} className="text-center py-4 text-muted">
                                    Hiện chưa có tài liệu công khai nào hệ thống.
                                </Col>
                            )}
                        </Row>
                    )}
                </Container>
            </section>

            {/* STATS SECTION */}
            <section className="stats-section">
                <Container>
                    <Row className="g-4">
                        <Col md={4}>
                            <div className="stats-card">
                                <TrendingUp size={32} />
                                <h3>1,240+</h3>
                                <p>Tài liệu học tập học phần</p>
                            </div>
                        </Col>
                    </Row>
                </Container>
            </section>
        </div>
    );
};

export default HomeScreen;
