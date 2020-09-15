package com.jhc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhc.o2o.dto.ImageHolder;
import com.jhc.o2o.dto.ProductExecution;
import com.jhc.o2o.entity.Product;
import com.jhc.o2o.entity.ProductCategory;
import com.jhc.o2o.entity.Shop;
import com.jhc.o2o.enums.ProductStateEnum;
import com.jhc.o2o.exceptions.ProductOperationException;
import com.jhc.o2o.service.ProductCategoryService;
import com.jhc.o2o.service.ProductService;
import com.jhc.o2o.util.CodeUtil;
import com.jhc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("shopadmin")
public class ProductManagementController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    //支持上传商品详情图的最大数量
    private static final int IMAGEMAXCOUNT = 6;

    /**
     * 通过店铺id获取该店铺下的商品列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getproductlistbyshop")
    @ResponseBody
    private Map<String, Object> getProductListByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        if (pageIndex > -1 && pageSize > -1 && currentShop != null && currentShop.getShopId() != null) {
            long productCategoryId = HttpServletRequestUtil.getLong(request,"productCategoryId");
            String productName =  HttpServletRequestUtil.getString(request,"productName");
            Product productCondition = compactProductCondition(currentShop.getShopId(),productCategoryId,productName);

            ProductExecution pe = productService.getProductList(productCondition,pageIndex,pageSize);

            modelMap.put("productList", pe.getProductList());
            modelMap.put("count", pe.getCount());
            modelMap.put("success", true);
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageIndex or pageSize or shopId");
        }
        return modelMap;
    }

    private Product compactProductCondition(Long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop= new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);

        if (productCategoryId!=-1L){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }

        if (productName!=null){
            productCondition.setProductName(productName);
        }
        return productCondition;
    }

    /**
     * 根据商品id查询唯一商品
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "getproductbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getProductById(@RequestParam Long productId) {
        Map<String, Object> modelMap = new HashMap<>();

        if (productId > -1) {
            Product product = productService.getProductById(productId);
            //该店铺的商品类别列表
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
            modelMap.put("product", product);
            modelMap.put("productCategoryList", productCategoryList);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty productId");
        }
        return modelMap;
    }

    /**
     * 添加商品
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "addproduct", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //验证码
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        //接收前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类列表实体类
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        try {
            //若请求中存在文件流
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImage((MultipartHttpServletRequest) request, thumbnail, productImgList);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传图片不能为空");
                return modelMap;
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        try {
            String productStr = HttpServletRequestUtil.getString(request, "productStr");
            //尝试获取前端传过来的表单string流，并将其转换成Product实体类
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        if (product != null && thumbnail != null && productImgList.size() > 0) {
            try {
                //从session中获取当前店铺并赋值给product，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                Shop shop = new Shop();
                shop.setShopId(currentShop.getShopId());
                product.setShop(shop);

                ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);

                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                    return modelMap;
                }
            } catch (ProductOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;
    }

    /**
     * 编辑商品
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "modifyproduct", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //是商品编辑时候调用，还是上下架的时候调用
        //若为前者则进行验证码判断，否则不进行
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        //接收前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类列表实体类
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        if(!statusChange){
            try {
                //若请求中存在文件流
                if (multipartResolver.isMultipart(request)) {
                    thumbnail = handleImage((MultipartHttpServletRequest) request, thumbnail, productImgList);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "上传图片不能为空");
                    return modelMap;
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        }


        try {
            String productStr = HttpServletRequestUtil.getString(request, "productStr");
            //尝试获取前端传过来的表单string流，并将其转换成Product实体类
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        if (product != null) {
            try {
                //从session中获取当前店铺并赋值给product，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                Shop shop = new Shop();
                shop.setShopId(currentShop.getShopId());
                product.setShop(shop);

                ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);

                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                    return modelMap;
                }
            } catch (ProductOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;
    }

    private ImageHolder handleImage(MultipartHttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImgList) throws IOException {
        MultipartHttpServletRequest multipartRequest = request;
        //取出缩略图
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        if (thumbnailFile != null) {
            thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
        }
        //取出详情图列表
        for (int i = 0; i < IMAGEMAXCOUNT; i++) {
            CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
            if (productImgFile != null) {
                ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(), productImgFile.getInputStream());
                productImgList.add(productImg);
            } else {
                break;
            }
        }
        return thumbnail;
    }
}
