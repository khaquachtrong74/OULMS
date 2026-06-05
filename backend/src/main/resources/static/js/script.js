function deleteSubject(url) {
    if (confirm("Bạn có muốn xoá không") === true) {
        fetch(url, {
            method: "delete"
        }).then(res => {
            if (res.status === 204) {
                alert("Xoá thành công");
                location.reload();
            }
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
};

function deleteCourse(url) {
    if (confirm("Bạn có muốn xoá không") === true) {
        fetch(url, {
            method: "delete"
        }).then(res => {
            if (res.status === 204)
                location.reload();
            else
                alert("Xoá thất bại!");
        });
    };
};

function updateStatusDocument(url) {
    if (confirm("Bạn có muốn cập nhật trạng thái không") === true) {
        fetch(url, {
            method: "POST"
        }).then(async res => {
            const message = await res.text();

            if (res.ok) {
                alert(message);
                location.reload();
            } else {
                alert(message);
            }
        })
        .catch(err => {
            alert("Có lỗi xảy ra!");
            console.error(err);
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
};
