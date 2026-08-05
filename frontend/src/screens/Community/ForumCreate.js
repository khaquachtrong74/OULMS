import { useState } from "react";
import { Container, Card, Form, Button, Alert, Spinner } from "react-bootstrap";
import { ArrowLeft, Send } from "react-bootstrap-icons";
import { useNavigate } from "react-router-dom";
import forumApi from "../../configs/api/forumApi";

const validatePost = (data) => {
    const errors = {};

    if (!data.title.trim()) {
        errors.title = "Tiêu đề không được để trống.";
    } else if (data.title.trim().length < 10) {
        errors.title = "Tiêu đề phải có ít nhất 10 ký tự.";
    }

    if (!data.content.trim()) {
        errors.content = "Nội dung không được để trống.";
    }

    return errors;
};

const ForumCreate = () => {
    const nav = useNavigate();

    const [formData, setFormData] = useState({
        title: "",
        content: ""
    });

    const [errors, setErrors] = useState({});
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [apiError, setApiError] = useState(null);

    const handleChange = (e) => {
        const { name, value } = e.target;

        setFormData((prev) => ({
            ...prev,
            [name]: value
        }));

        if (errors[name]) {
            setErrors((prev) => ({
                ...prev,
                [name]: null
            }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const validationErrors = validatePost(formData);

        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            return;
        }

        try {
            setIsSubmitting(true);
            setApiError(null);

            const payload = {
                title: formData.title.trim(),
                content: formData.content.trim()
            };

            await forumApi.create(payload);

            nav("/forum");
        } catch (error) {
            console.error("Lỗi khi đăng bài:", error);

            if (error.response?.status === 401 || error.response?.status === 403) {
                setApiError("Bạn cần đăng nhập để đăng bài.");
            } else {
                setApiError("Không thể đăng bài. Vui lòng thử lại sau.");
            }
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <Container className="py-5" style={{ maxWidth: "800px" }}>
            <Button
                onClick={() => nav("/forum")}
                variant="link"
                className="text-decoration-none text-dark px-0 mb-3 d-flex align-items-center gap-2"
            >
                <ArrowLeft /> Quay lại diễn đàn
            </Button>

            <Card className="border-0 shadow-sm rounded-4">
                <Card.Body className="p-4 p-md-5">
                    <h3 className="fw-bold mb-4">
                        Tạo bài thảo luận mới
                    </h3>

                    {apiError && (
                        <Alert variant="danger">
                            {apiError}
                        </Alert>
                    )}

                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-4">
                            <Form.Label className="fw-semibold">
                                Tiêu đề bài viết
                            </Form.Label>

                            <Form.Control
                                type="text"
                                name="title"
                                placeholder="Nhập tiêu đề bài viết"
                                value={formData.title}
                                onChange={handleChange}
                                isInvalid={!!errors.title}
                            />

                            <Form.Control.Feedback type="invalid">
                                {errors.title}
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-4">
                            <Form.Label className="fw-semibold">
                                Nội dung chi tiết
                            </Form.Label>

                            <Form.Control
                                as="textarea"
                                name="content"
                                rows={8}
                                placeholder="Nhập nội dung bài thảo luận..."
                                value={formData.content}
                                onChange={handleChange}
                                isInvalid={!!errors.content}
                            />

                            <Form.Control.Feedback type="invalid">
                                {errors.content}
                            </Form.Control.Feedback>
                        </Form.Group>

                        <div className="d-flex justify-content-end gap-3 mt-5">
                            <Button
                                type="button"
                                variant="light"
                                className="text-secondary fw-semibold"
                                onClick={() => nav("/forum")}
                                disabled={isSubmitting}
                            >
                                Hủy bỏ
                            </Button>

                            <Button
                                type="submit"
                                variant="primary"
                                className="fw-semibold d-flex align-items-center gap-2"
                                disabled={isSubmitting}
                            >
                                {isSubmitting ? (
                                    <>
                                        <Spinner size="sm" animation="border" />
                                        Đang đăng...
                                    </>
                                ) : (
                                    <>
                                        <Send size={18} />
                                        Đăng bài
                                    </>
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