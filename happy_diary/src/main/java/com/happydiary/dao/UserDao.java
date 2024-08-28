package com.happydiary.dao;

import com.happydiary.dto.UserDto;

import java.util.List;

public interface UserDao {
    // 현재 일시(년월일, 시분초) 반환
    String now() throws Exception;

    // 회원 수
    int count() throws Exception;

    // 1.1. 회원 정보 입력
    int insert(UserDto userDto) throws Exception;

    // 1.2. 특정 회원 정보 조회
    // 1.2.1. 아이디로 조회
    UserDto select(String id) throws Exception;

    // 1.2.2. 이메일로 조회
    UserDto selectByEmail(String email) throws Exception;

    // 1.2. 전체 회원 정보 조회
    List<UserDto> selectAll() throws Exception;

    // 3.1. 특정 회원 정보 수정
    int update(UserDto userDto) throws Exception;

    // 4.1. 특정 회원 정보 삭제
    int delete(String id) throws Exception;

    // 4.2. 전체 회원 정보 삭제
    int deleteAll() throws Exception;
}
