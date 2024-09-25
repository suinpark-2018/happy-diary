<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign up|Happy Diary</title>
</head>
<link rel="stylesheet" href="/resources/css/register.css">
<body>
    <div class="wrapper">
    <div class="header"><p>회원가입</p></div>
        <form action="/register/info" method="post">
            <table class="input-group">
                <tr>
                    <td>아이디</td>
                    <td>
                        <input type="text" id="id" name="id" oninput="checkIdFormat(this.value)" placeholder="아이디를 입력해주세요.">
                        <p id="id-msg"></p>
                        <p id="id-error-msg"></p>
                    </td>
                    <td>
                        <button type="button" id="checkDuplicateBtn">중복확인</button>
                    </td>
                </tr>
                <tr>
                    <td>비밀번호</td>
                    <td>
                        <input type="password" id="pwd1" name="pwd" oninput="checkPwdLength()" placeholder="영문 대문자 또는 특수문자를 포함하여 입력해주세요.">
                        <p id="pwd-length-error-msg"></p>
                    </td>
                </tr>
                <tr>
                    <td>비밀번호 확인</td>
                    <td>
                        <input type="password" id="pwd2" name="pwd" oninput="checkPwdMatch()" placeholder="비밀번호를 다시 입력해주세요.">
                        <p id="match-error-msg"></p>
                    </td>
                </tr>
                <tr>
                    <td>이름</td>
                    <td>
                        <input type="text" name="name" oninput="checkNameFormat(this.value)" placeholder="이름을 입력해주세요.">
                        <p id="name-error-msg"></p>
                    </td>
                </tr>
                <tr>
                    <td>성별</td>
                    <td>
                        <label><input type="radio" name="gender" value="M">남자</label>
                        <label><input type="radio" name="gender" value="F">여자</label>
                    </td>
                </tr>
                <tr>
                    <td>생년월일</td>
                    <td><input type="date" name="birth"></td>
                </tr>
                <tr>
                    <td>주소</td>
                    <td><input type="text" name="address"></td>
                </tr>
                <tr>
                    <td>E-mail</td>
                    <td>
                        <input type="email" name="email" oninput="checkEmailFormat(this.value)">
                        <p id="email-error-msg"></p>
                    </td>
                </tr>
                <tr>
                    <td>휴대폰번호</td>
                    <td>
                        <input type="text" name="phone_num" oninput="restrictToPhoneNumbers(this.value)" placeholder="- 기호를 포함하여 입력해주세요.">
                        <p id="mobile-number-error-msg"></p>
                    </td>
                </tr>
            </table>
            <div class="button-group">
                <button type="button" onclick="location.href='/'">Cancel</button>
                <button type="submit" id="sendUserInfoBtn">Submit</button>
            </div>
        </form>
    </div>
</body>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="/resources/js/register.js"></script>
</html>
