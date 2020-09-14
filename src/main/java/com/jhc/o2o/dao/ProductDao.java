package com.jhc.o2o.dao;

import com.jhc.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {
    /**
     * 查询商品列表并分页，可输入商品名（模糊），商品状态，店铺id，商品类别
     * @param productCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<Product> queryProductList(@Param("productCondition") Product productCondition, @Param("rowIndex")int rowIndex, @Param("pageSize")int pageSize);

    /**
     * 查询对应商品的总数
     * @param productCondition
     * @return
     */
    int queryProductCount(@Param("productCondition") Product productCondition);

    /**
     * 根据商品id查询唯一商品
     * @param productId
     * @return
     */
    Product queryProductById(long productId);

    /**
     * 插入商品
     * @param product
     * @return
     */
    int insertProduct(Product product);

    /**
     * 更新商品
     * @param product
     * @return
     */
    int updateProduct(Product product);

    /**
     * 删除商品类别之前，将商品类别置为空
     * @param productCategoryId
     * @return
     */
    int updateProductCategoryToNull(long productCategoryId);
}
