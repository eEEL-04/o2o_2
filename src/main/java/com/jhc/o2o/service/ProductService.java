package com.jhc.o2o.service;

import com.jhc.o2o.dto.ImageHolder;
import com.jhc.o2o.dto.ProductExecution;
import com.jhc.o2o.entity.Product;
import com.jhc.o2o.exceptions.ProductOperationException;

import java.util.List;

public interface ProductService {

    /**
     * 查询商品列表并分页，可输入商品名（模糊），商品状态，店铺id，商品类别
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

    /**
     * 添加商品
     * @param product
     * @param thumbnail 缩略图
     * @param productImgList 详情图list
     * @return
     * @throws ProductOperationException
     */
    ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException;

    /**
     * 根据商品Id查询唯一商品
     * @param productId
     * @return
     */
    Product getProductById(long productId);

    /**
     * 修改商品信息
     * @param product
     * @param thumbnail
     * @param productImgList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)throws ProductOperationException;
}
