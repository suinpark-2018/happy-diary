// 사용자 로그아웃 버튼 이벤트 처리
$('#logout').on('click', function(event) {
    event.preventDefault(); // 기본 동작 막기

    if (confirm('로그아웃 하시겠습니까?')) {
        $.get('/login/out', function() {
            alert('로그아웃 되었습니다.');
            window.location.href = "/login/form"; // 로그인 페이지로 리디렉션
        }).fail(function() {
            alert('로그아웃에 실패했습니다.');
        });
    }
});

$('#accountDeletion').on('click', function(event) {
    event.preventDefault(); // 기본 동작 막기

    if (confirm('정말로 탈퇴 하시겠습니까?')) {
        $.post('/delete/account', function() {
            alert('탈퇴되었습니다.');
            window.location.href = "/login/form"; // 로그인 페이지로 리디렉션
        }).fail(function() {
            alert('탈퇴 실패했습니다.');
        });
    }
});