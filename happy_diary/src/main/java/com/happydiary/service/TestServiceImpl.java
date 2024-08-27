package com.happydiary.service;

import com.happydiary.dao.TestDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired TestDaoImpl testDao;

    @Override
    public String now() {
        String testResult = "";
        try {
            testResult = testDao.now();
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return testResult;
    }
}
