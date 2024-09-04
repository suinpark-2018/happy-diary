// 사용자 로그아웃 버튼 이벤트 처리
document.getElementById('logout-btn').addEventListener('click', function () {
    // 로그아웃 API 호출 또는 세션 종료 처리 후 리디렉션
    alert('로그아웃 되었습니다.');
    window.location.href = "/login/form"; // 로그인 페이지로 리디렉션
});