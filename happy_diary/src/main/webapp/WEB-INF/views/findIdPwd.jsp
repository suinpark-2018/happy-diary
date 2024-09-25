<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <title>아이디,비밀번호 찾기|Happy Diary</title>
</head>
<link rel="stylesheet" href="/resources/css/findIdPwd.css">
<body>
<div class="container">

    <a href="/" class="additional-options">< Home</a>

    <!-- Tabs -->
    <div class="tabs">
        <button class="tab-button active" data-target="find-id">아이디 찾기</button>
        <button class="tab-button" data-target="find-password">비밀번호 찾기</button>
    </div>

    <!-- Find ID Form -->
    <div id="find-id" class="tab-content" style="display: block;">
        <table>
            <tr>
                <td>
                    <label for="find-id-email">Email</label><br>
                    <input type="text" id="find-id-email" name="email" placeholder="이메일 입력" required>
                </td>
                <td>
                    <button type="button" id="sendEmailBtn-id">전송</button>
                </td>
            </tr>
        </table>
        <div class="input-verification-code" id="verificationDiv-id">
            <table>
                <tr>
                    <td>
                        <input type="text" name="mailKey" placeholder="인증번호를 입력해주세요." required>
                    </td>
                    <td>
                        <button id="verifyCodeBtn-id">인증</button>
                    </td>
                </tr>
            </table>
            <p>인증번호를 받지 못하셨다면, 다시 전송 버튼을 클릭하십시오.</p>
        </div>
        <!-- 조회된 아이디가 표시될 영역 -->
        <div id="found-id"></div>
    </div>

    <!-- Find Password Form -->
    <div id="find-password" class="tab-content">
        <table>
            <tr>
                <td>
                    <label for="id">아이디</label>
                    <input type="text" id="id" name="id" placeholder="아이디 입력" required>
                </td>
            </tr>
            <tr>
                <td>
                    <label for="find-pwd-email">이메일</label>
                    <input type="email" id="find-pwd-email" name="email" placeholder="이메일 입력" required>
                </td>
                <td>
                    <button type="button" id="sendEmailBtn-pwd">Send</button>
                </td>
            </tr>
        </table>
        <div class="input-verification-code" id="verificationDiv-pwd">
            <table>
                <tr>
                    <td>
                        <input type="text" name="mailKey" placeholder="인증번호를 입력해주세요." required>
                    </td>
                    <td>
                        <button id="verifyCodeBtn-pwd">인증</button>
                    </td>
                </tr>
            </table>
            <p>인증번호를 받지 못하셨다면, 다시 전송 버튼을 클릭하십시오.</p>
        </div>
    </div>

    <!-- Footer Links -->
    <div class="footer">
        <a href="/login/form">Login</a>
        <a>|</a>
        <a href="/register/form">Sign Up</a>
    </div>
</div>
<input type="text" id="pwdUpdateState" value="${pwdUpdateState}" hidden="hidden">
</body>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/findIdPwd.js"></script>
</html>
