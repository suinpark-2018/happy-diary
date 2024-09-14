<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원가입 본인인증</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/identityVerification.css">
</head>

<body>
    <div class="wrapper">
        <a href="/" class="back-item">< Back</a>
        <div class="title">
            <a href="/">본인 인증</a>
        </div>
        <div class="content">
            <div class="error-msg">
                <p id="email-format-error-msg"></p>
            </div>
            <table>
                <tr>
                    <td>
                        <input type="email" id="email" name="email" oninput="checkEmailFormat(this.value)" placeholder="이메일을 입력해주세요." required>
                    </td>
                    <td>
                        <button id="sendEmailBtn" disabled>전송</button>
                    </td>
                </tr>
            </table>
            <div class="input-verification-code" id="verificationDiv" onclick="showVerifyEmailForm()">
                <table>
                    <tr>
                        <td>
                            <input type="text" id="inputMailKey" name="inputMailKey" placeholder="인증번호를 입력해주세요." required>
                        </td>
                        <td>
                            <button id="verifyCodeBtn" disabled>인증</button>
                        </td>
                    </tr>
                </table>
                <p>인증번호를 받지 못하셨다면, 다시 전송 버튼을 클릭하십시오.</p>
            </div>

        </div>
    </div>
</body>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/identityVerification.js"></script>
</html>
