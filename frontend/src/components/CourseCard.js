import { Badge, Card } from "react-bootstrap";
import { CashCoin, PersonCircle, Tag } from "react-bootstrap-icons";

const CourseCard = ({ course, onClick }) => {
    return (
        <Card className="h-100 shadow-sm border-0 document-card cursor-pointer" onClick={onClick}>
            <Card.Body className="d-flex flex-column">
                <div className="d-flex justify-content-between align-items-start mb-3">
                    <Badge bg="info" className="text-dark shadow-sm d-flex align-items-center gap-1">
                        <Tag size={12} /> {course.subjectName || 'Chưa phân loại'}
                    </Badge>
                    
                    <span className="fw-bold text-danger d-flex align-items-center gap-1">
                        <CashCoin size={16} /> 
                        {course.price === 0 ? 'Miễn phí' : new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(course.price)}
                    </span>
                </div>

                <Card.Title className="fw-bold fs-5 text-dark mt-2">
                    {course.name}
                </Card.Title>
                <Card.Text className="text-secondary small mb-4 flex-grow-1" style={{ display: '-webkit-box', WebkitLineClamp: 3, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                    {course.description || 'Chưa có mô tả chi tiết cho khóa học này.'}
                </Card.Text>

                <div className="d-flex align-items-center gap-2 mt-auto pt-3 border-top">
                    <PersonCircle size={24} className="text-secondary" />
                    <div>
                        <span className="small text-muted d-block" style={{ fontSize: '0.75rem' }}>Giảng viên</span>
                        <span className="fw-semibold text-dark fs-6">{course.lecturerName || 'Đang cập nhật'}</span>
                    </div>
                </div>
            </Card.Body>
        </Card>
    );
};

export default CourseCard;