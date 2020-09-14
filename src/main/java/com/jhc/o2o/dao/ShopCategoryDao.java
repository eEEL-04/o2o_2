package com.jhc.o2o.dao;

import com.jhc.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopCategoryDao {

    public List<ShopCategory> queryShopCategory(@Param("ShopCategoryCondition") ShopCategory shopCategoryCondition);
}
