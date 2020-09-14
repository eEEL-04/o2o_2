package com.jhc.o2o.dao;

import com.jhc.o2o.entity.ProductCategory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Test
    public void testBQueryProductCategoryByShopId(){
        long shopId = 1L;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryByShopId(shopId);
        for (ProductCategory productCategory : productCategoryList) {
            System.out.println(productCategory);
        }
    }


    @Test
    public void testABatchInsertProductCategory(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryName("商品类别1");
        productCategory.setPriority(1);
        productCategory.setCreateTime(new Date());
        productCategory.setShopId(1L);
        ProductCategory productCategory2 = new ProductCategory();
        productCategory2.setProductCategoryName("商品类别2");
        productCategory2.setPriority(2);
        productCategory2.setCreateTime(new Date());
        productCategory2.setShopId(1L);
        List<ProductCategory> productCategoryList = new ArrayList<>();
        productCategoryList.add(productCategory);
        productCategoryList.add(productCategory2);
        int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
        assertEquals(2,effectedNum);
    }

    @Test
    public void testCDeleteProductCategory(){
        long shopId = 1L;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryByShopId(shopId);
        for (ProductCategory productCategory : productCategoryList) {
            if ("商品类别1".equals(productCategory.getProductCategoryName())||"商品类别2".equals(productCategory.getProductCategoryName())){
                int effectiveNum = productCategoryDao.deleteProductCategory(productCategory.getProductCategoryId(),shopId);
                assertEquals(1,effectiveNum);
            }
        }
    }


}
