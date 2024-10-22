<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login|Happy Diary</title>
</head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css">
<body>
    <div class="login-container">
        <a href="/" class="additional-options">< Home</a>
        <h1>Sign In</h1>
        <form action="/login/in" method="POST">
            <p id="error-msg">${errorMsg}</p>
            <div class="input-group">
                <label for="id">ID</label>
                <input type="text" id="id" name="id" value="${cookie['savedId'].value}" placeholder="Enter your ID" required>
                <p id="non-exist-id-msg">${nonExistIdMsg}</p>
            </div>
            <div class="input-group">
                <label for="pwd">Password</label>
                <input type="password" id="pwd" name="pwd" placeholder="Enter your password" required>
                <p id="not-match-msg">${notMatchMsg}</p>
            </div>
            <div class="checkbox-group">
                <input type="checkbox" id="rememberMe" name="rememberMe" ${cookie['savedId'] != null ? 'checked' : ''}>
                <label for="rememberMe">Remember Me</label>
            </div>
            <button type="submit" class="btn primary">Sign In</button>
            <div class="additional-options">
                <a href="/find/idAndPwd" class="forgot-link">Forgot ID/Password?</a>
                <a href="/register/verification" class="signup-link">Sign Up</a>
            </div>
        </form>
        <div class="social-login">
            <p>SNS 계정 로그인 서비스는 추후 제공 예정입니다.</p>
            <p>sign in with</p>
            <button class="btn social naver" onclick="location.href='#'">Naver</button>
            <button class="btn social kakao" onclick="location.href='#'">Kakao</button>
            <button class="btn social google" onclick="location.href='#'">Google</button>
        </div>
    </div>
    <input type="text" id="pwdUpdateState" hidden="hidden" value="${pwdUpdateState}">
</body>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/login.js"></script>
</html>
