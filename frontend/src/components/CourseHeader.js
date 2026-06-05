import React from 'react';
import { Button, Badge } from 'react-bootstrap';
import { ArrowLeft, Tag, Clock } from 'react-bootstrap-icons';

const CourseHeader = ({ course, onBack, backText = "Quay lại", children }) => {
    if (!course) return null;

    return (
        <div className="mb-5 pe-lg-4">
            <Button 
                variant="link" 
                className="text-secondary text-decoration-none px-0 mb-4 d-inline-flex align-items-center gap-2"
                onClick={onBack}
            >
                <ArrowLeft size={18} /> {backText}
            </Button>

            <div className="mb-3">
                <Badge 
                    bg="info" 
                    className="text-dark px-3 py-2 rounded-pill fw-medium d-inline-flex align-items-center gap-2"
                >
                    <Tag size={14} /> {course.subjectName || 'Chưa phân loại'}
                </Badge>
            </div>

            <h1 
                className="fw-bold mb-4 text-dark" 
                style={{ fontSize: '2.5rem', letterSpacing: '-0.5px' }}
            >
                {course.name || course.title}
            </h1>

            <div className="d-flex flex-wrap gap-4 pb-4 border-bottom text-secondary small fw-medium">
 
                {children}

                <div className="d-flex align-items-center gap-2">
                    <Clock size={18} />
                    <span>Cập nhật gần nhất: {new Date(course.updatedAt).toLocaleDateString('vi-VN')}</span>
                </div>
            </div>
        </div>
    );
};

export default CourseHeader;