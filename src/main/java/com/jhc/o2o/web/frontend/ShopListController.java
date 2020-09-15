package com.jhc.o2o.web.frontend;

import com.jhc.o2o.dto.ShopExecution;
import com.jhc.o2o.entity.Area;
import com.jhc.o2o.entity.Shop;
import com.jhc.o2o.entity.ShopCategory;
import com.jhc.o2o.service.AreaService;
import com.jhc.o2o.service.ShopCategoryService;
import com.jhc.o2o.service.ShopService;
import com.jhc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("frontend")
public class ShopListController {
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ShopService shopService;


    /**
     * 发挥商店列表里的ShopCategory列表（二级或一级），一级区域信息列表
     * @param request
     * @return
     */
    @RequestMapping(value = "listshopspageinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShopsPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();

        long parentId = HttpServletRequestUtil.getLong(request,"parentId");
        List<ShopCategory> shopCategoryList = null;
        if (parentId!=-1){
            //如果parentId存在，则取出该一级目录下的二级商店类别列表
            try {
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryCondition.setParent(parent);
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }else {
            //如果parentId不存在，则取出所有一级SHopCategory（用户在首页选择的是全部商店列表）
            try {
                shopCategoryList = shopCategoryService.getShopCategoryList(null);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }
        modelMap.put("shopCategoryList",shopCategoryList);
        List<Area> areaList = null;
        try {
            //获取区域列表信息
            areaList = areaService.getAreaList();
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }


    /**
     * 获取指定查询条件下的店铺列表
     * @param request
     * @return
     */
    @RequestMapping(value = "listshops",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShops(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();

        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");

        if (pageIndex>-1 && pageSize>-1){
            //尝试获取一级类别id
            long parentId = HttpServletRequestUtil.getLong(request,"parentId");
            //尝试获取特定二级类别id
            long shopCategoryId = HttpServletRequestUtil.getInt(request,"shopCategoryId");
            //尝试获取区域id
            int areaId = HttpServletRequestUtil.getInt(request,"areaId");
            //尝试获取模糊查询的名字
            String shopName = HttpServletRequestUtil.getString(request,"shopName");
            //结合查询条件
            Shop shopCondition = compactShopCondition4Search(parentId,shopCategoryId,areaId,shopName);
            ShopExecution se = shopService.getShopList(shopCondition,pageIndex,pageSize);
            modelMap.put("shopList",se.getShopList());
            modelMap.put("count",se.getCount());
            modelMap.put("success",true);
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageSize or pageIndex");
        }
        return modelMap;

    }

    /**
     * 组合查询条件
     * @param parentId
     * @param shopCategoryId
     * @param areaId
     * @param shopName
     * @return
     */
    private Shop compactShopCondition4Search(long parentId, long shopCategoryId, int areaId, String shopName) {
        Shop shopCondition = new Shop();
        if (parentId!=-1L){
            //查询某个一级ShopCategory下面所有二级ShopCategory里面的店铺列表
            ShopCategory childCategory = new ShopCategory();
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        if (shopCategoryId!=-1L){
            //查询某个二级ShopCategory下面的店铺列表
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if (areaId!=-1){
            Area area =new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        if (shopName!=null){
            shopCondition.setShopName(shopName);
        }
        //前端展示的都是审核成功的列表
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }
}
