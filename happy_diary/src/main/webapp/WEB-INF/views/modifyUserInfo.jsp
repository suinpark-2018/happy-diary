<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원정보 수정|Happy Diary</title>
</head>
<link rel="stylesheet" href="/resources/css/nav.css">
<link rel="stylesheet" href="/resources/css/modifyUserInfo.css">
<body>
    <jsp:include page="/WEB-INF/views/include/nav.jsp"/>
    <section>
        <div class="sub-title">
            <a href="/board/home" class="sub-tag">home ></a>
            <a href="#" class="sub-tag">회원정보 수정</a>
        </div>
        <div class="wrapper">
            <div class="head-title">
                <span>회원정보 수정</span>
            </div>
            <div class="content">
                <p class="table_title">
                    기본 회원정보
                </p>
                <table>
                    <tr>
                        <td>아이디</td>
                        <td>${userDto.id}</td>
                        <td></td>
                    </tr>
                    <tr class="no-bottom">
                        <td>닉네임</td>
                        <td>해당 기능은 추후 제공 예정입니다.</td>
<%--                        <td>${userDto.nickname}</td>--%>
                        <td><button type="button" id="sns-connect" onclick="showSNSList()">계정 연결</button></td>
                    </tr>
                    <tr class="hidden no-bottom sns-kakao">
                        <td></td>
                        <td>
                            <a class="kakao-connect" href="#">
<%--                            <a class="kakao-connect" href="https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=873c82dfa901cd280c11ee222e944826&redirect_uri=http://localhost:8080/kakaoConnection">--%>
                                <span class="sns-title">KAKAOTALK</span>
                                <img src="/resources/img/kakao.png" alt="Kakao 로그인" class="login-logo">
                                <span>카카오 연동</span>
                            </a>
                        </td>
                        <td></td>
                    </tr>
                    <tr class="hidden sns-naver">
                        <td></td>
                        <td>
                            <a class="naver-connect" href="#">
<%--                            <a class="naver-connect" href="https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=nV14togkwJUZVzs2_YnF&state=STATE_STRING&redirect_uri=http://localhost:8080/naverConnection">--%>
                                <span class="sns-title">NAVER</span>
                                <img src="/resources/img/naver.png" alt="Naver 로그인">
                                <span>네이버 연동</span>
                            </a>
                        </td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>비밀번호</td>
                        <td>********</td>
                        <td><button id="modify-pwd-btn" onclick="showPwdChangeInput()">비밀번호 변경</button></td>
                    </tr>
                    <tr class="hidden no-bottom new-pwd-show">
                        <td>현재 비밀번호</td>
                        <td>
                            <input type="password" id="pwd" name="pwd" oninput="checkPwdLength()">
                            <span id="pwd-length-error-msg"></span>
                        </td>
                        <td></td>
                    </tr>
                    <tr class="hidden no-bottom new-pwd-show">
                        <td>새 비밀번호</td>
                        <td><input type="password" id="newPwd1" name="pwd"></td>
                        <td></td>
                    </tr>
                    <tr class="hidden new-pwd-show">
                        <td>비밀번호 확인</td>
                        <td>
                            <input type="password" id="newPwd2" name="pwd" oninput="checkPwdMatch()">
                            <span id="match-error-msg"></span>
                        </td>
                        <td><button type="button" id="saveNewPwdBtn" disabled>비밀번호 저장</button></td>
                    </tr>
                    <tr class="hidden">
                        <td></td>
                        <td id="wrong-pwd-format-msg">${wrongFormat}</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>이름</td>
                        <td>${userDto.name}</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>휴대폰 번호</td>
                        <td>
                            <c:if test="${not empty userDto.phone_num}">
                                ${userDto.phone_num}
                            </c:if>
                            <c:if test="${empty userDto.phone_num}">
                                ${userDto.phone_num}
                            </c:if>
                        </td>
                        <td><button id="modify-phone-num" onclick="showPhoneNumChangeInput()">휴대폰번호 변경</button></td>
                    </tr>
                    <tr class="hidden new-phone-num-show">
                        <td>변경할 휴대폰 번호</td>
                        <td>
                            <input type="text" id="phone_num" name="phone_num" placeholder="- 기호를 포함하여 입력하십시오." maxlength="15" oninput="restrictPhoneNumbers(this.value); inputNum(this);">
                            <span id="phone-number-error-msg"></span>
                        </td>
                        <td><button type="button" id="saveNewPhoneNumBtn" disabled>휴대폰 번호 저장</button></td>
                    </tr>
                    <tr class="no-bottom">
                        <td>이메일</td>
                        <td>${userDto.email}</td>
                        <td><button id="modify-email-btn" onclick="showEmailChangeInput()">이메일 변경</button></td>
                    </tr>
                    <tr class="hidden no-bottom new-email-show">
                        <td></td>
                        <td>
                            <div>
                                * 메일주소 입력 후 인증하기 버튼을 누르시면, 고객님의 이메일로 인증 번호가 적힌 메일이 발송됩니다.
                            </div>
                            <div>
                                * 아래에 인증 번호 입력 후 인증번호 전송 버튼을 누르시면 인증이 완료됩니다.
                            </div>
                        </td>
                        <td></td>
                    </tr>
                    <tr class="hidden no-bottom new-email-show">
                        <td>변경할 이메일 주소</td>
                        <td>
                            <input type="email" id="newEmail" name="email" maxlength="50" oninput="checkEmailFormat(this.value)">
                            <span id="email-error-msg"></span>
                        </td>
                        <td><button type="button" id="sendEmailBtn">인증번호 전송</button></td>
                    </tr>
                    <tr class="hidden new-email-show">
                        <td>인증번호</td>
                        <td><input type="text" id="verificationCode" name="mail_key" maxlength="10" required></td>
                        <td><button type="button" id="verifyCodeBtn">인증</button></td>
                    </tr>
                </table>
            </div>
        </div>
    </section>
</body>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="/resources/js/nav.js"></script>
<script src="/resources/js/modifyUserInfo.js"></script>
</html>
