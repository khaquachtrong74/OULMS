import React, { useState, useEffect, useCallback } from 'react';
import { Container, Row, Col, Form, InputGroup, Button, Card, Spinner, Badge } from 'react-bootstrap';
import { Search, Filter, Book } from 'react-bootstrap-icons';
import Apis, { endpoints } from '../../configs/Apis'; // Import config API của bạn
import { useNavigate } from 'react-router-dom';

const ExploreScreen = () => {
  const nav = useNavigate();
  
  // 1. Lớp Trạng thái (State)
  const [loading, setLoading] = useState(false);
  const [documents, setDocuments] = useState([]);
  
  // Trạng thái cho bộ lọc
  const [filters, setFilters] = useState({
    kw: '',
    subjectId: '',
    levelId: ''
  });

  // Dữ liệu Sidebar
  const [subjects, setSubjects] = useState([]);
  const [levels, setLevels] = useState([]);

  // 2. Tải Metadata (Môn học, Level) ngay khi vào trang
  useEffect(() => {
    const fetchMetadata = async () => {
      try {
        // Chạy song song 2 request thay vì đợi request này xong mới chạy request kia
        const [resSub, resLev] = await Promise.all([
          Apis.get(endpoints['subject']),
          Apis.get(endpoints['level'])
        ]);
        
        setSubjects(resSub.data);
        setLevels(resLev.data);
      } catch (error) {
        console.error("Lỗi khi tải danh sách môn học hoặc độ khó:", error);
      }
    };
    
    fetchMetadata();
  }, []); // [] Đảm bảo chỉ chạy 1 lần khi mount component

  // 3. Tải danh sách tài liệu dựa trên filters
  const loadDocuments = useCallback(async () => {
    setLoading(true);
    try {
      const queryParams = new URLSearchParams();
      if (filters.kw) queryParams.append('kw', filters.kw);
      if (filters.subjectId) queryParams.append('subjectId', filters.subjectId);
      if (filters.levelId) queryParams.append('levelId', filters.levelId);

      // Gọi endpoint '/documents'
      const url = `${endpoints['document']}?${queryParams.toString()}`;
      const res = await Apis.get(url);
      setDocuments(res.data);
    } catch (error) {
      console.error("Lỗi khi tải tài liệu:", error);
    } finally {
      setLoading(false);
    }
  }, [filters]);

  useEffect(() => {
    loadDocuments();
  }, [loadDocuments]);

  // Xử lý sự kiện thay đổi
  const handleFilterChange = (type, value) => {
    setFilters(prev => ({ ...prev, [type]: value }));
  };

  return (
    <Container fluid className="py-4 bg-light min-vh-100">
      <Container>
        <Row>
          {/* CỘT TRÁI: Bộ lọc (Sidebar) */}
          <Col md={3} className="mb-4">
            <div className="bg-white p-3 rounded shadow-sm border border-secondary border-opacity-25 sticky-top" style={{ top: '80px' }}>
              <h5 className="fw-bold d-flex align-items-center gap-2 mb-4 text-dark">
                <Filter size={20} /> Bộ lọc tài liệu
              </h5>

              {/* Lọc theo môn học (Sử dụng state subjects) */}
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

              {/* Lọc theo Level (Sử dụng state levels vừa fetch được) */}
              <Form.Group className="mb-4">
                <Form.Label className="fw-semibold text-secondary">Độ khó</Form.Label>
                <div className="d-flex flex-column gap-2 mt-2">
                  <Form.Check 
                    type="radio" 
                    id="level-all"
                    label="Tất cả độ khó" 
                    name="level"
                    checked={filters.levelId === ''}
                    onChange={() => handleFilterChange('levelId', '')}
                    className="custom-radio"
                  />
                  {levels.map(lvl => (
                    <Form.Check 
                      key={lvl.id} 
                      type="radio" 
                      id={`level-${lvl.id}`}
                      label={lvl.name} // Backend của bạn cần trả về property "name" cho level
                      name="level"
                      checked={filters.levelId === lvl.id.toString()}
                      onChange={() => handleFilterChange('levelId', lvl.id.toString())}
                      className="custom-radio"
                    />
                  ))}
                </div>
              </Form.Group>

              <Button variant="outline-danger" className="w-100 fw-bold" onClick={() => setFilters({ kw: '', subjectId: '', levelId: '' })}>
                Xóa bộ lọc
              </Button>
            </div>
          </Col>

          {/* CỘT PHẢI: Thanh tìm kiếm & Kết quả */}
          <Col md={9}>
            {/* Thanh tìm kiếm */}
            <div className="bg-white p-3 rounded shadow-sm border border-secondary border-opacity-25 mb-4 d-flex gap-2">
              <InputGroup>
                <InputGroup.Text className="bg-white border-end-0">
                  <Search className="text-secondary" />
                </InputGroup.Text>
                <Form.Control 
                  type="text" 
                  placeholder="Nhập tên tài liệu, từ khóa..." 
                  value={filters.kw}
                  onChange={(e) => handleFilterChange('kw', e.target.value)}
                  className="border-start-0 shadow-none focus-ring focus-ring-info"
                />
              </InputGroup>
            </div>

            {/* Vùng hiển thị kết quả */}
            <div className="d-flex justify-content-between align-items-center mb-3">
              <h5 className="fw-bold m-0">Kết quả tìm kiếm</h5>
              <span className="text-secondary">{documents.length} tài liệu được tìm thấy</span>
            </div>

            {loading ? (
              <div className="text-center py-5">
                <Spinner animation="border" variant="info" />
                <p className="text-secondary mt-2 fw-medium">Đang tìm kiếm tài liệu...</p>
              </div>
            ) : documents.length === 0 ? (
              <div className="text-center py-5 bg-white rounded shadow-sm border border-secondary border-opacity-25">
                <p className="text-secondary mb-0 fw-medium">Không tìm thấy tài liệu nào phù hợp với bộ lọc hiện tại.</p>
              </div>
            ) : (
              <Row xs={1} md={2} lg={3} className="g-4">
                {documents.map(doc => (
                  <Col key={doc.id}>
                    <Card className="h-100 shadow-sm border-0 document-card cursor-pointer" onClick={() => nav(`/documents/${doc.id}`)}>
                      <div className="bg-light border-bottom" style={{ height: '140px', position: 'relative' }}>
                         <Badge bg={doc.level?.name === 'Nâng cao' ? 'danger' : 'info'} className="position-absolute top-0 start-0 m-2 text-dark shadow-sm">
                           {doc.level?.name || 'Cơ bản'}
                         </Badge>
                      </div>
                      <Card.Body className="d-flex flex-column">
                        <Card.Title className="fw-bold fs-6 text-dark">{doc.title}</Card.Title>
                        <Card.Text className="text-secondary small mb-3 flex-grow-1" style={{ display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                          {doc.description || 'Chưa có mô tả cho tài liệu này.'}
                        </Card.Text>
                        
                        <div className="d-flex justify-content-between align-items-center mt-auto pt-3 border-top">
                          <span className="text-primary small fw-semibold d-flex align-items-center gap-1">
                            <Book size={14} /> {doc.subject?.name || 'Chưa phân loại'}
                          </span>
                          <span className="text-success fw-bold fs-6">
                            {doc.price === 0 || !doc.price ? 'Miễn phí' : `${doc.price.toLocaleString('vi-VN')} đ`}
                          </span>
                        </div>
                      </Card.Body>
                    </Card>
                  </Col>
                ))}
              </Row>
            )}
          </Col>
        </Row>
      </Container>
    </Container>
  );
};

export default ExploreScreen;