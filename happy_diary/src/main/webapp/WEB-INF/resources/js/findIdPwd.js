window.onload = function () {
    let pwdUpdateState = $("#pwdUpdateState").val();
    if (pwdUpdateState !== null && pwdUpdateState !== '') {
        alert(pwdUpdateState);
    }
}

// 이메일 형식 체크 함수
function checkEmailFormat(inputValue, sendEmailBtn, msgId) {
    let pattern = new RegExp('^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$');
    let msg = document.getElementById(msgId);

    if(inputValue.length > 0 && !inputValue.match(pattern)) {
        msg.innerHTML = "이메일 형식이 올바르지 않습니다.";
        sendEmailBtn.disabled = true;
    } else if (inputValue.length == 0) {
        msg.innerHTML = "";
        sendEmailBtn.disabled = true;
    } else {
        msg.innerHTML = "";
        sendEmailBtn.disabled = false;
    }
}

$(document).ready(function() {
    // 아이디 찾기 탭과 비밀번호 찾기 탭 전환
    $(".tab-button").click(function () {
        // 모든 탭 버튼에서 active 클래스 제거
        $(".tab-button").removeClass("active");
        // 클릭한 버튼에 active 클래스 추가
        $(this).addClass("active");

        // 모든 탭 콘텐츠 숨기기
        $(".tab-content").removeClass("active").hide(); // 탭 내용을 숨기기

        // 선택된 탭의 콘텐츠만 보이기
        $("#" + $(this).data("target")).addClass("active").show(); // 해당 탭 내용 표시
    });

    // 아이디 찾기 이메일 입력 필드에서 실시간 이메일 형식 확인
    $("#find-id-email").on('input', function() {
        let email = $(this).val();
        checkEmailFormat(email, document.getElementById('sendEmailBtn-id'), 'wrong-email-format-msg-id');
    });

    // 비밀번호 찾기 이메일 입력 필드에서 실시간 이메일 형식 확인
    $("#find-pwd-email").on('input', function() {
        let email = $(this).val();
        checkEmailFormat(email, document.getElementById('sendEmailBtn-pwd'), 'wrong-email-format-msg-pwd');
    });

    // 아이디 찾기
    $("#sendEmailBtn-id").click(function () {
        let email = $("#find-id-email").val();

        $.ajax({
            url: "/find/id/verify",
            type: "POST",
            data: JSON.stringify({ email: email }),
            contentType: 'application/json; charset=utf-8', // JSON 데이터 전송
            dataType: 'json', // 서버 응답을 JSON으로 받음
            success: function (response) {
                // 인증번호 입력란 활성화
                // $("#verificationDiv-id").addClass('active'); // .active 클래스 추가
                alert("이메일이 전송되었습니다.");
            },
            error: function (xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    });

    // 아이디 찾기 인증번호 검증
    $("#verifyCodeBtn-id").click(function() {
        let email = $("#find-id-email").val();
        let verificationCode = $("#verificationDiv-id input[name='mailKey']").val(); // 특정 div 내의 input 선택

        $.ajax({
            url: "/find/id/verification",
            type: "POST",
            data: JSON.stringify({ email: email, verificationCode: verificationCode }),
            contentType: 'application/json; charset=utf-8', // JSON 데이터 전송
            dataType: 'json',
            success: function(response) {
                if (response.id) {
                    // 조회된 아이디를 화면에 출력
                    $("#found-id").css('display', 'block').html("해당 이메일로 조회된 아이디는 다음과 같습니다." + "<br>" + response.id);
                } else {
                    alert(response.message); // 에러 메시지 출력
                }
            },
            error: function (xhr) {
                let response = JSON.parse(xhr.responseText); // 응답을 JSON으로 파싱
                alert(response.message); // 서버에서 전달된 메시지 출력
            }
        });
    });

    // 비밀번호 찾기
    $("#sendEmailBtn-pwd").click(function () {
        let id = $("#id").val();  // 비밀번호 찾기 탭의 id 필드 선택
        let email = $("#find-pwd-email").val();

        $.ajax({
            url: "/find/pwd/verify",
            type: "POST",
            data: JSON.stringify({ id: id, email: email }),
            contentType: 'application/json; charset=utf-8',
            dataType: 'text',
            success: function (response) {
                $("#id").prop("readonly", true);
                alert(response);
            },
            error: function (xhr, status, error) {
                alert(xhr.responseText);
            }
        });
    });

    // 비밀번호 찾기 인증번호 검증
    $("#verifyCodeBtn-pwd").click(function() {
        let verificationCode = $("#verificationDiv-pwd input[name='mailKey']").val();
        let id = $("#id").val();

        $.ajax({
            url: "/find/pwd/verification",
            type: "POST",
            data: JSON.stringify({ verificationCode: verificationCode, id: id}),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function(response) {
                alert(response.message);
                window.location.href = "/find/pwd/modify"; // 비밀번호 재설정 페이지로 이동
            },
            error: function (xhr) {
                let response = JSON.parse(xhr.responseText);
                alert(response.message);
            }
        });
    });
});