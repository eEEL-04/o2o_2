package com.jhc.o2o.service;

import com.jhc.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {

    public static final String SCLISTKEY = "shopcategorylist";
    /**
     * 根据查询条件获取 商铺类别 列表
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
