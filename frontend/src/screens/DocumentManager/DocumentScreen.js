
import React, { useContext, useState, useEffect, useCallback } from 'react';
import { Container, Row, Col, Table, Button, Form, Badge, InputGroup, Card, Spinner, Modal } from 'react-bootstrap';
import { Search, FileEarmarkPdf, PlayCircle, Files, FileEarmarkText, CloudUpload, PencilSquare, Trash } from 'react-bootstrap-icons';
import './DocumentPage.css';
import { MyUserContext } from '../../configs/Contexts';
import Apis, { endpoints } from '../../configs/Apis';
import { Pagination } from 'react-bootstrap';
const DocumentScreen = () => {
  const [user] = useContext(MyUserContext);

  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [kw, setKw] = useState('');
  const [filterLevel, setFilterLevel] = useState('');

  const [topics, setTopics] = useState([]);
  const [levels, setLevels] = useState([]);

  const [showUploadModal, setShowUploadModal] = useState(false);
  const [uploadLoading, setUploadLoading] = useState(false);
  const [newDoc, setNewDoc] = useState({
    title: '',
    description: '',
    topicId: '',
    levelId: '',
    file: null
  });

  const [showEditModal, setShowEditModal] = useState(false);
  const [editLoading, setEditLoading] = useState(false);
  const [editingDoc, setEditingDoc] = useState({
    id: '',
    title: '',
    description: '',
    topicId: '',
    levelId: '',
    file: null
  });
  const [currentPage, setCurrentPage] = useState(1);
  const ITEMS_PER_PAGE = 6;
  const currentDocuments = documents.slice(
    (currentPage - 1) * ITEMS_PER_PAGE,
    currentPage * ITEMS_PER_PAGE
  );

  const totalPages = Math.ceil(documents.length / ITEMS_PER_PAGE);
  useEffect(() => {
    const fetchMetadata = async () => {
      try {
        const [resTopic, resLevel] = await Promise.all([
          Apis.get(endpoints['topic']),
          Apis.get(endpoints['level'])
        ]);
        setTopics(resTopic.data || []);
        setLevels(resLevel.data || []);
      } catch (error) {
        console.error("Lỗi khi tải metadata:", error);
      }
    };
    fetchMetadata();
  }, []);

  const fetchMyDocuments = useCallback(async () => {
    if (!user) return;
    try {
      setLoading(true);
      const params = {};
      if (kw.trim() !== '') params.kw = kw;
      if (filterLevel !== '') params.levelId = filterLevel;

      const response = await Apis.get(endpoints['secureDocuments'], { params });
      if (response.data) {
        setDocuments(response.data);
      }
    } catch (error) {
      console.error("Lỗi khi nạp tài liệu cá nhân:", error);
    } finally {
      setLoading(false);
    }
  }, [kw, filterLevel, user]);

  useEffect(() => {
    const delayDebounce = setTimeout(() => {
      fetchMyDocuments();
    }, 400);
    return () => clearTimeout(delayDebounce);
  }, [fetchMyDocuments]);
  useEffect(() => {
    setCurrentPage(1);
  }, [kw, filterLevel]);
  const handleUploadSubmit = async (e) => {
    e.preventDefault();
    if (!newDoc.file) {
      alert("Vui lòng đính kèm tệp tin tài liệu!");
      return;
    }

    setUploadLoading(true);
    try {
      const formData = new FormData();
      formData.append('title', newDoc.title);
      formData.append('description', newDoc.description);
      formData.append('topicId', newDoc.topicId);
      formData.append('levelId', newDoc.levelId);
      formData.append('file', newDoc.file);

      await Apis.post(endpoints['createDocument'], formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });

      alert("Tải lên học liệu thành công! Đang chờ kiểm duyệt.");
      setShowUploadModal(false);
      setNewDoc({ title: '', description: '', topicId: '', levelId: '', file: null });
      fetchMyDocuments();
    } catch (error) {
      console.error("Lỗi upload tài liệu:", error);
      alert(error.response?.data || "Tải lên thất bại. Vui lòng thử lại!");
    } finally {
      setUploadLoading(false);
    }
  };

  const handleOpenEditModal = (doc) => {
    setEditingDoc({
      id: doc.id,
      title: doc.title,
      description: doc.description || '',
      topicId: doc.topicId || '',
      levelId: doc.levelId || '',
      file: null 
    });
    setShowEditModal(true);
  };

  const handleEditSubmit = async (e) => {
    e.preventDefault();
    setEditLoading(true);
    try {
      const formData = new FormData();
      formData.append('title', editingDoc.title);
      formData.append('description', editingDoc.description);
      formData.append('topicId', editingDoc.topicId);
      formData.append('levelId', editingDoc.levelId);

      if (editingDoc.file) {
        formData.append('file', editingDoc.file);
      }

      await Apis.post(endpoints['secureDocument'](editingDoc.id), formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });

      alert("Cập nhật thông tin học liệu thành công!");
      setShowEditModal(false);
      fetchMyDocuments();
    } catch (error) {
      console.error("Lỗi khi cập nhật tài liệu:", error);
      alert(error.response?.data || "Chỉnh sửa tài liệu thất bại. Vui lòng kiểm tra lại dữ liệu!");
    } finally {
      setEditLoading(false);
    }
  };

  const handleDeleteDocument = async (docId, title) => {
    const confirmed = window.confirm(`Bạn có chắc muốn xóa tài liệu "${title}" không?`);
    if (!confirmed) return;
    try {
      await Apis.delete(endpoints['secureDocument'](docId));

      alert("Xóa tài liệu thành công!");

      setDocuments(prev =>
        prev.filter(doc => doc.id !== docId)
      );

      fetchMyDocuments();
    } catch (error) {
      console.error("Lỗi khi xóa tài liệu:", error);
      alert(error.response?.data || "Không thể xóa tài liệu!");
    }
  }
  const renderFileIcon = (title = "") => {
    const lower = title.toLowerCase();
    if (lower.includes('.pdf')) return <FileEarmarkPdf size={24} className="text-danger me-2" />;
    if (lower.includes('.mp4') || lower.includes('.mkv')) return <PlayCircle size={24} className="text-primary me-2" />;
    if (lower.includes('.docx') || lower.includes('.doc')) return <FileEarmarkText size={24} className="text-info me-2" />;
    return <Files size={24} className="text-secondary me-2" />;
  };
  

  return (
    <Container className="py-5">
      {/* HEADER SECTION */}
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-5">
        <div>
          <h2 className="fw-extrabold text-dark m-0 d-flex align-items-center gap-2">
            Kho Học Liệu Cá Nhân
          </h2>
          <p className="text-muted mb-0 mt-1">Quản lý, chỉnh sửa nội dung và theo dõi trạng thái phê duyệt các tài nguyên số của bạn.</p>
        </div>
        <Button
          variant="primary"
          className="fw-bold px-4 py-2 rounded-pill shadow-sm d-flex align-items-center gap-2 align-self-start"
          onClick={() => setShowUploadModal(true)}
        >
          <CloudUpload size={20} /> Tải lên tài liệu mới
        </Button>
      </div>

      {/* SEARCH & FILTERS CONTAINER */}
      <Card className="border-0 shadow-sm p-4 mb-4 rounded-4 bg-white">
        <Row className="g-3">
          <Col md={8}>
            <InputGroup className="bg-light rounded-3 overflow-hidden border border-1">
              <InputGroup.Text className="bg-light border-0 ps-3">
                <Search className="text-muted" />
              </InputGroup.Text>
              <Form.Control
                className="bg-light border-0 py-2 shadow-none"
                placeholder="Tìm kiếm nhanh tiêu đề hoặc từ khóa tài liệu học phần..."
                value={kw}
                onChange={(e) => setKw(e.target.value)}
              />
            </InputGroup>
          </Col>
          <Col md={4}>
            <Form.Select
              className="py-2 border-1 rounded-3 shadow-none focus-ring focus-ring-primary"
              value={filterLevel}
              onChange={(e) => setFilterLevel(e.target.value)}
            >
              <option value="">Tất cả cấp độ</option>
              {levels.map(lvl => (
                <option key={lvl.id} value={lvl.id}>{lvl.name}</option>
              ))}
            </Form.Select>
          </Col>
        </Row>
      </Card>

      {/* DOCUMENT LIST TABLE */}
      <Card className="border-0 shadow-sm rounded-4 overflow-hidden bg-white">
        {loading ? (
          <div className="text-center py-5">
            <Spinner animation="border" variant="primary" className="mb-3" />
            <div className="text-secondary fw-medium">Đang đồng bộ kho dữ liệu tài liệu...</div>
          </div>
        ) : (
          <Table hover responsive className="mb-0 align-middle">
            <thead className="table-dark-custom">
              <tr>
                <th className="ps-4 py-3" style={{ width: '45%' }}>Tài liệu học tập</th>
                <th className="py-3">Chủ đề</th>
                <th className="py-3">Cấp độ</th>
                <th className="py-3">Trạng thái</th>
                <th className="text-end pe-4 py-3">Hành động</th>
              </tr>
            </thead>
            <tbody>
              {currentDocuments.length > 0 ? (
                currentDocuments.map((doc) => (
                  <tr key={doc.id}>
                    <td className="ps-4 py-3">
                      <div className="d-flex align-items-center">
                        {renderFileIcon(doc.title)}
                        <div>
                          <div className="fw-bold text-dark text-hover-primary cursor-pointer">{doc.title}</div>
                          <span className="text-muted small d-block text-truncate mt-1" style={{ maxWidth: '380px' }}>
                            {doc.description || 'Chưa cấu hình mô tả ngắn.'}
                          </span>
                        </div>
                      </div>
                    </td>
                    <td>
                      <Badge bg="network-light" text="dark" className="border px-2 py-1.5 fw-medium text-secondary bg-light">
                        {doc.topicName || 'Chưa phân loại'}
                      </Badge>
                    </td>
                    <td>
                      <span className="small fw-semibold text-dark">{doc.levelName || 'Cơ bản'}</span>
                    </td>
                    <td>
                      {doc.status === 'ACTIVE' && <Badge bg="success" className="px-2.5 py-1.5 rounded-2">Đang hiển thị</Badge>}
                      {doc.status === 'HIDDEN' && <Badge bg="secondary" className="px-2.5 py-1.5 rounded-2">Đang ẩn</Badge>}
                      {doc.status === 'PENDING' && <Badge bg="warning" text="dark" className="px-2.5 py-1.5 rounded-2">Chờ duyệt</Badge>}
                      {doc.status === 'REJECTED' && <Badge bg="danger" className="px-2.5 py-1.5 rounded-2">Bị từ chối</Badge>}
                    </td>
                    <td className="text-end pe-4 py-3">
                      <div className="d-flex justify-content-end gap-2">
                        <Button
                          variant="outline-primary"
                          size="sm"
                          className="rounded-3 px-3 d-inline-flex align-items-center gap-1"
                          onClick={() => handleOpenEditModal(doc)}
                        >
                          <PencilSquare size={14} />
                          <span>Chỉnh sửa</span>
                        </Button>

                        <Button
                          variant="outline-danger"
                          size="sm"
                          className="rounded-3 px-3 d-inline-flex align-items-center gap-1"
                          onClick={() => handleDeleteDocument(doc.id, doc.title)}
                        >
                          <Trash size={14} />
                          <span>Xóa</span>
                        </Button>
                      </div>
                    </td>

                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" className="text-center py-5 text-muted fw-medium">
                    Bạn chưa đóng góp tài liệu nào hoặc bộ lọc không khớp dữ liệu.
                  </td>
                </tr>
              )}
            </tbody>
          </Table>

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

      </Card>

      {/* ================= MODAL 1: TẢI LÊN TÀI LIỆU MỚI ================= */}
      <Modal show={showUploadModal} onHide={() => !uploadLoading && setShowUploadModal(false)} centered size="lg">
        <Form onSubmit={handleUploadSubmit}>
          <Modal.Header closeButton={!uploadLoading} className="border-0 pb-0 pt-4 px-4">
            <Modal.Title className="fw-bold text-dark">Tải Lên Học Liệu Số Mới</Modal.Title>
          </Modal.Header>
          <Modal.Body className="p-4">
            <Row className="g-3">
              <Col md={12}>
                <Form.Group>
                  <Form.Label className="fw-semibold text-secondary small">Tiêu đề học phần / Bài học</Form.Label>
                  <Form.Control
                    required
                    placeholder="Ví dụ: Slide bài giảng Chương 1 - Cấu trúc dữ liệu giải thuật.pdf"
                    value={newDoc.title}
                    onChange={(e) => setNewDoc(prev => ({ ...prev, title: e.target.value }))}
                  />
                </Form.Group>
              </Col>

              <Col md={6}>
                <Form.Group>
                  <Form.Label className="fw-semibold text-secondary small">Thuộc chủ đề</Form.Label>
                  <Form.Select
                    required
                    value={newDoc.topicId}
                    onChange={(e) => setNewDoc(prev => ({ ...prev, topicId: e.target.value }))}
                  >
                    <option value="">-- Chọn chủ đề bài học --</option>
                    {topics.map(t => (
                      <option key={t.id} value={t.id}>{t.name}</option>
                    ))}
                  </Form.Select>
                </Form.Group>
              </Col>

              <Col md={6}>
                <Form.Group>
                  <Form.Label className="fw-semibold text-secondary small">Cấp độ học thuật</Form.Label>
                  <Form.Select
                    required
                    value={newDoc.levelId}
                    onChange={(e) => setNewDoc(prev => ({ ...prev, levelId: e.target.value }))}
                  >
                    <option value="">-- Chọn cấp độ học thuật --</option>
                    {levels.map(l => (
                      <option key={l.id} value={l.id}>{l.name}</option>
                    ))}
                  </Form.Select>
                </Form.Group>
              </Col>

              <Col md={12}>
                <Form.Group>
                  <Form.Label className="fw-semibold text-secondary small">Mô tả tóm tắt nội dung chính</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    required
                    placeholder="Mô tả ngắn gọn nội dung cốt lõi của tài liệu..."
                    value={newDoc.description}
                    onChange={(e) => setNewDoc(prev => ({ ...prev, description: e.target.value }))}
                  />
                </Form.Group>
              </Col>

              <Col md={12}>
                <Form.Group>
                  <Form.Label className="fw-semibold text-secondary small">Đính kèm tệp tin tài liệu</Form.Label>
                  <Form.Control
                    type="file"
                    required
                    accept="application/pdf,video/mp4,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                    onChange={(e) => setNewDoc(prev => ({ ...prev, file: e.target.files[0] }))}
                  />
                </Form.Group>
              </Col>
            </Row>
          </Modal.Body>
          <Modal.Footer className="border-0 pt-0 pb-4 px-4">
            <Button variant="outline-secondary" className="px-4 rounded-pill" onClick={() => setShowUploadModal(false)} disabled={uploadLoading}>
              Hủy bỏ
            </Button>
            <Button variant="primary" type="submit" className="px-4 rounded-pill" disabled={uploadLoading}>
              {uploadLoading ? <Spinner animation="border" size="sm" /> : "Xác nhận tải lên"}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>

      {/* ================= MODAL 2: CẬP NHẬT THÔNG TIN (EDIT MODAL) ================= */}
      <Modal show={showEditModal} onHide={() => !editLoading && setShowEditModal(false)} centered size="lg">
        <Form onSubmit={handleEditSubmit}>
          <Modal.Header closeButton={!editLoading} className="border-0 pb-0 pt-4 px-4">
            <Modal.Title className="fw-bold text-dark">Chỉnh Sửa Thông Tin Học Liệu</Modal.Title>
          </Modal.Header>
          <Modal.Body className="p-4">
            <Row className="g-3">
              <Col md={12}>
                <Form.Group>
                  <Form.Label className="fw-semibold text-secondary small">Tiêu đề học phần</Form.Label>
                  <Form.Control
                    required
                    placeholder="Nhập tiêu đề cập nhật..."
                    value={editingDoc.title}
                    onChange={(e) => setEditingDoc(prev => ({ ...prev, title: e.target.value }))}
                  />
                </Form.Group>
              </Col>

              <Col md={6}>
                <Form.Group>
                  <Form.Label className="fw-semibold text-secondary small">Chủ đề bài giảng</Form.Label>
                  <Form.Select
                    required
                    value={editingDoc.topicId}
                    onChange={(e) => setEditingDoc(prev => ({ ...prev, topicId: e.target.value }))}
                  >
                    <option value="">-- Chọn chủ đề bài học --</option>
                    {topics.map(t => (
                      <option key={t.id} value={t.id}>{t.name}</option>
                    ))}
                  </Form.Select>
                </Form.Group>
              </Col>

              <Col md={6}>
                <Form.Group>
                  <Form.Label className="fw-semibold text-secondary small">Cấp độ học thuật</Form.Label>
                  <Form.Select
                    required
                    value={editingDoc.levelId}
                    onChange={(e) => setEditingDoc(prev => ({ ...prev, levelId: e.target.value }))}
                  >
                    <option value="">-- Chọn cấp độ học thuật --</option>
                    {levels.map(l => (
                      <option key={l.id} value={l.id}>{l.name}</option>
                    ))}
                  </Form.Select>
                </Form.Group>
              </Col>

              <Col md={12}>
                <Form.Group>
                  <Form.Label className="fw-semibold text-secondary small">Mô tả tóm tắt</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    required
                    placeholder="Cập nhật mô tả nội dung..."
                    value={editingDoc.description}
                    onChange={(e) => setEditingDoc(prev => ({ ...prev, description: e.target.value }))}
                  />
                </Form.Group>
              </Col>

              <Col md={12}>
                <Form.Group>
                  <Form.Label className="fw-semibold text-secondary small">Thay đổi tệp tin mới (Không bắt buộc)</Form.Label>
                  <Form.Control
                    type="file"
                    accept="application/pdf,video/mp4,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                    onChange={(e) => setEditingDoc(prev => ({ ...prev, file: e.target.files[0] }))}
                  />
                  <Form.Text className="text-muted block mt-1">
                    Chỉ chọn tệp nếu bạn muốn tải đè và thay thế tệp tin hiện tại trên hệ thống lưu trữ Cloud.
                  </Form.Text>
                </Form.Group>
              </Col>
            </Row>
          </Modal.Body>
          <Modal.Footer className="border-0 pt-0 pb-4 px-4">
            <Button variant="outline-secondary" className="px-4 rounded-pill" onClick={() => setShowEditModal(false)} disabled={editLoading}>
              Hủy bỏ
            </Button>
            <Button variant="primary" type="submit" className="px-4 rounded-pill" disabled={editLoading}>
              {editLoading ? <Spinner animation="border" size="sm" /> : "Lưu thay đổi"}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </Container>
  );
};

export default DocumentScreen;

