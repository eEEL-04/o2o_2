package com.jhc.o2o.service.impl;

import com.jhc.o2o.dao.LocalAuthDao;
import com.jhc.o2o.dto.LocalAuthExecution;
import com.jhc.o2o.entity.LocalAuth;
import com.jhc.o2o.enums.LocalAuthStateEnum;
import com.jhc.o2o.exceptions.LocalAuthOperationException;
import com.jhc.o2o.service.LocalAuthService;
import com.jhc.o2o.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {
    @Autowired
    private LocalAuthDao localAuthDao;

    @Override
    public LocalAuth getLocalAuthByUsernameAndPwd(String username, String password) {
        return localAuthDao.queryLocalByUserNameAndPwd(username, MD5.getMD5(password));
    }

    @Override
    public LocalAuth getLocalAuthByUserId(long userId) {
        return localAuthDao.queryLocalByUserId(userId);
    }

    @Override
    @Transactional
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
        if (localAuth==null || localAuth.getPassword()==null || localAuth.getUsername()==null ||
        localAuth.getPersonInfo()==null || localAuth.getPersonInfo().getUserId()==null){
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
        if (tempAuth!=null){
            //绑定过则直接退出，保证平台账号的唯一性
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_COUNT);
        }
        try {
            localAuth.setCreateTime(new Date());
            localAuth.setLastEditTime(new Date());

            localAuth.setPassword(MD5.getMD5(localAuth.getPassword()));
            int effectiveNum = localAuthDao.insertLocalAuth(localAuth);

            //判断是否创建是成功
            if (effectiveNum<=0){
                throw new LocalAuthOperationException("账号绑定失败");
            }else{
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,localAuth);
            }
        }catch (Exception e){
            throw new LocalAuthOperationException("insertLocalAuth error:"+e.getMessage());
        }
    }

    @Override
    @Transactional
    public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) throws LocalAuthOperationException {
        if (userId!=null && username!=null && password!=null && newPassword!=null &&!password.equals(newPassword)){
            try {
                //更新密码
                int effective = localAuthDao.updateLocalAuth(userId,username,MD5.getMD5(password),MD5.getMD5(newPassword),new Date());
                if (effective<=0){
                    throw new LocalAuthOperationException("更新密码失败");
                }
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            }catch (Exception e){
                throw new LocalAuthOperationException("更新密码失败:"+e.getMessage());
            }
        }else {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
    }
}
