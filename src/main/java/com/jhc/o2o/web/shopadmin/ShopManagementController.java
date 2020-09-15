package com.jhc.o2o.web.shopadmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhc.o2o.dto.ImageHolder;
import com.jhc.o2o.dto.ShopExecution;
import com.jhc.o2o.entity.Area;
import com.jhc.o2o.entity.PersonInfo;
import com.jhc.o2o.entity.Shop;
import com.jhc.o2o.entity.ShopCategory;
import com.jhc.o2o.enums.ShopStateEnum;
import com.jhc.o2o.exceptions.ShopOperationException;
import com.jhc.o2o.service.AreaService;
import com.jhc.o2o.service.ShopCategoryService;
import com.jhc.o2o.service.ShopService;
import com.jhc.o2o.util.CodeUtil;
import com.jhc.o2o.util.HttpServletRequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("shopadmin")
public class ShopManagementController {
    private final ShopCategoryService shopCategoryService;
    private final ShopService shopService;
    private final AreaService areaService;

    public ShopManagementController(ShopService shopService, ShopCategoryService shopCategoryService, AreaService areaService) {
        this.shopService = shopService;
        this.shopCategoryService = shopCategoryService;
        this.areaService = areaService;
    }

    @RequestMapping(value = "getshopmanagementinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopManagementInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        if (shopId<=0){
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if (currentShopObj==null){
                modelMap.put("redirect",true);
                modelMap.put("url","/shopadmin/shoplist");
            }else {
                Shop currentShop = (Shop) currentShopObj;
                modelMap.put("redirect",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        }else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("redirect",false);
        }
        return modelMap;
    }

    @RequestMapping(value = "getshoplist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopList(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();;
        PersonInfo user =new PersonInfo();
        /*user.setUserId(1L);
        user.setName("test1");
        request.getSession().setAttribute("user",user);*/
        user = (PersonInfo) request.getSession().getAttribute("user");

        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution se = shopService.getShopList(shopCondition,0,100);

            modelMap.put("shopList",se.getShopList());
            //列出店铺成功后，将店铺放入session中作为权限验证器，即该账号只能操作他的店铺
            request.getSession().setAttribute("shopList",se.getShopList());
            modelMap.put("user",user);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "getshopbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            try {
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList = new ArrayList<>();
        List<Area> areaList = new ArrayList<>();
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);

        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }


    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request) {
        System.out.println("这是注册店铺方法");
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }

        //接收并转化相应的参数，包括店铺信息及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        ImageHolder imageHolder = null;
        try {
            if (commonsMultipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
                imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传图片不能为空");
                return modelMap;
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }


        //注册店铺
        if (shop != null && imageHolder != null) {
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);


            ShopExecution se = null;
            try {
                //调用方法
                se = shopService.addShop(shop, imageHolder);
                if (se.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if (shopList == null || shopList.size() == 0) {
                        shopList = new ArrayList<>();
                    }
                    shopList.add(se.getShop());
                    request.getSession().setAttribute("shopList", shopList);

                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
        }
        //返回结果
        return modelMap;
    }

    @RequestMapping(value = "modifyshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }

        //接收并转化相应的参数，包括店铺信息及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        ImageHolder imageHolder = null;
        try {
            if (commonsMultipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
                imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
            }
        }catch (Exception e){
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }


        //修改店铺信息
        if (shop != null && shop.getShopId() != null) {
            ShopExecution se;
            try {
                if (imageHolder == null) {
                    se = shopService.modifyShop(shop, null);
                } else {
                    se = shopService.modifyShop(shop, imageHolder);
                }
                if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺id");
        }
        //返回结果
        return modelMap;
    }


    /*private static void inputStreamToFile(InputStream is, File file) {
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = is.read(buffer))!=-1){
                os.write(buffer,0,bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException("调用inputStreamToFile产生异常："+e.getMessage());
        }finally {
            try {
                if (os!=null){
                    os.close();
                }
                if (is!=null){
                    is.close();
                }
            }catch (IOException e){
                throw new RuntimeException("inputStreamToFile关闭IO产生异常："+e.getMessage());
            }
        }
    }*/


}
