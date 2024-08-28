package com.happydiary.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    String id; // 아이디
    String pwd; // 비밀번호
    String name; // 이름
    String email; // 이메일
    String phone_num; // 휴대폰 번호
    Integer birth; // 생년월일
    String gender; // 성별
    String address; // 주소
    String join_state; // 가입 상태
    String remark; // 비고
    String recent_login; // 최근 접속일

    // 시스템 컬럼 데이터
    String reg_id;
    String reg_date;
    String up_id;
    String up_date;

    // 회원가입용 생성자
    public UserDto(String id, String pwd, String name, String email, String phone_num, Integer birth, String gender, String address) {
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
                ", birth=" + birth +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", join_state='" + join_state + '\'' +
                ", remark='" + remark + '\'' +
                ", recent_login='" + recent_login + '\'' +
                ", reg_id='" + reg_id + '\'' +
                ", reg_date='" + reg_date + '\'' +
                ", up_id='" + up_id + '\'' +
                ", up_date='" + up_date + '\'' +
                '}';
    }
}