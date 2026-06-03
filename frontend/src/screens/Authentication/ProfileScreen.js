import React, { useContext, useState, useEffect, useRef } from 'react';
import { Container, Row, Col, Card, Form, Button, Spinner, Badge, Image } from 'react-bootstrap';
import { Person,  Telephone, ShieldLock, PencilSquare, XCircle, Save, Camera } from 'react-bootstrap-icons';
import { MyUserContext } from '../../configs/Contexts';
import Apis, { endpoints } from '../../configs/Apis';
import { ACTION_TYPE } from '../../configs/constants';

const ProfileScreen = () => {
  const [user, dispatch] = useContext(MyUserContext);
  
  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(false);
  
  // 1. Khai báo thêm State cho File và Preview
  const [avatarFile, setAvatarFile] = useState(null);
  const [previewAvatar, setPreviewAvatar] = useState(null);
  
  // 2. Tạo một tham chiếu (Ref) móc nối thẳng vào thẻ input HTML
  const fileInputRef = useRef(null);

  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    phone: '',
    email: '',
  });

  useEffect(() => {
    if (user) {
      setFormData({
        firstName: user.firstName || '',
        lastName: user.lastName || '',
      });
    }
  }, [user, isEditing]);

  const handleInputChange = (field, value) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  // 3. Hàm xử lý khi người dùng chọn file
  const handleAvatarChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setAvatarFile(file); // Lưu file thật để chuẩn bị gửi API
      setPreviewAvatar(URL.createObjectURL(file)); // Tạo một URL ảo để hiển thị ngay lập tức
    }
  };

  // Hàm xử lý khi ấn nút Hủy
  const handleCancelEdit = () => {
    setIsEditing(false);
    setAvatarFile(null);
    if (previewAvatar) {
      URL.revokeObjectURL(previewAvatar); // Giải phóng bộ nhớ RAM
      setPreviewAvatar(null);
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const form = new FormData();
      form.append("firstName", formData.firstName);
      form.append("lastName", formData.lastName);
      
      // Đẩy file nhị phân vào gói tin FormData
      if (avatarFile) {
        form.append("avatar", avatarFile);
      }

      const res = await Apis.patch(endpoints['profile'], form);
      
      dispatch({ type: ACTION_TYPE.LOGIN, payload: res.data });
      setIsEditing(false);
      setAvatarFile(null);
      setPreviewAvatar(null);
    } catch (error) { 
      console.error("Lỗi khi cập nhật hồ sơ:", error);
      alert("Cập nhật thất bại. Vui lòng thử lại.");
    } finally {
      setLoading(false);
    }
  };

  if (!user) return <div className="text-center mt-5"><Spinner animation="border" /></div>;

  return (
    <Container fluid className="py-5 bg-light min-vh-100">
      <Container>
        <Row className="justify-content-center">
          <Col md={4} className="mb-4">
            <Card className="border-0 shadow-sm text-center h-100">
              <Card.Body className="d-flex flex-column align-items-center justify-content-center py-5">
                
                {/* Khu vực xử lý Avatar */}
                <div className="position-relative mb-4" style={{ width: '150px', height: '150px' }}>
                  <Image 
                    // Ưu tiên hiển thị ảnh Preview, nếu không có thì lấy ảnh của User
                    src={previewAvatar || user.avatar || 'https://res.cloudinary.com/dsjxgri1t/image/upload/v1780048454/nxi0lpgryarwav9i5lej.png'} 
                    roundedCircle 
                    width={150} 
                    height={150} 
                    className={`object-fit-cover shadow border border-3 ${isEditing ? 'border-primary cursor-pointer' : 'border-info'}`}
                    onClick={() => isEditing && fileInputRef.current.click()} // Kích hoạt thẻ input ẩn
                    style={{ transition: 'all 0.3s ease', cursor: isEditing ? 'pointer' : 'default' }}
                  />
                  
                  {isEditing && (
                    <div 
                      className="position-absolute bottom-0 end-0 bg-primary text-white rounded-circle p-2 shadow"
                      style={{ cursor: 'pointer', transform: 'translate(-10px, -10px)' }}
                      onClick={() => fileInputRef.current.click()}
                    >
                      <Camera size={20} />
                    </div>
                  )}

                  {/* Thẻ input ẩn (Invisible DOM Element) */}
                  <input 
                    type="file" 
                    accept="image/*" 
                    ref={fileInputRef} 
                    style={{ display: 'none' }} 
                    onChange={handleAvatarChange}
                  />
                </div>

                <h4 className="fw-bold text-dark">{`${user.lastName || ''} ${user.firstName || ''}`.trim() || 'Người dùng ẩn danh'}</h4>
                <p className="text-secondary mb-2">{user.email}</p>
                <Badge bg={user.role === 'ADMIN' ? 'danger' : user.role === 'LECTURER' ? 'success' : 'info'} className="px-3 py-2 fs-6 shadow-sm">
                  <ShieldLock className="me-2" />
                  {user.role || 'STUDENT'}
                </Badge>
              </Card.Body>
            </Card>
          </Col>

          <Col md={8}>
            <Card className="border-0 shadow-sm h-100">
              <Card.Header className="bg-white border-bottom-0 pt-4 pb-0 px-4 d-flex justify-content-between align-items-center">
                <h5 className="fw-bold text-dark mb-0 d-flex align-items-center gap-2">
                  <Person size={24} className="text-info" /> Thông tin cá nhân
                </h5>
                {!isEditing ? (
                  <Button variant="outline-info" size="sm" className="fw-bold px-3 shadow-sm rounded-pill" onClick={() => setIsEditing(true)}>
                    <PencilSquare className="me-2" /> Chỉnh sửa
                  </Button>
                ) : (
                  <Button variant="outline-secondary" size="sm" className="fw-bold px-3 shadow-sm rounded-pill" onClick={handleCancelEdit}>
                    <XCircle className="me-2" /> Hủy
                  </Button>
                )}
              </Card.Header>
              
              <Card.Body className="p-4">
                <Form onSubmit={handleSave}>
                  <Row className="g-3">
                    <Col md={6}>
                      <Form.Group>
                        <Form.Label className="fw-semibold text-secondary small">Họ và đệm</Form.Label>
                        <Form.Control 
                          type="text" 
                          value={formData.lastName} 
                          onChange={(e) => handleInputChange('lastName', e.target.value)}
                          readOnly={!isEditing}
                          className={`shadow-none ${!isEditing ? 'bg-light border-0' : 'focus-ring focus-ring-info'}`}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group>
                        <Form.Label className="fw-semibold text-secondary small">Tên</Form.Label>
                        <Form.Control 
                          type="text" 
                          value={formData.firstName} 
                          onChange={(e) => handleInputChange('firstName', e.target.value)}
                          readOnly={!isEditing}
                          className={`shadow-none ${!isEditing ? 'bg-light border-0' : 'focus-ring focus-ring-info'}`}
                        />
                      </Form.Group>
                    </Col>

                  </Row>

                  {isEditing && (
                    <div className="mt-4 pt-3 border-top text-end">
                      <Button 
                        variant="info" 
                        type="submit" 
                        disabled={loading} 
                        className="fw-bold text-dark px-4 shadow-sm rounded-pill d-flex align-items-center gap-2 ms-auto"
                      >
                        {loading ? <Spinner as="span" animation="border" size="sm" /> : <Save />}
                        Lưu thay đổi
                      </Button>
                    </div>
                  )}
                </Form>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </Container>
  );
};

export default ProfileScreen;