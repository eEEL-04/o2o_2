package com.jhc.o2o.service;

import com.jhc.o2o.dto.LocalAuthExecution;
import com.jhc.o2o.entity.LocalAuth;
import com.jhc.o2o.exceptions.LocalAuthOperationException;

public interface LocalAuthService {

    /**
     * 通过账号密码获取拼台账号信息
     * @param username
     * @param password
     * @return
     */
    LocalAuth getLocalAuthByUsernameAndPwd(String username, String password);

    /**
     * 通过userId获取平台账号信息
     * @param userId
     * @return
     */
    LocalAuth getLocalAuthByUserId(long userId);


    /**
     * 绑定微信账号
     * @param localAuth
     * @return
     * @throws LocalAuthOperationException
     */
    LocalAuthExecution bindLocalAuth(LocalAuth localAuth)throws LocalAuthOperationException;

    /**
     * 修改密码
     * @param userId
     * @param username
     * @param password
     * @param newPassword
     * @return
     * @throws LocalAuthOperationException
     */
    LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) throws LocalAuthOperationException;


}
