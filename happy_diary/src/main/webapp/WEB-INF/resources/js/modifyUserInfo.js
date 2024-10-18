function inputNum(element) {
    element.value = element.value.replace(/[^0-9-]/g, ""); // 숫자와 -만 허용
}

function checkPwdLength() {
    let pwd = document.getElementById('pwd').value;
    let msg = document.getElementById('pwd-length-error-msg');

    if (pwd.length > 0 && pwd.length < 8 || pwd.length > 20) {
        msg.innerHTML = "비밀번호는 8-20자리 이하로 입력해야 합니다."
    } else {
        msg.innerHTML = "";
    }
}

function checkPwdMatch() {
    let pwd1 = document.getElementById('newPwd1').value;
    let pwd2 = document.getElementById('newPwd2').value;
    let msg = document.getElementById('match-error-msg');
    let saveBtn = document.getElementById('saveNewPwdBtn');

    if (pwd1 !== pwd2) {
        msg.innerHTML = "비밀번호가 일치하지 않습니다."
        saveBtn.disabled = true;
    } else {
        msg.innerHTML = "";
        saveBtn.disabled = false;
    }
}

function showSNSList() {
    let snsKakao = document.getElementsByClassName("sns-kakao");
    let snsNaver = document.getElementsByClassName("sns-naver");

    for (let i = 0; i < snsKakao.length; i++) {
        snsKakao[i].style.display = snsKakao[i].style.display === "table-row" ? "none" : "table-row";
    }

    for (let i = 0; i < snsNaver.length; i++) {
        snsNaver[i].style.display = snsNaver[i].style.display === "table-row" ? "none" : "table-row";
    }
}

function showPwdChangeInput() {
    let pwdChangeInput = document.getElementsByClassName("new-pwd-show");
    let pwdChangeBtn = document.getElementById("modify-pwd-btn");

    for(let i = 0; i < pwdChangeInput.length; i++) {
        pwdChangeInput[i].style.display = "table-row"; // 입력란을 보이도록 변경

    }
    pwdChangeBtn.style.display = "none";
}

function showPhoneNumChangeInput() {
    let phoneNumChangeInput = document.getElementsByClassName("new-phone-num-show");
    let phoneNumChangeBtn = document.getElementById("modify-phone-num");

    for(let i = 0; i < phoneNumChangeInput.length; i++) {
        phoneNumChangeInput[i].style.display = "table-row"; // 입력란을 보이도록 변경
    }
    phoneNumChangeBtn.style.display= "none";
}

function restrictPhoneNumbers(inputValue) {
    let pattern = new RegExp('^01[016789]-\\d{3,4}-\\d{4}$');
    let msg = document.getElementById('phone-number-error-msg');
    let phoneNumChangeBtn = document.getElementById('saveNewPhoneNumBtn');

    if (inputValue.length > 0 && !inputValue.match(pattern)) {
        msg.innerHTML = "핸드폰 번호는 숫자와 - 부호만 입력 가능합니다.";
        phoneNumChangeBtn.disabled = true;
    } else if (inputValue.length === 0 || inputValue.length < 10) {
        msg.innerHTML = "";
        phoneNumChangeBtn.disabled = true;
    } else {
        msg.innerHTML = "";
        phoneNumChangeBtn.disabled = false;
    }
}

function showEmailChangeInput() {
    let emailChangeInput = document.getElementsByClassName("new-email-show");
    let emailChangeBtn = document.getElementById("modify-email-btn");

    for(let i = 0; i < emailChangeInput.length; i++) {
        emailChangeInput[i].style.display = "table-row"; // 입력란을 보이도록 변경
    }
    emailChangeBtn.style.display= "none";
}

function checkEmailFormat(inputValue) {
    let pattern = new RegExp('[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$')
    let msg = document.getElementById('email-error-msg');
    let sendEmailBtn = document.getElementById('sendEmailBtn');

    if(inputValue.length > 0 && !inputValue.match(pattern)) {
        msg.innerHTML = "이메일 형식이 올바르지 않습니다.";
        sendEmailBtn.disabled = true;
    } else if (inputValue.length === 0) {
        msg.innerHTML = "";
        sendEmailBtn.disabled = true;
    } else {
        msg.innerHTML = "";
        sendEmailBtn.disabled = false;
    }
}

// 1. 비밀번호 변경
$(document).ready(function () {
    // 1.1. '저장' 버튼 클릭
    $("#saveNewPwdBtn").click(function() {
        // let id = userDto.id;
        let pwd = $("#pwd").val();
        let newPwd = $("#newPwd2").val();

        // 1.1.1. AJAX 활용하여 현재 비밀번호 및 변경할 비밀번호 controller 에 넘겨줌.
        // 1.1.2. 현재 비밀번호 정확히 입력한 경우
        // 1.1.2.1. 변경된 비밀번호 고객 정보에 저장 및 변경이력 저장
        // 1.1.3. 현재 비밀번호 잘못 입력한 경우
        // 1.1.3.1. 기존 화면에서 오류메세지 출력
        $.ajax({
            url: "/userInfo/modifyPwd",
            type: "POST",
            data: { pwd: pwd, newPwd: newPwd },
            success: function (response) {
                alert("비밀번호 변경 성공");
                window.location.href = "/userInfo/modify";
            },
            error: function (xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    });
});

// 2. 이메일 인증
$(document).ready(function() {

    // 2.1. '인증번호 전송' 버튼 클릭
    $("#sendEmailBtn").click(function() {
        let newEmail = $("#newEmail").val();

        // 2.1.1. AJAX 활용하여 이메일 전송 요청
        // 2.1.2.1. 변경할 새로운 이메일 주소를 controller 에 넘겨줌.
        // 2.1.2.2. 이메일 전송 성공 시 인증번호 입력란 출력
        // 2.1.2.3. 이메일 전송 실패 시 오류 메세지 출력
        $.ajax({
            url: "/userInfo/verification",
            type: "POST",
            data: { newEmail: newEmail },
            success: function (response) {
                $("#verificationDiv").show();
                alert("인증번호 전송에 성공했습니다.");
            },
            error: function (xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    });

    // 2.2. '인증하기' 버튼 클릭
    $("#verifyCodeBtn").click(function() {
        let newEmail = $("#newEmail").val();
        let verificationCode = $("input[name='mail_key']").val();

        // 2.2.1. AJAX 활용하여 인증번호 입력 결과 확인
        // 2.2.2. 인증번호 일치 시
        // 2.2.2.1. 고객정보에서 이메일 변경하여 저장 및 변경이력 추가
        // 2.2.3. 인증번호 불일치 시
        // 2.2.3.1. 기존 화면에서 오류메세지 출력
        $.ajax({
            url: "/userInfo/modifyEmail",
            type: "POST",
            data: { newEmail: newEmail, verificationCode: verificationCode },
            success: function(response) {
                alert("이메일 변경 성공");
                window.location.href = "/userInfo/modify";
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    });

    // 4. 휴대폰 번호 변경
    $("#saveNewPhoneNumBtn").click(function() {
        let phone_num = $("#phone_num").val();

        $.ajax({
            url: "/userInfo/modifyPhoneNum",
            type: "POST",
            data: { phone_num: phone_num },
            success: function (response) {
                alert("휴대폰 번호 변경 성공");
                window.location.href = "/userInfo/modify";
            },
            error: function (xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    });
});