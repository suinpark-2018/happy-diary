package com.happydiary.dto;

import com.happydiary.common.validation.ValidationGroups;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotBlank(message = "아이디는 필수입력 항목입니다.", groups = {ValidationGroups.NotBlankGroup.class, ValidationGroups.IdCheckGroup.class, ValidationGroups.LoginCheckGroup.class})
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영문대소문자와 숫자만 입력 가능합니다.", groups = {ValidationGroups.PatternGroup.class, ValidationGroups.IdCheckGroup.class, ValidationGroups.LoginCheckGroup.class})
    @Size(min = 3, max = 20, message = "아이디는 최소 3자 이상, 20자 이하로 입력해주세요.", groups = {ValidationGroups.SizeGroup.class, ValidationGroups.IdCheckGroup.class, ValidationGroups.LoginCheckGroup.class})
    String id; // 아이디

    @NotBlank(message = "비밀번호는 필수입력 항목입니다.", groups = {ValidationGroups.NotBlankGroup.class, ValidationGroups.LoginCheckGroup.class})
    @ Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상, 최대 20자 이하로 입력해주세요.", groups = {ValidationGroups.SizeGroup.class, ValidationGroups.LoginCheckGroup.class})
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=]).+$", message = "비밀번호는 공백 없이 영문 대소문자, 숫자 최소 1개 이상 포함해야 합니다.", groups = {ValidationGroups.PatternGroup.class, ValidationGroups.LoginCheckGroup.class})
    String pwd; // 비밀번호

    @NotBlank(message = "이름은 필수입력 항목입니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Size(min = 1, max = 20, message = "이름은 1자 이상, 20자 이하로 입력해주세요.", groups = ValidationGroups.SizeGroup.class)
    @Pattern(regexp = "^[a-zA-Z가-힣\\s]+$", message = "이름은 영문과 한글만 입력 가능합니다.", groups = ValidationGroups.PatternGroup.class)
    String name; // 이름

    @NotBlank(message = "이메일은 필수입력 항목입니다.", groups = {ValidationGroups.NotBlankGroup.class, ValidationGroups.EmailCheckGroup.class})
    @Pattern(regexp = "[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.", groups = {ValidationGroups.NotBlankGroup.class, ValidationGroups.EmailCheckGroup.class})
    String email; // 이메일

    @NotBlank(message = "휴대폰번호가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Size(min = 10, max = 15, message = "휴대폰번호는 최소 10자 이상, 최대 15자 이하로 입력해주세요.", groups = ValidationGroups.SizeGroup.class)
    @Pattern(regexp = "^[0-9\\-]+$", message = "'-'과 숫자만 입력 가능합니다.", groups = ValidationGroups.PatternGroup.class)
    String phone_num; // 휴대폰 번호

    @NotBlank(message = "생년월일이 설정되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    String birth; // 생년월일

    @NotBlank(message = "생년월일이 설정되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    String gender; // 성별

    @NotBlank(message = "주소가 입력되지 않았습니다.", groups = ValidationGroups.NotBlankGroup.class)
    String address; // 주소

    String del_status; // 탈퇴여부
    String del_date;  // 탈퇴일자
    String remark;    // 비고
    String recent_login; // 최근 접속일

    // 시스템 컬럼 데이터
    String reg_id;
    String reg_date;
    String up_id;
    String up_date;

    // 회원가입용 생성자
    public UserDto(String id, String pwd, String name, String email, String phone_num, String birth, String gender, String address) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.email = email;
        this.phone_num = phone_num;
        this.birth = birth;
        this.gender = gender;
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id='" + id + '\'' +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone_num='" + phone_num + '\'' +
                ", birth='" + birth + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", del_state='" + del_status + '\'' +
                ", del_date='" + del_date + '\'' +
                ", remark='" + remark + '\'' +
                ", recent_login='" + recent_login + '\'' +
                ", reg_id='" + reg_id + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", up_id='" + up_id + '\'' +
                ", up_date='" + up_date + '\'' +
                '}';
    }
}