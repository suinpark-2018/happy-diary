package com.happydiary.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TestDaoImpl implements TestDao{

    @Autowired SqlSession session;

    private static String namespace = "com.happydiary.dao.TestDao.";

    @Override
    public String now() throws Exception {
        return session.selectOne(namespace + "now");
    }
}