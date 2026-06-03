import React, { useState, useEffect, useContext } from 'react';
import { Container, Row, Col, Card, Button, Form, InputGroup, Badge, Spinner } from 'react-bootstrap';
import { ChatDots, Search, PencilSquare, PersonCircle } from 'react-bootstrap-icons';
import { MyUserContext } from '../../configs/Contexts';
import Apis, { endpoints } from '../../configs/Apis';
import { useNavigate } from 'react-router-dom';
// [Suy luận] Giả định bạn có endpoint '/forums' trong Apis.js

const ForumScreen = () => {
    const [user] = useContext(MyUserContext);
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [searchTerm, setSearchTerm] = useState('');
    const nav = useNavigate();
    // Khởi tạo data giả lập để visualize trực quan khi chưa có backend
    useEffect(() => {
        setLoading(true);
        // Mô phỏng gọi API
        setTimeout(() => {
            setPosts([
                { id: 1, author: 'Nguyễn Văn A', role: 'STUDENT', title: 'Thắc mắc về thuật toán BFS', content: 'Có ai giải thích giúp mình cách queue hoạt động trong BFS không?', replies: 5, time: '2 giờ trước', tags: ['Cấu trúc dữ liệu'] },
                { id: 2, author: 'Trần Thị B', role: 'LECTURER', title: 'Cập nhật tài liệu ôn tập giữa kỳ', content: 'Các em vào mục Khám phá để tải tài liệu ôn tập nhé.', replies: 12, time: '5 giờ trước', tags: ['Thông báo'] }
            ]);
            setLoading(false);
        }, 800);
    }, []);

    return (
        <Container fluid className="py-4 bg-light min-vh-100">
            <Container>
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <h2 className="fw-bold text-dark d-flex align-items-center gap-2">
                        <ChatDots size={28} className="text-primary"/> Diễn đàn trao đổi
                    </h2>
                    {user && (
                        <Button onClick={()=>{
                            nav('/forum-create')
                        }
                        } variant="primary" className="fw-bold rounded-pill shadow-sm d-flex align-items-center gap-2">
                            <PencilSquare size={18} /> Đăng bài mới
                        </Button>
                    )}
                </div>

                <Row>
                    <Col md={8}>
                        {loading ? (
                            <div className="text-center py-5"><Spinner animation="border" variant="primary" /></div>
                        ) : (
                            <div className="d-flex flex-column gap-3">
                                {posts.map(post => (
                                    <Card key={post.id} className="border-0 shadow-sm rounded-4 cursor-pointer document-card">
                                        <Card.Body>
                                            <div className="d-flex align-items-center gap-2 mb-2">
                                                <PersonCircle size={32} className="text-secondary" />
                                                <div>
                                                    <span className="fw-bold text-dark d-block" style={{lineHeight: '1.2'}}>{post.author}</span>
                                                    <Badge bg={post.role === 'LECTURER' ? 'success' : 'info'} className="small">{post.role}</Badge>
                                                </div>
                                                <span className="text-muted ms-auto small">{post.time}</span>
                                            </div>
                                            <Card.Title className="fw-bold fs-5 mt-3">{post.title}</Card.Title>
                                            <Card.Text className="text-secondary">{post.content}</Card.Text>
                                            <div className="d-flex justify-content-between align-items-center mt-3 pt-3 border-top">
                                                <div>
                                                    {post.tags.map(tag => (
                                                        <Badge key={tag} bg="light" text="dark" className="border me-2">{tag}</Badge>
                                                    ))}
                                                </div>
                                                <span className="text-primary small fw-semibold">
                                                    {post.replies} bình luận
                                                </span>
                                            </div>
                                        </Card.Body>
                                    </Card>
                                ))}
                            </div>
                        )}
                    </Col>
                    
                    {/* Cột phải: Tìm kiếm & Chủ đề nổi bật */}
                    <Col md={4}>
                        <Card className="border-0 shadow-sm rounded-4 bg-white mb-4">
                            <Card.Body>
                                <h6 className="fw-bold mb-3">Tìm kiếm thảo luận</h6>
                                <InputGroup>
                                    <Form.Control
                                        placeholder="Nhập từ khóa..."
                                        className="bg-light border-0 shadow-none"
                                        value={searchTerm}
                                        onChange={(e) => setSearchTerm(e.target.value)}
                                    />
                                    <Button variant="light" className="border-0 bg-light text-secondary">
                                        <Search />
                                    </Button>
                                </InputGroup>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
        </Container>
    );
};

export default ForumScreen;