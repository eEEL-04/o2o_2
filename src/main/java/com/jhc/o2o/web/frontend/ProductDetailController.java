package com.jhc.o2o.web.frontend;

import com.jhc.o2o.entity.Product;
import com.jhc.o2o.service.ProductService;
import com.jhc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("frontend")
public class ProductDetailController {
    @Autowired
    private ProductService productService;

    /**
     * 根据商品Id获取商品详情
     * @param request
     * @return
     */
    @RequestMapping(value = "listproductdetailpageinfo")
    @ResponseBody
    private Map<String,Object> listProductDetailPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        long productId = HttpServletRequestUtil.getLong(request,"productId");
        Product product = null;
        if (productId!=-1L){
            product=productService.getProductById(productId);
            modelMap.put("product",product);
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty productId");
        }
        return modelMap;
    }
}
