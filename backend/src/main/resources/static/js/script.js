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