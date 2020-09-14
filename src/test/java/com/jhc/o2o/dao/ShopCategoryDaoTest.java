package com.jhc.o2o.dao;

import com.jhc.o2o.entity.ShopCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopCategoryDaoTest {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void testQueryShopCategory(){
        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(null);
        for (ShopCategory shopCategory : shopCategoryList) {
            System.out.println(shopCategory);
        }
    }

    @Test
    public void testQueryShopCategory2(){
        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(new ShopCategory());
        assertEquals(12,shopCategoryList.size());
        for (ShopCategory shopCategory : shopCategoryList) {
            System.out.println(shopCategory);
        }
    }

    @Test
    public void testQueryShopCategory3(){
        ShopCategory testCategory = new ShopCategory();
        ShopCategory parentCategory = new ShopCategory();
        parentCategory.setShopCategoryId(3L);
        testCategory.setParent(parentCategory);
        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(testCategory);
        assertEquals(2,shopCategoryList.size());
        for (ShopCategory shopCategory : shopCategoryList) {
            System.out.println(shopCategory);
        }
    }

}
