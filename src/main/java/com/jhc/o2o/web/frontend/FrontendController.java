package com.jhc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {

    /**
     * 首页
     * @return
     */
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    private String index(){
        return "frontend/index";
    }

    /**
     * 店铺列表
     * @return
     */
    @RequestMapping(value = "shoplist",method = RequestMethod.GET)
    private String shopList(){
        return "frontend/shoplist";
    }

    /**
     * 店铺详情
     * @return
     */
    @RequestMapping(value = "shopdetail",method = RequestMethod.GET)
    private String showShopDetail() {
        return "frontend/shopdetail";
    }

    /**
     * 商品详情
     * @return
     */
    @RequestMapping(value = "productdetail",method = RequestMethod.GET)
    private String showProductDetail() {
        return "frontend/productdetail";
    }
}
