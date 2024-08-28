package com.happydiary.dao;

import com.happydiary.dto.UserDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired SqlSession session;

    private static String namespace = "com.happydiary.dao.UserDao.";

    // 현재 일시(년월일, 시분초) 반환
    @Override
    public String now() throws Exception {
        return session.selectOne(namespace + "now");
    }

    // 사용자 수
    @Override
    public int count() throws Exception {
        return session.selectOne(namespace + "count");
    }

    // 1. 회원가입
    // 2. 로그인
    // 3. 회원 정보 수정
    // 4. 회원 정보 삭제

    // 1.1. 회원 정보 입력
    @Override
    public int insert(UserDto userDto) throws Exception {
        return session.insert(namespace + "insert", userDto);
    }

    // 1.2. 특정 회원 정보 조회
    // 1.2.1. 아이디로 조회
    @Override
    public UserDto select(String id) throws Exception {
        return session.selectOne(namespace + "select", id);
    }
    // 1.2.2. 이메일로 조회
    @Override
    public UserDto selectByEmail(String email) throws Exception {
        return session.selectOne(namespace + "selectByEmail", email);
    }

    // 1.2. 전체 회원 정보 조회
    @Override
    public List<UserDto> selectAll() throws Exception {
        return session.selectList(namespace + "selectAll");
    }

    // 3.1. 특정 회원 정보 수정
    @Override
    public int update(UserDto userDto) throws Exception {
        return session.update(namespace + "update", userDto);
    }

    // 4.1. 특정 회원 정보 삭제
    @Override
    public int delete(String id) throws Exception {
        return session.delete(namespace + "delete", id);
    }

    // 4.2. 전체 회원 정보 삭제
    @Override
    public int deleteAll() throws Exception {
        return session.delete(namespace + "deleteAll");
    }
}
