window.onload = function () {
    let pwdUpdateState = $("#pwdUpdateState").val();
    if (pwdUpdateState !== null && pwdUpdateState.length != '') {
        alert(pwdUpdateState);
    }
}

function validateForm() {
    const id = document.getElementById('id').value;
    const pwd = document.getElementById('pwd').value;

    if (id === '' || pwd === '') {
        alert('아이디와 비밀번호 모두 입력해주세요.');
        return false;
    }

    if (pwd.length < 8) {
        alert('비밀번호는 최소 8자 이상 입력해주세요.');
        return false;
    }

    return true;
}