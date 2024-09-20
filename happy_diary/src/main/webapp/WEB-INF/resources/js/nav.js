// 사용자 로그아웃 버튼 이벤트 처리
document.getElementById('logout').addEventListener('click', function (event) {
    event.preventDefault(); // 기본 동작 막기

    if (confirm('로그아웃 하시겠습니까?')) {
        // 로그아웃 처리
        alert('로그아웃 되었습니다.');

        // 로그아웃 API 호출 또는 세션 종료 처리 후 리디렉션
        window.location.href = "/login/form"; // 로그인 페이지로 리디렉션
    }
});