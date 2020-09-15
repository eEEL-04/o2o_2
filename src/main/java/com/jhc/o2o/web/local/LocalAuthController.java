package com.jhc.o2o.web.local;

import com.jhc.o2o.dto.LocalAuthExecution;
import com.jhc.o2o.entity.LocalAuth;
import com.jhc.o2o.entity.PersonInfo;
import com.jhc.o2o.enums.LocalAuthStateEnum;
import com.jhc.o2o.service.LocalAuthService;
import com.jhc.o2o.util.CodeUtil;
import com.jhc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "local", method = {RequestMethod.GET, RequestMethod.POST})
public class LocalAuthController {
    @Autowired
    private LocalAuthService localAuthService;

    /**
     * 绑定账号
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "bindlocalauth", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> bindLocalAuth(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }

        String userName = HttpServletRequestUtil.getString(request, "userName");
        String password = HttpServletRequestUtil.getString(request, "password");

        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

        if (userName != null && password != null && user != null && user.getUserId() != null) {
            LocalAuth localAuth = new LocalAuth();
            localAuth.setUsername(userName);
            localAuth.setPassword(password);
            localAuth.setPersonInfo(user);

            LocalAuthExecution lae = localAuthService.bindLocalAuth(localAuth);
            if (lae.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", lae.getStateInfo());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名和密码均不为空");
        }
        return modelMap;
    }

    /**
     * 修改密码
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "changelocalpwd", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> changeLocalPwd(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        String userName = HttpServletRequestUtil.getString(request, "userName");
        String password = HttpServletRequestUtil.getString(request, "password");
        String newPassword = HttpServletRequestUtil.getString(request, "newPassword");

        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

        if (userName != null && password != null && newPassword != null && user != null && user.getUserId() != null) {
            try {
                LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
                //非法操作
                if (localAuth == null || !localAuth.getUsername().equals(userName)) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "输入的账号非本次登录的账号");
                    return modelMap;
                }
                //修改密码
                LocalAuthExecution lae = localAuthService.modifyLocalAuth(user.getUserId(), userName, password, newPassword);
                if (lae.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", lae.getStateInfo());
                }
            }catch (Exception e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入密码");
        }
        return modelMap;
    }


    @RequestMapping(value = "logincheck", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> loginCheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取是否需要进行验证码校验的标识符
        boolean needVerify = HttpServletRequestUtil.getBoolean(request,"needVerify");
        if (needVerify && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }

        String userName = HttpServletRequestUtil.getString(request, "userName");
        String password = HttpServletRequestUtil.getString(request, "password");

        if (userName!=null && password!=null){
            LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(userName,password);
            if (localAuth!=null){
                modelMap.put("success",true);
                request.getSession().setAttribute("user",localAuth.getPersonInfo());
            }else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "用户名或密码错误");
            }
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名和密码均不能为空");
        }
        return modelMap;
    }


    /**
     * 当用户点击登出按钮时注销session
     * @param request
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> logOut(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        request.getSession().setAttribute("user",null);
        modelMap.put("success",true);
        return modelMap;
    }


}
