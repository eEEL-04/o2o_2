package com.jhc.o2o.dao;


import com.jhc.o2o.entity.LocalAuth;
import com.jhc.o2o.entity.PersonInfo;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthDaoTest {
    @Autowired
    private LocalAuthDao localAuthDao;

    private final String username = "testbind";
    private final String password = "testbind";

    @Test
    public void testAInsertLocalAuth(){
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1L);

        localAuth.setPersonInfo(personInfo);
        localAuth.setUsername(username);
        localAuth.setPassword(password);
        localAuth.setCreateTime(new Date());
        localAuth.setLastEditTime(new Date());
        int effectedNum = localAuthDao.insertLocalAuth(localAuth);
        assertEquals(1,effectedNum);
    }

    @Test
    public void testBQueryLocalByUserNameAndPwd(){
        LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(username, password);
        System.out.println(localAuth);
    }

    @Test
    public void testCQueryLocalByUserId(){
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
        System.out.println(localAuth);
    }

    @Test
    public void testDUpdateLocalAuth(){
        int effectiveNum = localAuthDao.updateLocalAuth(1L,username,password,"123",new Date());
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
        System.out.println(localAuth);
    }

}
