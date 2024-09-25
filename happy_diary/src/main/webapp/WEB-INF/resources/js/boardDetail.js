function confirmDelete() {
    // 사용자가 '확인'을 누르면 true 반환하여 삭제를 진행하고, '취소'를 누르면 false 반환하여 삭제 중단
    return confirm("정말 삭제하시겠습니까?");
}

$(document).ready(function () {
    // 댓글 수정
    $(".modify-comment").click(function (e) {
        let cno = $(this).val();
        let content = $("#comment-content-" + cno).text(); // 기존 댓글 내용
        $("#modify-comment-content").val(content);
        $("#modify-comment-form").data("cno", cno).show();
    });

    $("#save-modified-comment").click(function () {
        let cno = $("#modify-comment-form").data("cno");
        let updatedContent = $("#modify-comment-content").val();

        $.ajax({
            url: '/comment/update',
            type: 'POST',
            data: JSON.stringify({cno: cno, content: updatedContent}),
            contentType: 'application/json; charset=utf-8',
            dataType: 'text',
            success: function (response) {
                console.log("댓글 수정 성공");
                $("#comment-content-" + cno).text(updatedContent); // 수정된 내용 업데이트
                $("#modify-comment-form").hide(); // 수정 폼 숨김
            },
            error: function (xhr, status, error) {
                console.error("댓글 수정 실패:", error);
            }
        });
    });

    // 답글 수정
    $(".modify-reply").click(function (e) {
        let cno = $(this).val();
        let content = $("#reply-content-" + cno).text(); // 기존 답글 내용
        $("#modify-reply-content").val(content);
        $("#modify-reply-form").data("cno", cno).show();
    });

    $("#save-modified-reply").click(function () {
        let cno = $("#modify-reply-form").data("cno");
        let updatedContent = $("#modify-reply-content").val();

        $.ajax({
            url: '/comment/update',
            type: 'POST',
            data: JSON.stringify({ cno: cno, content: updatedContent }),
            contentType: 'application/json; charset=utf-8',
            dataType: 'text',
            success: function (response) {
                console.log("답글 수정 성공");
                $("#reply-content-" + cno).text(updatedContent); // 수정된 내용 업데이트
                $("#modify-reply-form").hide(); // 수정 폼 숨김
            },
            error: function (xhr, status, error) {
                console.error("답글 수정 실패:", error);
            }
        });
    });

    // 댓글 삭제
    $(".delete-comment").click(function (e) {
        let cno = $(this).val();
        $.ajax({
            url: '/comment/delete',
            type: 'POST',
            data: JSON.stringify({cno: cno}),
            contentType: 'application/json; charset=utf-8',
            dataType: 'text',
            success: function (response) {
                console.log("댓글 삭제 성공")
                $("#comment-" + cno).remove();
            },
            error: function (xhr, status, error) {
                console.error("댓글 삭제 실패:", error);
                alert(xhr.responseText);
            }
        });
    });

    // 답글 삭제
    $(".delete-reply").click(function (e) {
        let cno = $(this).val();
        $.ajax({
            url: '/comment/delete',
            type: 'POST',
            data: JSON.stringify({cno: cno}),
            contentType: 'application/json; charset=utf-8',
            dataType: 'text',
            success: function (response) {
                console.log("답글 삭제 성공")
                $("#reply-" + cno).remove();
            },
            error: function (xhr, status, error) {
                console.error("답글 삭제 실패:", error);
                alert(xhr.responseText);
            }
        });
    });
});