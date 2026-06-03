function deleteSubject(url) {
    if (confirm("Bạn có muốn xoá không") === true) {
        fetch(url, {
            method: "delete"
        }).then(res => {
            if (res.status === 204)
                location.reload();
            else
                alert("Xoá thất bại!");
        });
    }
    ;
}
;

function deleteTopic(url) {
    if (confirm("Bạn có muốn xoá không") === true) {
        fetch(url, {
            method: "delete"
        }).then(res => {
            if (res.status === 204)
                location.reload();
            else
                alert("Xoá thất bại!");
        });
    }
    ;
}
;

function updateStatusDocument(url) {
    if (confirm("Bạn có muốn cập nhật trạng thái không") === true) {
        fetch(url, {
            method: "post"
        }).then(res => {
            if (res.status === 204)
                location.reload();
            else
                alert("Xoá thất bại!");
        });
    }
    ;
}
;

function deleteUser(url) {
    if (confirm("Bạn có muốn xoá không") === true) {
        fetch(url, {
            method: "delete"
        }).then(res => {
            if (res.status === 204)
                location.reload();
            else
                alert("Xoá thất bại!");
        });
    }
}
;
function quickUpdateStatus(url, documentId, newStatus) {
    if (confirm(`Xác nhận chuyển trạng thái tài liệu sang: ${newStatus}?`)) {
        
        // Sử dụng url được truyền từ Thymeleaf xuống để đảm bảo có Context Path
        fetch(url, {
            method: 'POST', // Giữ nguyên POST
            headers: {'Content-Type': 'application/json'}
        })
        .then(response => {
            if (response.ok) {
                const row = document.getElementById(`doc-row-${documentId}`);
                if (row) {
                    row.classList.add('fade-out'); 
                    setTimeout(() => {
                        row.remove();
                    }, 300);
                }
            } else {
                alert("Không thể cập nhật trạng thái do lỗi từ máy chủ.");
            }
        })
        .catch(error => {
            console.error("Lỗi:", error);
            alert("Mất kết nối đến máy chủ.");
        });
    }
}