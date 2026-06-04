import React, { useState, useEffect, useCallback } from 'react';
import { Container, Row, Col, Form, InputGroup, Button, Spinner, Nav } from 'react-bootstrap';
import { Search, Filter } from 'react-bootstrap-icons';
import Apis, { endpoints } from '../../configs/Apis'; 
import { useNavigate, useSearchParams } from 'react-router-dom'; // Bổ sung useSearchParams
import DocumentCard from '../../components/DocumentCard';
import CourseCard from '../../components/CourseCard';

const ExploreCourseScreen = () => {
    const nav = useNavigate();
    
    // 1. Khởi tạo useSearchParams để tương tác với URL
    const [searchParams, setSearchParams] = useSearchParams();

    // 2. Lấy giá trị tab từ URL. Nếu không có (null) hoặc gõ bậy bạ, mặc định là 'document'
    const activeTab = searchParams.get('tab') === 'course' ? 'course' : 'document';

    // Bỏ dòng này: const [activeTab, setActiveTab] = useState('document'); 

    const [loading, setLoading] = useState(false);
    const [documents, setDocuments] = useState([]);
    const [courses, setCourses] = useState([]);

    const [filters, setFilters] = useState({ kw: '', subjectId: '', levelId: '', topicId: '' });
    const [debouncedKw, setDebouncedKw] = useState('');
    
    const [subjects, setSubjects] = useState([]);
    const [levels, setLevels] = useState([]);
    const [topics, setTopics] = useState([]);

    // 3. Hàm chuyển tab: Ghi đè tham số 'tab' lên URL
    const handleTabChange = (tabName) => {
        // Lấy tất cả các param hiện tại trên URL (để không làm mất các param khác nếu có)
        const currentParams = Object.fromEntries([...searchParams]);
        // Cập nhật lại param tab và đẩy lên URL
        setSearchParams({ ...currentParams, tab: tabName });
    };

    useEffect(() => {
        const timer = setTimeout(() => setDebouncedKw(filters.kw), 500);
        return () => clearTimeout(timer);
    }, [filters.kw]);

    useEffect(() => {
        const fetchMetadata = async () => {
            try {
                const [resSub, resLev, resTop] = await Promise.all([
                    Apis.get(endpoints['subject']),
                    Apis.get(endpoints['level']),
                    Apis.get(endpoints['topic'])
                ]);
                setSubjects(resSub.data);
                setLevels(resLev.data);
                setTopics(resTop.data);
            } catch (error) {
                console.error("Lỗi khi tải metadata:", error);
            }
        };
        fetchMetadata();
    }, []);

    const loadData = useCallback(async () => {
        setLoading(true);
        try {
            const queryParams = new URLSearchParams();
            if (debouncedKw) queryParams.append('kw', debouncedKw);
            if (filters.subjectId) queryParams.append('subjectId', filters.subjectId);

            if (activeTab === 'document') {
                if (filters.topicId) queryParams.append('topicId', filters.topicId);
                if (filters.levelId) queryParams.append('levelId', filters.levelId);
                
                const res = await Apis.get(`${endpoints.document}?${queryParams.toString()}`);
                setDocuments(res.data);
            } else if (activeTab === 'course') {
                const res = await Apis.get(`${endpoints['courses']}?${queryParams.toString()}`);
                setCourses(res.data);
            }
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, [activeTab, debouncedKw, filters.subjectId, filters.topicId, filters.levelId]);

    useEffect(() => {
        loadData();
    }, [loadData]);

    const handleFilterChange = (type, value) => {
        setFilters(prev => ({ ...prev, [type]: value }));
    };

    return (
        <Container fluid className="py-4 bg-light min-vh-100">
            <Container>
                <Row>
                    {/* ... (Phần Sidebar Filter giữ nguyên code cũ) ... */}
                    <Col md={3} className="mb-4">
                        <div className="bg-white p-3 rounded shadow-sm border border-secondary border-opacity-25 sticky-top" style={{ top: '80px', zIndex: 1 }}>
                            <h5 className="fw-bold d-flex align-items-center gap-2 mb-4 text-dark">
                                <Filter size={20} /> Bộ lọc {activeTab === 'document' ? 'tài liệu' : 'khóa học'}
                            </h5>

                            <Form.Group className="mb-4">
                                <Form.Label className="fw-semibold text-secondary">Môn học</Form.Label>
                                <Form.Select
                                    value={filters.subjectId}
                                    onChange={(e) => handleFilterChange('subjectId', e.target.value)}
                                    className="border-secondary shadow-none focus-ring focus-ring-info"
                                >
                                    <option value="">Tất cả môn học</option>
                                    {subjects.map(sub => (
                                        <option key={sub.id} value={sub.id}>{sub.name}</option>
                                    ))}
                                </Form.Select>
                            </Form.Group>

                            {activeTab === 'document' && (
                                <>
                                    <Form.Group className="mb-4">
                                        <Form.Label className="fw-semibold text-secondary">Chủ đề</Form.Label>
                                        <Form.Select
                                            value={filters.topicId}
                                            onChange={(e) => handleFilterChange('topicId', e.target.value)}
                                            className="border-secondary shadow-none focus-ring focus-ring-info"
                                        >
                                            <option value="">Tất cả chủ đề</option>
                                            {topics.map(topic => (
                                                <option key={topic.id} value={topic.id}>{topic.name}</option>
                                            ))}
                                        </Form.Select>
                                    </Form.Group>

                                    <Form.Group className="mb-4">
                                        <Form.Label className="fw-semibold text-secondary">Độ khó</Form.Label>
                                        <div className="d-flex flex-column gap-2 mt-2">
                                            <Form.Check
                                                type="radio" id="level-all" label="Tất cả độ khó" name="level"
                                                checked={filters.levelId === ''}
                                                onChange={() => handleFilterChange('levelId', '')}
                                            />
                                            {levels.map(lvl => (
                                                <Form.Check
                                                    key={lvl.id} type="radio" id={`level-${lvl.id}`} label={lvl.name} name="level"
                                                    checked={filters.levelId === lvl.id.toString()}
                                                    onChange={() => handleFilterChange('levelId', lvl.id.toString())}
                                                />
                                            ))}
                                        </div>
                                    </Form.Group>
                                </>
                            )}

                            <Button variant="outline-danger" className="w-100 fw-bold" onClick={() => setFilters({ kw: filters.kw, subjectId: '', levelId: '', topicId: '' })}>
                                Xóa bộ lọc
                            </Button>
                        </div>
                    </Col>

                    <Col md={9}>
                        <Nav variant="pills" className="mb-4 bg-white p-2 rounded shadow-sm border border-secondary border-opacity-25" style={{ gap: '10px' }}>
                            <Nav.Item>
                                <Nav.Link 
                                    className={`fw-bold ${activeTab === 'document' ? 'bg-primary text-white' : 'text-dark'}`}
                                    // 4. Gọi hàm handleTabChange thay vì setActiveTab
                                    onClick={() => handleTabChange('document')}
                                    style={{ cursor: 'pointer', borderRadius: '8px' }}
                                >
                                    Khám phá Tài liệu
                                </Nav.Link>
                            </Nav.Item>
                            <Nav.Item>
                                <Nav.Link 
                                    className={`fw-bold ${activeTab === 'course' ? 'bg-primary text-white' : 'text-dark'}`}
                                    // 4. Gọi hàm handleTabChange
                                    onClick={() => handleTabChange('course')}
                                    style={{ cursor: 'pointer', borderRadius: '8px' }}
                                >
                                    Khám phá Khóa học
                                </Nav.Link>
                            </Nav.Item>
                        </Nav>

                        {/* ... (Phần UI Search và Kết quả giữ nguyên code cũ) ... */}
                        <div className="bg-white p-3 rounded shadow-sm border border-secondary border-opacity-25 mb-4 d-flex gap-2">
                            <InputGroup>
                                <InputGroup.Text className="bg-white border-end-0">
                                    <Search className="text-secondary" />
                                </InputGroup.Text>
                                <Form.Control
                                    type="text"
                                    placeholder={`Nhập tên ${activeTab === 'document' ? 'tài liệu' : 'khóa học'}, từ khóa...`}
                                    value={filters.kw}
                                    onChange={(e) => handleFilterChange('kw', e.target.value)}
                                    className="border-start-0 shadow-none focus-ring focus-ring-info"
                                />
                            </InputGroup>
                        </div>

                        <div className="d-flex justify-content-between align-items-center mb-3">
                            <h5 className="fw-bold m-0">Kết quả tìm kiếm</h5>
                            <span className="text-secondary">
                                {activeTab === 'document' ? documents.length : courses.length} kết quả được tìm thấy
                            </span>
                        </div>

                        {loading ? (
                            <div className="text-center py-5">
                                <Spinner animation="border" variant="primary" />
                                <p className="text-secondary mt-2 fw-medium">Đang đồng bộ dữ liệu...</p>
                            </div>
                        ) : (activeTab === 'document' && documents.length === 0) || (activeTab === 'course' && courses.length === 0) ? (
                            <div className="text-center py-5 bg-white rounded shadow-sm border border-secondary border-opacity-25">
                                <p className="text-secondary mb-0 fw-medium">Không tìm thấy dữ liệu nào phù hợp với bộ lọc hiện tại.</p>
                            </div>
                        ) : (
                            <Row xs={1} md={2} lg={3} className="g-4">
                                {activeTab === 'document' 
                                    ? documents.map(doc => (
                                        <Col key={doc.id}>
                                            <DocumentCard doc={doc} onClick={() => nav(`/documents/${doc.id}`)} />
                                        </Col>
                                    ))
                                    : courses.map(course => (
                                        <Col key={course.id}>
                                            <CourseCard course={course} onClick={() => nav(`/courses/${course.id}`)} />
                                        </Col>
                                    ))
                                }
                            </Row>
                        )}
                    </Col>
                </Row>
            </Container>
        </Container>
    );
};

export default ExploreCourseScreen;