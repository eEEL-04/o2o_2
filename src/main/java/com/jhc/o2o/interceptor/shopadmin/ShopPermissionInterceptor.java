package com.jhc.o2o.interceptor.shopadmin;

import com.jhc.o2o.entity.Shop;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 店家管理系统操作验证拦截器
 */
public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //当前选择的店铺
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //从session中获取当前用户可操作的店铺列表
        List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");

        if (currentShop!=null && shopList!=null){
            for (Shop shop : shopList) {
                if (shop.getShopId()==currentShop.getShopId()){
                    return true;
                }
            }
        }
        //若不满足拦截器的验证则返回false,终止用户操作
        return false;
    }
}
