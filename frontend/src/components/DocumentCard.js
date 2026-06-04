import { Badge, Card } from "react-bootstrap";
import { Book } from "react-bootstrap-icons";

const DocumentCard = ({ doc, onClick }) => (
    <Card className="h-100 shadow-sm border-0 document-card cursor-pointer" onClick={onClick}>
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
                <span className=" fw-bold fs-6">{!doc.topicName ? 'Chưa phân chủ đề' : doc.topicName}</span>
                <span className="text-primary small fw-semibold d-flex align-items-center gap-1">
                    <Book size={14} /> {doc.levelName || 'Chưa phân cấp'}
                </span>
            </div>
        </Card.Body>
    </Card>
);

export default DocumentCard;