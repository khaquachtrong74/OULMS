import { useContext } from "react";
import {
    Button,
    Card,
    Col,
    Container,
    Form,
    InputGroup,
    Row,
    Badge
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

import { MyUserContext } from "../../configs/Contexts";

import "./HomePage.css";

const HomeScreen = () => {

    const [user] = useContext(MyUserContext);

    const categories = [
        {
            title: "Trí tuệ nhân tạo",
            icon: <Brain size={28} />,
            docs: 128
        },
        {
            title: "Khoa học dữ liệu",
            icon: <Database size={28} />,
            docs: 94
        },
        {
            title: "Lập trình Web",
            icon: <Globe size={28} />,
            docs: 176
        },
        {
            title: "Kỹ thuật phần mềm",
            icon: <Cpu size={28} />,
            docs: 112
        }
    ];

    const latestDocuments = [
        {
            title: "Deep Learning for Medical Imaging",
            author: "Nguyễn Văn A",
            time: "2 giờ trước"
        },
        {
            title: "ReactJS Architecture Pattern",
            author: "Trần Minh K",
            time: "5 giờ trước"
        },
        {
            title: "Cloud Native Application",
            author: "Lê Hoàng P",
            time: "1 ngày trước"
        }
    ];

    return (
        <div className="homepage">

            {/* HERO SECTION */}
            <section className="hero-section">
                <Container>

                    <div className="hero-card">

                        <Badge className="hero-badge">
                            OULMS Academic Portal
                        </Badge>

                        <h1 className="hero-title">
                            Khám phá tri thức,
                            nâng tầm học thuật
                        </h1>

                        <p className="hero-description">
                            Tìm kiếm tài liệu, môn học và nguồn học thuật
                            chất lượng cao từ cộng đồng học tập.
                        </p>

                        <div className="quote-box">
                            <blockquote>
                                “Học vấn do người siêng năng đạt được,
                                tài sản do người tinh khôn sở hữu,
                                quyền lực do người dũng cảm nắm giữ.”
                            </blockquote>

                            <span>
                                — Hành trang tri thức
                            </span>
                        </div>

                        <div className="search-wrapper">
                            <InputGroup size="lg">

                                <Form.Control
                                    placeholder="Nhập tên môn học, giảng viên hoặc từ khóa tài liệu..."
                                />

                                <Button
                                    variant="warning"
                                    className="search-btn"
                                >
                                    <Search size={20} />
                                    <span>Tìm kiếm</span>
                                </Button>

                            </InputGroup>
                        </div>

                    </div>

                </Container>
            </section>

            {/* CATEGORY SECTION */}
            <section className="section-spacing">
                <Container>

                    <div className="section-header">
                        <h2>Danh mục môn học</h2>

                        <p>
                            Khám phá tài liệu theo từng chuyên môn
                        </p>
                    </div>

                    <Row className="g-4">

                        {categories.map((c, index) => (
                            <Col
                                lg={3}
                                md={6}
                                key={index}
                            >
                                <Card className="category-card h-100">

                                    <Card.Body>

                                        <div className="category-icon">
                                            {c.icon}
                                        </div>

                                        <h5>
                                            {c.title}
                                        </h5>

                                        <p>
                                            {c.docs} tài liệu
                                        </p>

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

                        <p>
                            Cập nhật kiến thức mới nhất từ cộng đồng
                        </p>
                    </div>

                    <Row className="g-4">

                        {latestDocuments.map((doc, index) => (
                            <Col
                                lg={4}
                                key={index}
                            >
                                <Card className="document-card h-100">

                                    <Card.Body>

                                        <div className="document-top">

                                            <div className="document-icon">
                                                <FileText size={22} />
                                            </div>

                                            <Badge bg="warning">
                                                Mới
                                            </Badge>

                                        </div>

                                        <h5 className="document-title">
                                            {doc.title}
                                        </h5>

                                        <div className="document-meta">

                                            <div>
                                                <BookOpen size={16} />
                                                <span>
                                                    {doc.author}
                                                </span>
                                            </div>

                                            <div>
                                                <Clock size={16} />
                                                <span>
                                                    {doc.time}
                                                </span>
                                            </div>

                                        </div>

                                        <Button
                                            variant="outline-primary"
                                            className="w-100 mt-4"
                                        >
                                            Xem tài liệu
                                        </Button>

                                    </Card.Body>

                                </Card>
                            </Col>
                        ))}

                    </Row>

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

                                <p>Tài liệu học tập</p>
                            </div>
                        </Col>

                        <Col md={4}>
                            <div className="stats-card">
                                <BookOpen size={32} />

                                <h3>320+</h3>

                                <p>Môn học</p>
                            </div>
                        </Col>

                        <Col md={4}>
                            <div className="stats-card">
                                <Brain size={32} />

                                <h3>4,500+</h3>

                                <p>Người học</p>
                            </div>
                        </Col>

                    </Row>

                </Container>
            </section>

        </div>
    );
};

export default HomeScreen;

