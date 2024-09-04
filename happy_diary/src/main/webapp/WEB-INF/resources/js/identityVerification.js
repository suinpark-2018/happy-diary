// 인증번호 입력란 보이도록 변경
function showVerifyEmailForm() {
    $("#verificationDiv").show();  // jQuery로 요소를 선택하고 보이도록 설정
}

// 이메일 형식 체크 및 버튼 활성화/비활성화
function checkEmailFormat(inputValue) {
    let emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,3}$/i;
    let sendEmailBtn = $("#sendEmailBtn");  // jQuery로 버튼 선택
    let msg = $("#email-format-error-msg");  // jQuery로 메시지 요소 선택

    if (inputValue.length > 0 && !emailPattern.test(inputValue)) {
        msg.text("잘못된 이메일 형식입니다.");  // 오류 메시지 출력
        sendEmailBtn.prop('disabled', true);  // 버튼 비활성화
    } else if (inputValue.length === 0) {
        msg.text("");  // 오류 메시지 초기화
        sendEmailBtn.prop('disabled', true);  // 버튼 비활성화
    } else {
        msg.text("");  // 오류 메시지 초기화
        sendEmailBtn.prop('disabled', false);  // 버튼 활성화
    }
}

$(document).ready(function() {

    // 이메일 입력란이 변경될 때마다 형식 확인
    $("#emailInput").on('input', function() {
        let emailValue = $(this).val();
        checkEmailFormat(emailValue);  // 이메일 형식 검사 함수 호출
    });

    // 인증 버튼
    // 처음에 버튼을 비활성화 상태로 설정
    $('#verifyCodeBtn').prop('disabled', true);

    // 입력 필드의 값이 변경될 때마다 이벤트 처리
    $('#inputMailKey').on('input', function() {
        let inputMailKey = $(this).val(); // 입력된 값을 가져옴

        // 입력된 값이 있을 때만 버튼을 활성화
        if (inputMailKey.trim().length > 0) {
            $('#verifyCodeBtn').prop('disabled', false);
        } else {
            $('#verifyCodeBtn').prop('disabled', true);
        }
    });

    // 1. '전송' 버튼 클릭
    $("#sendEmailBtn").click(function() {
        let email = $("#email").val();

        $.ajax({
            url: "/register/verificationEmail",
            type: "POST",
            data: JSON.stringify({ email: email }),
            contentType: 'application/json; charset=utf-8',
            dataType: 'text',
            success: function (response) {
                $("#verificationDiv").show();
                alert("인증번호 전송 성공")
            },
            error: function (xhr, status, error) {
                console.error("메일 전송 실패:", error);
                alert(xhr.responseText);
            }
        });
    });

    // 2. '인증' 버튼 클릭
    $("#verifyCodeBtn").click(function() {
        let inputMailKey = $("input[name='inputMailKey']").val();
        console.log($("input[name='inputMailKey']").val());
        $.ajax({
            url: "/register/verificationCode",
            type: "POST",
            data: JSON.stringify({ inputMailKey: inputMailKey }),
            contentType: 'application/json; charset=utf-8',
            dataType: 'text',
            success: function(response) {
                alert("본인인증 완료");
                window.location.href = "/register/form";
            },
            error: function(xhr, status, error) {
                console.error("인증 실패: ", error);
                alert(xhr.responseText);
            }
        });
    });
});