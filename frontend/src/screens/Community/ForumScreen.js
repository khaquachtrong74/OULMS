import { useContext, useEffect, useMemo, useState } from "react";
import {
  Container,
  Row,
  Col,
  Card,
  Button,
  Form,
  InputGroup,
  Badge,
  Spinner,
  Alert,
  Pagination,
} from "react-bootstrap";
import {
  ChatDots,
  Search,
  PencilSquare,
  PersonCircle,
  Clock,
} from "react-bootstrap-icons";
import { MyUserContext } from "../../configs/Contexts";
import Apis, { endpoints } from "../../configs/Apis";
import { useNavigate } from "react-router-dom";
import moment from "moment";
import "moment/locale/vi";
import forumApi from "../../configs/api/forumApi";

const ForumScreen = () => {
  const [user] = useContext(MyUserContext);
  const nav = useNavigate();

  const [posts, setPosts] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [totalItems, setTotalItems] = useState(0);

  const loadForumPosts = async (page = 1) => {
    try {
      setLoading(true);
      setError(null);

      const res = await forumApi.getAll({
        params: {
          page,
        },
      });

      setPosts(res.data.data || []);
      setCurrentPage(res.data.currentPage || 1);
      setTotalPages(res.data.totalPages || 1);
      setTotalItems(res.data.totalItems || 0);
    } catch (err) {
      console.error("Lỗi load forum posts:", err);
      setError("Không thể tải danh sách bài viết.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadForumPosts(1);
  }, []);

  const filteredPosts = useMemo(() => {
    if (!searchTerm.trim()) return posts;

    const keyword = searchTerm.toLowerCase();

    return posts.filter(
      (post) =>
        post.title?.toLowerCase().includes(keyword) ||
        post.content?.toLowerCase().includes(keyword) ||
        post.userEmail?.toLowerCase().includes(keyword),
    );
  }, [posts, searchTerm]);

  const deleteForumPost = async (forumPostId) => {
    const confirmDelete = window.confirm(
      "Bạn có chắc chắn muốn xoá bài viết này?",
    );

    if (!confirmDelete) return;

    try {
      await forumApi.delete(forumPostId);
      setPosts((prev) => prev.filter((p) => p.id !== forumPostId));
    } catch (err) {
      console.error("Lỗi xoá forum:", err);

      alert(err.response?.data || "Không thể xoá bài viết.");
    }
  };

  return (
    <div className="bg-light min-vh-100 py-4">
      <Container>
        <div className="d-flex justify-content-between align-items-center flex-wrap gap-3 mb-4">
          <div>
            <h2 className="fw-bold mb-1 d-flex align-items-center gap-2">
              <ChatDots className="text-primary" />
              Diễn đàn trao đổi
            </h2>

            <p className="text-muted mb-0">
              Nơi sinh viên và giảng viên cùng thảo luận, đặt câu hỏi.
            </p>
          </div>

          {user && (
            <Button
              onClick={() => nav("/forum-create")}
              variant="primary"
              className="fw-semibold rounded-pill px-4 d-flex align-items-center gap-2"
            >
              <PencilSquare size={18} />
              Đăng bài mới
            </Button>
          )}
        </div>

        <Row className="g-4">
          <Col lg={8}>
            {error && <Alert variant="danger">{error}</Alert>}

            {loading ? (
              <div className="text-center py-5">
                <Spinner animation="border" variant="primary" />
                <p className="text-muted mt-3">Đang tải bài viết...</p>
              </div>
            ) : filteredPosts.length === 0 ? (
              <Card className="border-0 shadow-sm rounded-4">
                <Card.Body className="text-center py-5">
                  <ChatDots size={48} className="text-muted mb-3" />
                  <h5 className="fw-bold">Chưa có bài viết nào</h5>
                  <p className="text-muted mb-0">
                    Hãy là người đầu tiên tạo bài thảo luận.
                  </p>
                </Card.Body>
              </Card>
            ) : (
              <div className="d-flex flex-column gap-3">
                {filteredPosts.map((post) => (
                  <Card key={post.id} className="border-0 shadow-sm rounded-4">
                    <Card.Body className="p-4">
                      <div className="d-flex align-items-center gap-3 mb-3">
                        <PersonCircle size={42} className="text-secondary" />

                        <div>
                          <div className="fw-bold">{post.userEmail}</div>

                          <div className="text-muted small d-flex align-items-center gap-1">
                            <Clock size={14} />
                            {moment(post.createdAt).fromNow()}
                          </div>
                        </div>

                        <Badge
                          bg="light"
                          text="dark"
                          className="border ms-auto"
                        >
                          #{post.id}
                        </Badge>
                      </div>

                      <Card.Title className="fw-bold fs-5 mb-2">
                        {post.title}
                      </Card.Title>

                      <Card.Text className="text-secondary">
                        {post.content}
                      </Card.Text>

                      <div className="d-flex justify-content-end pt-3 border-top">
                        <Button
                          variant="outline-primary"
                          size="sm"
                          className="rounded-pill px-3"
                          onClick={() => nav(`/forum/${post.id}`)}
                        >
                          Xem thảo luận
                        </Button>
                      </div>

                      {user && user.email === post.userEmail && (
                        <Button
                          variant="outline-danger"
                          size="sm"
                          className="rounded-pill px-3"
                          onClick={() => deleteForumPost(post.id)}
                        >
                          Xoá
                        </Button>
                      )}
                    </Card.Body>
                  </Card>
                ))}

                {totalPages > 1 && (
                  <Pagination className="justify-content-center mt-3">
                    {[...Array(totalPages)].map((_, index) => {
                      const page = index + 1;

                      return (
                        <Pagination.Item
                          key={page}
                          active={page === currentPage}
                          onClick={() => loadForumPosts(page)}
                        >
                          {page}
                        </Pagination.Item>
                      );
                    })}
                  </Pagination>
                )}
              </div>
            )}
          </Col>

          <Col lg={4}>
            <Card className="border-0 shadow-sm rounded-4 mb-4">
              <Card.Body className="p-4">
                <h6 className="fw-bold mb-3">Tìm kiếm thảo luận</h6>

                <InputGroup>
                  <Form.Control
                    placeholder="Nhập từ khóa..."
                    className="bg-light border-0"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                  />

                  <Button variant="light" className="border-0 bg-light">
                    <Search />
                  </Button>
                </InputGroup>
              </Card.Body>
            </Card>

            <Card className="border-0 shadow-sm rounded-4">
              <Card.Body className="p-4">
                <h6 className="fw-bold mb-3">Thống kê diễn đàn</h6>

                <div className="d-flex justify-content-between border-bottom py-2">
                  <span className="text-muted">Tổng bài viết</span>
                  <strong>{totalItems}</strong>
                </div>

                <div className="d-flex justify-content-between py-2">
                  <span className="text-muted">Trang hiện tại</span>
                  <strong>
                    {currentPage}/{totalPages}
                  </strong>
                </div>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default ForumScreen;
