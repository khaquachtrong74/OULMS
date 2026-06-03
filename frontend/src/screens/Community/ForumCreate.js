import React, { useEffect, useState } from 'react';
import { Container, Card, Form, Button, Alert, Spinner } from 'react-bootstrap';
import { ArrowLeft, Send } from 'react-bootstrap-icons';
import Apis, { endpoints } from '../../configs/Apis';
import { useNavigate } from 'react-router-dom';
// import { useNavigate } from 'react-router-dom'; // Dùng để chuyển trang sau khi tạo xong

// [SRP] Tách riêng logic kiểm tra tính hợp lệ của dữ liệu
// Hàm này nhận vào object dữ liệu và trả về object chứa các lỗi tương ứng
const validatePost = (data) => {
    const errors = {};
    if (!data.title || data.title.trim() === '') {
        errors.title = 'Tiêu đề không được để trống.';
    } else if (data.title.length < 10) {
        errors.title = 'Tiêu đề phải có ít nhất 10 ký tự để rõ nghĩa.';
    }

    if (!data.content || data.content.trim() === '') {
        errors.content = 'Nội dung không được để trống.';
    }

    if (!data.tag || data.tag === '') {
        errors.tag = 'Vui lòng chọn một chủ đề.';
    }
    return errors;
};

const ForumCreate = () => {

    const [formData, setFormData] = useState({
        title: '',
        content: '',
        tag: ''
    });
    const [tags, setTags] = useState([]);
    const [loadingTags, setLoadingTags] = useState(true); // Trạng thái đang tải Tag
    const [fetchTagError, setFetchTagError] = useState(null);
    const [errors, setErrors] = useState({});
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [apiError, setApiError] = useState(null);

    const nav = useNavigate();
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));

        if (errors[name]) {
            setErrors(prev => ({ ...prev, [name]: null }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault(); // Ngăn trình duyệt tự động reload trang khi submit form

        // 1. Chạy hàm kiểm tra dữ liệu
        const validationErrors = validatePost(formData);
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            return; // Dừng lại, không gọi API nếu có lỗi
        }

        // 2. Nếu hợp lệ, bắt đầu quá trình gửi
        setIsSubmitting(true);
        setApiError(null);

        try {
            // [Mô phỏng] Chạy test: Đóng gói dữ liệu thành JSON và gửi
            const payload = {
                title: formData.title,
                content: formData.content,
                tags: [formData.tag],
                // authorId sẽ được backend lấy từ Token bảo mật (JWT) chứ không gửi từ client
            };

            console.log("Dữ liệu chuẩn bị gửi đi:", JSON.stringify(payload));

            // Giả lập API delay 1.5 giây
            await new Promise(resolve => setTimeout(resolve, 1500));

            // Nếu gọi API (axios.post) thành công:
            alert("Đăng bài thành công!");
            // navigate('/forums'); // Quay về trang danh sách

        } catch (error) {
            console.error("Lỗi khi đăng bài:", error);
            setApiError("Hệ thống đang bận hoặc có lỗi xảy ra. Vui lòng thử lại sau.");
        } finally {
            setIsSubmitting(false); // Dù thành công hay thất bại cũng phải tắt biểu tượng loading
        }
    };

    useEffect(() => {
        const fetchTags = async () => {
            try {
                const result = await Apis.get(endpoints['tag']);

                if (result.status === 200) {
                    setTags(result.data);
                    setFetchTagError(null);
                }
            } catch (error) {
                console.error(error);
                setFetchTagError(error);
            }
        };

        fetchTags();
    }, []);
    return (
        <Container className="py-5" style={{ maxWidth: '800px' }}>
            <Button
            onClick={()=>{
                nav('/forum')
            }}
             variant="link"
              className="text-decoration-none text-dark px-0 mb-3 d-flex align-items-center gap-2">
                <ArrowLeft /> Quay lại diễn đàn
            </Button>

            <Card className="border-0 shadow-sm rounded-4">
                <Card.Body className="p-4 p-md-5">
                    <h3 className="fw-bold mb-4">Tạo bài thảo luận mới</h3>

                    {apiError && <Alert variant="danger">{apiError}</Alert>}

                    <Form onSubmit={handleSubmit}>
                        {/* Nhập tiêu đề */}
                        <Form.Group className="mb-4">
                            <Form.Label className="fw-semibold">Tiêu đề bài viết</Form.Label>
                            <Form.Control
                                type="text"
                                name="title"
                                placeholder="Tiêu đề"
                                value={formData.title}
                                onChange={handleChange}
                                isInvalid={!!errors.title}
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.title}
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-4">
                            <Form.Label className="fw-semibold">Chủ đề </Form.Label>
                            <Form.Select
                                name="tag"
                                value={formData.tag}
                                onChange={handleChange}
                                isInvalid={!!errors.tag}
                            >
                                <option value="">-- Chọn một chủ đề --</option>

                                {tags && tags.map(tag => {
                                    return (
                                        <option key={tag.id} value={tag.name}>
                                            {tag.name}
                                        </option>
                                    );
                                })}
                            </Form.Select>
                            <Form.Control.Feedback type="invalid">
                                {errors.tag}
                            </Form.Control.Feedback>
                        </Form.Group>

                        {/* Nhập nội dung */}
                        <Form.Group className="mb-4">
                            <Form.Label className="fw-semibold">Nội dung chi tiết</Form.Label>
                            <Form.Control
                                as="textarea"
                                name="content"
                                rows={8}
                                placeholder="Mô tả chi tiết vấn đề bạn đang gặp phải, có thể kèm theo đoạn code..."
                                value={formData.content}
                                onChange={handleChange}
                                isInvalid={!!errors.content}
                            />
                            <Form.Control.Feedback type="invalid">
                                {errors.content}
                            </Form.Control.Feedback>
                        </Form.Group>

                        {/* Nút submit */}
                        <div className="d-flex justify-content-end gap-3 mt-5">
                            <Button variant="light" className="text-secondary fw-semibold">
                                Hủy bỏ
                            </Button>
                            <Button
                                type="submit"
                                variant="primary"
                                className="fw-semibold d-flex align-items-center gap-2"
                                disabled={isSubmitting}
                            >
                                {isSubmitting ? (
                                    <><Spinner size="sm" animation="border" /> Đang đăng...</>
                                ) : (
                                    <><Send size={18} /> Đăng bài</>
                                )}
                            </Button>
                        </div>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default ForumCreate;