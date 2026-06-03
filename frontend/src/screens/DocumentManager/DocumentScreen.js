import React, { useContext, useState, useEffect } from 'react';
import { Container, Row, Col, Table, Button, Form, Badge, InputGroup, Card, Spinner } from 'react-bootstrap';
import './DocumentPage.css';
import { MyUserContext } from '../../configs/Contexts';
import Apis, { endpoints } from '../../configs/Apis'; 

const DocumentScreen = () => {
  const [user] = useContext(MyUserContext);
  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [kw, setKw] = useState(''); 
  const [filterType, setFilterType] = useState('Tất cả');

  // 1. Hàm nạp dữ liệu: Gọi qua endpoint bảo mật
  const fetchDocuments = async () => {
    try {
      setLoading(true);
      const params = {};
      if (kw.trim() !== '') params.kw = kw;
      if (filterType !== 'Tất cả') params.type = filterType;

      // CHUẨN BẢO MẬT: Gọi đúng cổng '/secure/documents' qua secureDocument
      const response = await Apis.get(endpoints['document'], { params });
      
      if (response.data) {
        setDocuments(response.data);
      }
    } catch (error) {
      console.error("Lỗi khi nạp dữ liệu tài liệu bảo mật:", error);
      if (error.response?.status === 401 || error.response?.status === 403) {
        alert("Phiên đăng nhập hết hạn hoặc bạn không có quyền quản lý kho tài liệu này!");
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (user) {
      const delayDebounce = setTimeout(() => {
        fetchDocuments();
      }, 500);
      return () => clearTimeout(delayDebounce);
    }
  }, [kw, filterType, user]);

  // 2. Hàm Ẩn/Hiện: Gửi request lên cổng bảo mật
  const handleToggleStatus = async (id, currentStatus) => {
    const nextStatus = currentStatus === 'ACTIVE' ? 'HIDDEN' : 'ACTIVE';
    try {
      // Gọi qua secureDocument có ID đi kèm ở URL
      await Apis.patch(`${endpoints['secureDocument']}/${id}/`, { status: nextStatus });
      setDocuments(documents.map(doc => doc.id === id ? { ...doc, status: nextStatus } : doc));
    } catch (error) {
      console.error("Lỗi khi cập nhật trạng thái tài liệu:", error);
      alert("Không thể cập nhật trạng thái. Vui lòng thử lại!");
    }
  };

  // 3. Hàm Xóa mềm: Gửi request lên cổng bảo mật
  const handleSoftDelete = async (id) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa tài liệu này không?')) {
      try {
        // Gọi qua secureDocument có ID đi kèm ở URL
        await Apis.patch(`${endpoints['secureDocument']}/${id}/`, { status: 'DELETE' });
        setDocuments(documents.map(doc => doc.id === id ? { ...doc, status: 'DELETE' } : doc));
      } catch (error) {
        console.error("Lỗi khi xóa tài liệu:", error);
        alert("Xóa tài liệu thất bại!");
      }
    }
  };

  return (
    <Container className="py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2 className="fw-bold text-dark">Kho Tài Liệu Của Tôi</h2>
          <p className="text-secondary mb-0">Xin chào giảng viên, bạn có thể đăng tải và quản lý các tài nguyên số tại đây</p>
        </div>
        <Button variant="primary" className="fw-medium shadow-sm">
          + Tải lên tài liệu mới
        </Button>
      </div>

      <Card className="border-0 shadow-sm p-3 mb-4 bg-light">
        <Row className="g-3">
          <Col md={7}>
            <InputGroup>
              <Form.Control
                placeholder="Tìm theo tên bài học hoặc mô tả ngắn..."
                value={kw}
                onChange={(e) => setKw(e.target.value)}
              />
            </InputGroup>
          </Col>
          <Col md={5}>
            <Form.Select value={filterType} onChange={(e) => setFilterType(e.target.value)}>
              <option value="Tất cả">Tất cả định dạng</option>
              <option value="PDF">Tài liệu tập tin (PDF)</option>
              <option value="VIDEO">Video bài giảng (VIDEO)</option>
              <option value="SLIDE">Slide trình chiếu (SLIDE)</option>
              <option value="DOC">Văn bản hướng dẫn (DOC)</option>
            </Form.Select>
          </Col>
        </Row>
      </Card>

      <Card className="border-0 shadow-sm overflow-hidden">
        {loading ? (
          <div className="text-center py-5">
            <Spinner animation="border" variant="primary" className="mb-2" />
            <div className="text-secondary">Đang nạp dữ liệu từ hệ thống bảo mật...</div>
          </div>
        ) : (
          <Table hover responsive className="mb-0 align-middle">
            <thead className="bg-dark text-white">
              <tr>
                <th className="ps-3" style={{ width: '45%' }}>Tài liệu / Mô tả</th>
                <th>Định dạng</th>
                <th>Ngày cập nhật</th>
                <th>Trạng thái</th>
                <th className="text-end pe-3">Hành động</th>
              </tr>
            </thead>
            <tbody>
              {documents.length > 0 ? (
                documents.map((doc) => (
                  <tr key={doc.id} className={doc.status === 'DELETE' ? 'opacity-50' : ''}>
                    <td className="ps-3">
                      <div className="fw-bold text-dark">{doc.title}</div>
                      <small className="text-muted d-block text-truncate" style={{ maxWidth: '350px' }}>
                        {doc.description || 'Không có mô tả ngắn'}
                      </small>
                    </td>
                    <td>
                      <Badge bg="secondary" className="fw-normal">{doc.type}</Badge>
                    </td>
                    <td>
                      <small className="text-secondary">
                        {doc.updatedAt ? new Date(doc.updatedAt).toLocaleDateString('vi-VN') : doc.createdAt ? new Date(doc.createdAt).toLocaleDateString('vi-VN') : 'Mới cập nhật'}
                      </small>
                    </td>
                    <td>
                      {doc.status === 'ACTIVE' && <Badge bg="success">Đang hiển thị</Badge>}
                      {doc.status === 'HIDDEN' && <Badge bg="warning" text="dark">Đang ẩn</Badge>}
                      {doc.status === 'DELETE' && <Badge bg="danger">Đã xóa</Badge>}
                    </td>
                    <td className="text-end pe-3">
                      <div className="d-flex justify-content-end gap-2">
                        {doc.status !== 'DELETE' && (
                          <>
                            <Button 
                              variant={doc.status === 'ACTIVE' ? 'outline-warning' : 'outline-success'} 
                              size="sm"
                              onClick={() => handleToggleStatus(doc.id, doc.status)}
                            >
                              {doc.status === 'ACTIVE' ? 'Ẩn đi' : 'Hiện'}
                            </Button>
                            <Button 
                              variant="outline-danger" 
                              size="sm" 
                              onClick={() => handleSoftDelete(doc.id)}
                            >
                              Xóa
                            </Button>
                          </>
                        )}
                      </div>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" className="text-center py-4 text-muted">
                    Kho lưu trữ hiện tại đang trống hoặc không tìm thấy tài liệu phù hợp.
                  </td>
                </tr>
              )}
            </tbody>
          </Table>
        )}
      </Card>
    </Container>
  );
};

export default DocumentScreen;
