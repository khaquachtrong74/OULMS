import React, { useState, useEffect } from 'react';
import {
    Button,
    Card,
    Col,
    Container,
    Row,
    Badge,
    Spinner,
    Pagination
} from "react-bootstrap";
import {
    Book,
    Cpu,
    Database,
    Globe,
    FileEarmarkText,
    Clock,
    GraphUp,
    Lightbulb
} from "react-bootstrap-icons";
import "./HomePage.css";
import Apis, { endpoints } from '../../configs/Apis';

const HomeScreen = () => {
    const [latestDocuments, setLatestDocuments] = useState([]);
    const [subjects, setSubjects] = useState([]); 

    const [loadingDocs, setLoadingDocs] = useState(true);
    const [loadingSubjects, setLoadingSubjects] = useState(true); 

    const [currentPage, setCurrentPage] = useState(1);
    const ITEMS_PER_PAGE = 6;
    const pagedDocuments = latestDocuments.slice(
        (currentPage - 1) * ITEMS_PER_PAGE,
        currentPage * ITEMS_PER_PAGE
    );

    const totalPages = Math.ceil(latestDocuments.length / ITEMS_PER_PAGE);
    const fetchLatestDocs = async (searchKey = '') => {
        try {
            setLoadingDocs(true);
            const params = {};
            if (searchKey.trim() !== '') params.kw = searchKey;

            const response = await Apis.get(endpoints['document'], { params });
            if (response.data) {
                setLatestDocuments(response.data);
            }
        } catch (error) {
            console.error("Lỗi khi nạp tài liệu trang chủ:", error);
        } finally {
            setLoadingDocs(false);
        }
    };

    const fetchSubjects = async () => {
        try {
            setLoadingSubjects(true);
            const response = await Apis.get(endpoints['subject']);
            if (response.data) {
                setSubjects(Array.isArray(response.data) ? response.data : response.data.content || []);
            }
        } catch (error) {
            console.error("Lỗi khi nạp danh mục môn học:", error);
        } finally {
            setLoadingSubjects(false);
        }
    };

    useEffect(() => {
        fetchLatestDocs();
        fetchSubjects();
    }, []);


    const renderSubjectIcon = (index) => {
        const icons = [
            <Lightbulb size={28} />,
            <Database size={28} />,
            <Globe size={28} />,
            <Cpu size={28} />,
            <Book size={28} />
        ];
        return icons[index % icons.length];
    };

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

                    </div>
                </Container>
            </section>

            {/* CATEGORY SECTION (ĐÃ THAY BẰNG DỮ LIỆU THẬT) */}
            <section className="section-spacing">
                <Container>
                    <div className="section-header">
                        <h2>Danh mục môn học</h2>
                        <p>Khám phá tài liệu theo từng chuyên môn</p>
                    </div>

                    {loadingSubjects ? (
                        <div className="text-center py-4">
                            <Spinner animation="border" variant="primary" />
                        </div>
                    ) : (
                        <Row className="g-4">
                            {subjects.length > 0 ? (
                                subjects.slice(0, 4).map((s, index) => (
                                    <Col lg={3} md={6} key={s.id || index}>
                                        <Card className="category-card h-100">
                                            <Card.Body>
                                                <div className="category-icon">
                                                    {renderSubjectIcon(index)}
                                                </div>
                                                <h5>{s.subjectName || s.name || "Tên môn học"}</h5>

                                                <p className="text-muted text-sm mt-2">
                                                    {s.description || "Tài liệu học thuật chuyên ngành"}
                                                </p>
                                            </Card.Body>
                                        </Card>
                                    </Col>
                                ))
                            ) : (
                                <Col xs={12} className="text-center text-muted">
                                    Chưa có môn học nào.
                                </Col>
                            )}
                        </Row>
                    )}
                </Container>
            </section>

            <section className="section-spacing pt-0">
                <Container>
                    <div className="section-header">
                        <h2>Tài liệu mới cập nhật</h2>
                        <p>Cập nhật kiến thức mới nhất từ cộng đồng</p>
                    </div>

                    {loadingDocs ? (
                        <div className="text-center py-5">
                            <Spinner animation="border" variant="primary" className="mb-2" />
                            <div className="text-secondary">Đang nạp kho tài nguyên...</div>
                        </div>
                    ) : (
                        <Row className="g-4">
                            {latestDocuments.length > 0 ? (
                                pagedDocuments.map((doc) => ( // Lấy tối đa 6 tài liệu mới nhất
                                    <Col lg={4} md={6} key={doc.id}>
                                        <Card className="document-card h-100">
                                            <Card.Body className="d-flex flex-column">
                                                <div className="document-top">
                                                    <div className="document-icon">
                                                        <FileEarmarkText size={22} />                                                    </div>
                                                    <Badge bg="info" className="text-uppercase">
                                                        {doc.type || 'Tài liệu'}
                                                    </Badge>
                                                </div>

                                                <h5 className="document-title mt-3 flex-grow-1">
                                                    {doc.title}
                                                </h5>

                                                <div className="document-meta mt-auto pt-3">
                                                    <div>
                                                        <Book size={16} />                                                        <span>Mã môn: #{doc.topicId || doc.subjectId || 'N/A'}</span>
                                                    </div>
                                                    <div>
                                                        <Clock size={16} />
                                                        <span>
                                                            {doc.updatedAt
                                                                ? new Date(doc.updatedAt).toLocaleDateString('vi-VN')
                                                                : (doc.createdAt ? new Date(doc.createdAt).toLocaleDateString('vi-VN') : 'Mới cập nhật')}
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
                    {totalPages > 1 && (
                        <div className="d-flex justify-content-center mt-4">
                            <Pagination>
                                <Pagination.Prev
                                    disabled={currentPage === 1}
                                    onClick={() => setCurrentPage(p => p - 1)}
                                />

                                {[...Array(totalPages)].map((_, index) => (
                                    <Pagination.Item
                                        key={index}
                                        active={currentPage === index + 1}
                                        onClick={() => setCurrentPage(index + 1)}
                                    >
                                        {index + 1}
                                    </Pagination.Item>
                                ))}

                                <Pagination.Next
                                    disabled={currentPage === totalPages} onClick={() => setCurrentPage(p => p + 1)}
                                />
                            </Pagination>
                        </div>
                    )}
                </Container>
            </section>

            {/* STATS SECTION */}
            <section className="stats-section">
                <Container>
                    <Row className="g-4">
                        <Col md={4}>
                            <div className="stats-card">
                                <GraphUp size={32} />
                                <h3>{latestDocuments.length > 0 ? `${latestDocuments.length}+` : "0"}</h3>
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