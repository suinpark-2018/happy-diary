function checkIdFormat(inputValue) {
    let pattern = new RegExp('^[A-Za-z0-9]+$')
    let msg = document.getElementById('id-error-msg');

    if (inputValue.length > 0 && inputValue.length < 3 || inputValue.length > 20) {
        msg.innerHTML = "아이디는 3자 이상, 20자 이하로 입력해주세요.";
    } else if(inputValue.length > 0 && !inputValue.match(pattern)) {
        msg.innerHTML = "아이디는 영문자와 숫자만 사용 가능합니다."
    } else {
        msg.innerHTML = "";
    }
}

function checkPwdLength() {
    let pwd1 = document.getElementById('pwd1').value;
    let msg = document.getElementById('pwd-length-error-msg');

    if (pwd1.length > 0 && pwd1.length < 8 || pwd1.length > 15) {
        msg.innerHTML = "비밀번호는 8자리 이상 20자리 이하로 입력해야 합니다."
    } else {
        msg.innerHTML = "";
    }
}

function checkPwdMatch() {
    let pwd1 = document.getElementById('pwd1').value;
    let pwd2 = document.getElementById('pwd2').value;
    let msg = document.getElementById('match-error-msg');

    if (pwd1 !== pwd2) {
        msg.innerHTML = "비밀번호가 일치하지 않습니다."
    } else {
        msg.innerHTML = "";
    }
}

function checkNameFormat(inputValue) {
    let pattern = new RegExp('^[a-zA-Z가-힣\\s]+$');
    let msg = document.getElementById('name-error-msg');

    if (inputValue.length > 0 && !inputValue.match(pattern)) {
        msg.innerHTML = "이름은 공백을 포함한 한글과 영어로만 입력 가능합니다.";
    } else {
        msg.innerHTML = "";
    }
}

function restrictToPhoneNumbers(inputValue) {
    let pattern = new RegExp('^[0-9\\\\-]+$');
    let msg = document.getElementById('mobile-number-error-msg');

    if (inputValue.length > 0 && !inputValue.match(pattern)) {
        msg.innerHTML = "휴대폰 번호는 숫자와 '-'만 입력 가능합니다.";
    } else {
        msg.innerHTML = "";
    }
}

function checkEmailFormat(inputValue) {
    let pattern = new RegExp('[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$')
    let msg = document.getElementById('email-error-msg');

    if(inputValue.length > 0 && !inputValue.match(pattern)) {
        msg.innerHTML = "이메일 형식이 올바르지 않습니다.";
    } else {
        msg.innerHTML = "";
    }
}

$(document).ready(function () {
    // 아이디 중복 확인
    $("#checkDuplicateBtn").click(function () {
        let id = $("#id").val();
        $.ajax({
            url: "/register/checkDuplicatedId",
            type: "POST",
            data: JSON.stringify({id: id}), // JSON 형식으로 데이터 직렬화
            contentType: 'application/json; charset=utf-8', // JSON 데이터 전송
            dataType: 'json', // 서버 응답을 JSON으로 받음
            success: function (response) {
                // 응답이 200인 경우
                if (response.status === "success") {
                    $("#id-msg").text(response.message) // 성공 메시지를 id-msg에 표시
                        .removeClass('duplicate-msg')
                        .addClass('success-msg'); // 성공 시 success-msg 클래스를 추가
                    $("#sendUserInfoBtn").prop("disabled", false); // 성공 시 제출 버튼 활성화
                } else {
                    console.log("Unexpected response");
                }
            },
            error: function (xhr, status, error) {
                let errorResponse = JSON.parse(xhr.responseText); // JSON 형식으로 파싱
                if (xhr.status === 400) {
                    // 400 오류가 발생한 경우 (유효성 검증 오류 등)
                    $("#id-msg").text(errorResponse.message) // id 필드에 해당하는 오류 메시지를 표시
                        .removeClass('success-msg')
                        .addClass('duplicate-msg'); // 오류 시 duplicate-msg 클래스를 추가
                    $("#sendUserInfoBtn").prop("disabled", true); // 실패 시 제출 버튼 비활성화
                } else if (xhr.status === 500) {
                    // 500 서버 오류가 발생한 경우
                    $("#id-msg").text("서버 오류가 발생했습니다. 나중에 다시 시도해주세요.") // 서버 오류 메시지를 표시
                        .removeClass('success-msg')
                        .addClass('duplicate-msg');
                    $("#sendUserInfoBtn").prop("disabled", true); // 제출 버튼 비활성화
                }
            }
        });
    });

    // 회원가입 정보 전송
    $("#sendUserInfoBtn").click(function (e) {
        e.preventDefault();

        let formData = $("form").serializeArray();
        let userDto = {};
        $.each(formData, function (i, field) {
           userDto[field.name] = field.value;
        });

        $.ajax({
           url: "/register/info",
           type: "POST",
           data: JSON.stringify(userDto),
           contentType: 'application/json; charset=utf-8',
           dataType: 'json',
           success: function (response) {
               if (response.status === "success") {
                   console.log("회원가입 성공: ", response)
                   alert(response.message);
                   window.location.href = "/login/form"; // 로그인 페이지로 이동
               } else if (response.status === "error") {
                   console.log("회원가입 실패: ", response)
                   alert(response.message);
               }
           },
           error: function (xhr, status, error) {
               let errorResponse = JSON.parse(xhr.responseText); // JSON 형식으로 파싱
               if (xhr.status === 400) {
                   console.log("회원가입 실패: ", errorResponse)
                   alert(errorResponse.message);
               } else if (xhr.status === 500) {
                   console.log("회원가입 실패: ", errorResponse)
                   alert(errorResponse.message);
               }
           }
        })

    })
});