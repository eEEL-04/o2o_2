package com.jhc.o2o.interceptor.shopadmin;

import com.jhc.o2o.entity.PersonInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 店家管理系统登录验证拦截器
 */
public class ShopLoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 事前拦截
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object userObj = request.getSession().getAttribute("user");
        if (userObj != null) {
            //若用户信息不为空
            PersonInfo user = (PersonInfo) userObj;
            //空值判断
            if (user != null && user.getUserId() != null && user.getUserId() > 0 && user.getEnableStatus() == 1 && user.getUserType()==2) {
                return true;
            }
        }
        // 若不满足登录验证，则直接跳转到帐号登录页面
        response.sendRedirect("/local/login?usertype=2");
        return false;
    }
}
