import { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import {
  Container,
  Row,
  Col,
  Button,
  Form,
  Badge,
  Card,
  Spinner,
  Alert,
} from "react-bootstrap";

import moment from "moment";
import "moment/locale/vi";
import { MyUserContext } from "../../configs/Contexts";
import { ArrowLeft } from "react-bootstrap-icons";
import documentApi from "../../configs/api/documentApi";

const DocumentDetailScreen = () => {
  const { documentId } = useParams();
  const [searchParams] = useSearchParams();

  const page = searchParams.get("page") || 1;

  const nav = useNavigate();

  const [user] = useContext(MyUserContext);

  const [doc, setDoc] = useState(null);
  const [comments, setComments] = useState([]);
  const [comment, setComment] = useState("");

  const [loading, setLoading] = useState(true);
  const [loadingComments, setLoadingComments] = useState(false);
  const [commentsLoaded, setCommentsLoaded] = useState(false);

  const getFileId = (url) => {
    if (!url) return null;
    const match = url.match(/\/d\/([^/]+)/);
    return match ? match[1] : null;
  };

  useEffect(() => {
    const loadDocument = async () => {
      try {
        setLoading(true);

        const res = await documentApi.getDetail(documentId);

        setDoc(res.data);
      } catch (err) {
        console.error("Lỗi load document:", err);
      } finally {
        setLoading(false);
      }
    };
    loadDocument();
  }, [documentId]);

  const loadComments = useCallback(async () => {
    if (commentsLoaded || loadingComments) return;

    try {
      setLoadingComments(true);

      const res = await documentApi.getComments(documentId);

      console.log("load");
      setComments(res.data);
      setCommentsLoaded(true);
    } catch (err) {
      console.error("Lỗi load comments:", err);
    } finally {
      setLoadingComments(false);
    }
  }, [documentId, commentsLoaded, loadingComments]);

  useEffect(() => {
    const handleScroll = () => {
      if (commentsLoaded || loadingComments) return;

      const commentSection = window.document.getElementById("comments-section");

      if (!commentSection) return;

      const rect = commentSection.getBoundingClientRect();

      if (rect.top <= window.innerHeight + 100) {
        loadComments();
      }
    };

    window.addEventListener("scroll", handleScroll);

    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, [loadComments, commentsLoaded, loadingComments]);

  const deleteComent = async (commentId) => {
    const confirmDelete = window.confirm(
      "Bạn có chắc chắn muốn xoá comment này?",
    );

    if (!confirmDelete) return;

    try {
      await documentApi.deleteComment(commentId);

      setComments((prev) => prev.filter((p) => p.id !== commentId));
    } catch (err) {
      console.error("Lỗi xoá forum:", err);

      alert(err.response?.data || "Không thể xoá bình luận.");
    }
  };

  const goToLogin = () => {
    nav(`/login?next=/documents/${documentId}`);
  };

  const addComment = async (e) => {
    e.preventDefault();

    if (!user) {
      goToLogin();
      return;
    }

    if (!comment.trim()) return;

    try {
      const res = await documentApi.addComment(documentId, {
        content: comment,
      });

      setComments((prev) => [res.data, ...prev]);
      setComment("");
      setCommentsLoaded(true);
    } catch (err) {
      console.error("Lỗi gửi comment:", err);
    }
  };

  if (loading) {
    return (
      <Container className="py-5 text-center">
        <Spinner animation="border" variant="primary" />
        <p className="text-muted mt-3">Đang tải tài liệu...</p>
      </Container>
    );
  }

  if (!doc) {
    return (
      <Container className="py-5">
        <Alert variant="warning">Không tìm thấy tài liệu.</Alert>
      </Container>
    );
  }

  const fileId = getFileId(doc.fileUrl);

  return (
    <div className="bg-light min-vh-100 py-4">
      <Container>
        <Card className="border-0 shadow-sm rounded-4 mb-4">
          <Card.Body className="p-4 p-md-5">
            <Button
              onClick={() => {
                nav(`/explore?tab=documents&page=${page}`);
              }}
              variant="link"
              className="text-decoration-none text-dark px-0 mb-3 d-flex align-items-center gap-2"
            >
              <ArrowLeft /> Quay lại
            </Button>
            <div className="d-flex justify-content-between align-items-start flex-wrap gap-3 mb-4">
              <div>
                <Badge bg="primary" className="px-3 py-2 rounded-pill mb-3">
                  {doc.topicName}
                </Badge>

                <h1 className="fw-bold mb-0">{doc.title}</h1>
              </div>
            </div>

            {fileId ? (
              <div className="rounded-4 overflow-hidden border bg-white mb-5">
                <div className="d-flex justify-content-between align-items-center px-3 py-2 border-bottom">
                  <div className="fw-semibold">Xem trước tài liệu</div>

                  <Button
                    as="a"
                    href={doc.fileUrl}
                    target="_blank"
                    rel="noreferrer"
                    variant="outline-primary"
                    size="sm"
                    className="rounded-pill"
                  >
                    Mở trên Drive
                  </Button>
                </div>

                <iframe
                  src={`https://drive.google.com/file/d/${fileId}/preview`}
                  title="Google Drive Preview"
                  width="100%"
                  height="760"
                  allow="autoplay"
                  style={{ border: 0 }}
                />
              </div>
            ) : (
              <Alert variant="danger">Không thể hiển thị tài liệu.</Alert>
            )}

            <p className="text-muted fs-5 mb-4">{doc.description}</p>

            <Row className="g-3">
              <Col md={6}>
                <Card className="border rounded-4 h-100">
                  <Card.Body>
                    <div className="text-muted small mb-1">Chủ đề</div>

                    <div className="fw-bold">{doc.topicName}</div>
                  </Card.Body>
                </Card>
              </Col>

              <Col md={6}>
                <Card className="border rounded-4 h-100">
                  <Card.Body>
                    <div className="text-muted small mb-1">Cấp độ</div>

                    <div className="fw-bold text-capitalize">
                      {doc.levelName}
                    </div>
                  </Card.Body>
                </Card>
              </Col>
            </Row>
          </Card.Body>
        </Card>

        <Card id="comments-section" className="border-0 shadow-sm rounded-4">
          <Card.Body className="p-4 p-md-5">
            <div className="d-flex justify-content-between align-items-center mb-4">
              <div>
                <h3 className="fw-bold mb-1">Bình luận</h3>

                <p className="text-muted mb-0">
                  Trao đổi về nội dung tài liệu này
                </p>
              </div>

              <Badge bg="primary" pill className="px-3 py-2">
                {comments.length}
              </Badge>
            </div>

            {!user ? (
              <Alert
                variant="info"
                className="d-flex justify-content-between align-items-center flex-wrap gap-3"
              >
                <div>Bạn cần đăng nhập để bình luận.</div>

                <Button
                  variant="primary"
                  className="rounded-pill px-4"
                  onClick={goToLogin}
                >
                  Đăng nhập
                </Button>
              </Alert>
            ) : (
              <Form onSubmit={addComment} className="mb-4">
                <Form.Control
                  as="textarea"
                  rows={4}
                  className="rounded-4 mb-3"
                  placeholder="Viết bình luận của bạn..."
                  value={comment}
                  onChange={(e) => setComment(e.target.value)}
                />

                <div className="text-end">
                  <Button
                    type="submit"
                    variant="primary"
                    className="px-4 rounded-pill"
                    disabled={!comment.trim()}
                  >
                    Gửi bình luận
                  </Button>
                </div>
              </Form>
            )}

            {!commentsLoaded && !loadingComments && (
              <div className="text-center py-4">
                <Button
                  variant="outline-primary"
                  className="rounded-pill px-4"
                  onClick={loadComments}
                >
                  Tải bình luận
                </Button>
              </div>
            )}

            {loadingComments ? (
              <div className="text-center py-4">
                <Spinner animation="border" variant="primary" />

                <p className="text-muted mt-3">Đang tải bình luận...</p>
              </div>
            ) : commentsLoaded && comments.length === 0 ? (
              <div className="text-center py-5">
                <i className="bi bi-chat-dots fs-1 text-muted"></i>

                <p className="text-muted mt-3 mb-0">Chưa có bình luận nào.</p>
              </div>
            ) : (
              comments.map((c) => (
                <div key={c.id} className="d-flex gap-3 py-3 border-bottom">
                  <img
                    src={c.avatar}
                    alt={c.email}
                    className="rounded-circle border"
                    style={{
                      width: "52px",
                      height: "52px",
                      objectFit: "cover",
                    }}
                  />

                  <div className="flex-grow-1">
                    <div className="d-flex justify-content-between align-items-start flex-wrap gap-2">
                      <div>
                        <div className="fw-semibold">{c.email}</div>

                        <small className="text-muted">
                          {moment(c.createdAt).fromNow()}
                        </small>
                      </div>

                      {user && user.email === c.email && (
                        <Button
                          variant="light"
                          size="sm"
                          className="border text-danger rounded-pill px-3 py-1"
                          onClick={() => deleteComent(c.id)}
                        >
                          Xoá
                        </Button>
                      )}
                    </div>

                    <div className="bg-light rounded-4 p-3 mt-2">
                      {c.content}
                    </div>
                  </div>
                </div>
              ))
            )}
          </Card.Body>
        </Card>
      </Container>
    </div>
  );
};

export default DocumentDetailScreen;
