import { useContext } from "react";
import { Button, Form, InputGroup } from "react-bootstrap";
import { MyUserContext } from "../../configs/Contexts";

const HomeScreen = () => {
    const [user] = useContext(MyUserContext);

    return (
        <>
            <section>
                <div className=" w-100 h-100 shadow-sm card rounded-0 bg-oulms-brand p-5 text-black text-center ">
                    {/* Đổi text-primary thành text-warning (màu vàng) để nổi bật trên nền xanh */}
                    <div className="card-header bg-transparent border-0 fw-bold text-warning uppercase tracking-wide fs-5">
                        Khám phá tri thức, nâng tầm học thuật
                    </div>
                    <div className="card-body">
                        <blockquote className="blockquote mb-0">
                            <p className="fs-4 fst-italic text-white mb-3">
                                "Học vấn do người siêng năng đạt được, tài sản do người tinh khôn sở hữu, quyền lực do người dũng cảm nắm giữ."
                            </p>
                            <footer className="blockquote-footer fs-6 mt-2 text-white-50">
                                Ngạn ngữ thế giới — <cite title="Nguồn trích dẫn" className="text-warning">Hành trang tri thức</cite>
                            </footer>
                        </blockquote>
                        <div className="d-flex justify-content-center mt-3">
                            <InputGroup size="lg" className="shadow-lg" style={{ maxWidth: '900px' }}>
                                <Form.Control
                                    placeholder="Nhập tên môn học, giảng viên hoặc từ khóa tài liệu..."
                                    aria-label="Search input"
                                    className="border-0 rounded-start-pill px-4"
                                />
                                <Button
                                    variant="warning"
                                    className="border-0 rounded-0 px-5 fw-bold"
                                >
                                    Tìm kiếm
                                </Button>
                            </InputGroup>
                        </div>
                    </div>
                </div>
            </section>
            <section className="pt-4 px-4">
                <h4>Danh mục môn học</h4>
                <p className="text-secondary">Khám phá tài liệu theo từng chuyên môn</p>
            </section>
            <section className="pt-4 px-4">
                <h4 >Tài liệu mới cập nhật</h4>
                <p className="text-secondary">Cập nhật kiến thức mới nhất từ cộng đồng</p>
            </section>
        </>
    );
};

export default HomeScreen;
