<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>비밀번호 변경</title>
</head>
<link rel="stylesheet" href="/resources/css/modifyPwd.css">
<body>
    <form action="/find/pwd/modify" method="POST" onsubmit="return validatePassword()">
        <a href="/find/idAndPwd" class="additional-options">< 뒤로 </a>
        <p>비밀번호 변경</p>
        <table>
            <tr>
                <td>
                    <label for="user-id">아이디</label>
                </td>
                <td>
                    <input type="text" id="user-id" name="id" value="${sessionScope.userIdToFindPwd}" readonly>
                </td>
            </tr>
            <tr>
                <td>
                    <label for="password">새 비밀번호</label>
                </td>
                <td>
                    <input type="password" id="password" name="pwd" required>
                </td>
            </tr>
            <tr>
                <td>
                    <label for="confirmPassword">비밀번호 확인</label>
                </td>
                <td>
                    <input type="password" id="confirmPassword" name="confirmPassword" required>
                </td>
            </tr>
        </table>
        <div class="button-container">
            <button type="button" onclick="location.href='/'">HOME</button>
            <button type="submit">비밀번호 변경</button>
        </div>
    </form>
    <input type="text" id="pwdUpdateState" value="${pwdUpdateState}" hidden="hidden">
</body>
<script>
    window.onload = function () {
        let pwdUpdateState = $("#pwdUpdateState").val();
        if (pwdUpdateState !== null && pwdUpdateState !== "") {
            alert(pwdUpdateState);
        }
    }

    function validatePassword() {
        // 입력된 비밀번호 값 가져오기
        let password = document.getElementById("password").value;
        let confirmPassword = document.getElementById("confirmPassword").value;
        let pattern = new RegExp('^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+=]).+$');

        // 비밀번호가 일치하는지 확인
        if (password !== confirmPassword) {
            alert("비밀번호가 일치하지 않습니다. 다시 확인해주세요.");
            return false; // 폼 제출 방지
        }
        // 비밀번호 길이 확인
        else if (password.length < 8 || password.length > 20) {
            alert("비밀번호는 최소 8자 이상, 최대 20자 이하로 입력해주세요.");
            return false;
        }
        // 비밀번호 조건 확인
        else if (!pattern.test(password)) {
            alert("비밀번호는 공백 없이 영문 대소문자, 숫자 최소 1개 이상 포함해야 합니다.");
            return false;
        }
        return true; // 폼 제출 허용
    }
</script>
</html>