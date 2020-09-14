package com.jhc.o2o.service;

import com.jhc.o2o.dto.LocalAuthExecution;
import com.jhc.o2o.entity.LocalAuth;
import com.jhc.o2o.entity.PersonInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthServiceTest {
    @Autowired
    private LocalAuthService localAuthService;

    @Test
    public void testBindLocalAuth(){
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        String username = "testusername1";
        String password = "testpassword1";

        personInfo.setUserId(1L);
        localAuth.setPersonInfo(personInfo);
        localAuth.setUsername(username);
        localAuth.setPassword(password);

        LocalAuthExecution lae = localAuthService.bindLocalAuth(localAuth);
        System.out.println(lae.getLocalAuth());

        localAuth = localAuthService.getLocalAuthByUserId(personInfo.getUserId());
        System.out.println(localAuth);
    }

    @Test
    public void testModifyLocalAuth(){
        long userId = 1L;
        String username = "testusername1";
        String password = "testpassword1";
        String newPassword = "testnewpassword1";

        localAuthService.modifyLocalAuth(userId,username,password,newPassword);

        LocalAuth localAuth = localAuthService.getLocalAuthByUserId(2L);
        System.out.println(localAuth);
    }

    @Test
    public void testGetLocalAuthByUsernameAndPwd(){
        LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd("testbind", "testbind");
        System.out.println(localAuth);
        System.out.println(localAuth.getPersonInfo());
    }
}
