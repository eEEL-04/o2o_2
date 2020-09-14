package com.jhc.o2o.dao;

import com.jhc.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryDao {
    /**
     * 根据shopId查找商品类别
     * @param shopId
     * @return
     */
    public List<ProductCategory> queryProductCategoryByShopId(long shopId);

    /**
     * 批量新增商品类别
     * @param productCategoryList
     * @return
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    /**
     * 删除商品类别
     * @param productCategoryId
     * @param shopId 安全起见，只能删除本店的商品类别
     * @return
     */
    int deleteProductCategory(@Param("productCategoryId")long productCategoryId, @Param("shopId")long shopId);
}
