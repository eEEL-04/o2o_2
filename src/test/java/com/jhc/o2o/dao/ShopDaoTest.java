package com.jhc.o2o.dao;

import com.jhc.o2o.entity.Area;
import com.jhc.o2o.entity.PersonInfo;
import com.jhc.o2o.entity.Shop;
import com.jhc.o2o.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopDaoTest {
    @Autowired
    private ShopDao shopDao;

    @Test
    public void testQueryShopList(){
        Shop shopCondition = new Shop();
        ShopCategory childCategory = new ShopCategory();
        ShopCategory parentCategory = new ShopCategory();
        parentCategory.setShopCategoryId(5L);
        childCategory.setParent(parentCategory);
        shopCondition.setShopCategory(childCategory);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 6);
        int shopCount = shopDao.queryShopCount(shopCondition);
        for (Shop shop : shopList) {
            System.out.println(shop);
        }
        System.out.println("查询总数："+shopCount);
        System.out.println("====================");
    }


    @Test
    @Ignore
    public void testQueryByShopId(){
        long shopId=1;
        Shop shop = shopDao.queryByShopId(shopId);
        System.out.println(shop);
    }

    @Test
    @Ignore
    public void testInsertShop(){
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();

        owner.setUserId(1L);
        area.setAreaId(1);
        shopCategory.setShopCategoryId(1L);

        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);

        shop.setShopName("测试店铺4");
        shop.setShopDesc("test4");
        shop.setShopAddr("test4");
        shop.setPhone("test4");
        shop.setShopImg("test4");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");

        int effectedNum = shopDao.insertShop(shop);
        assertEquals(1,effectedNum);

    }

    @Test
    @Ignore
    public void testUpdateShop(){
        Shop shop = new Shop();

        shop.setShopId(1L);
        shop.setShopName("测试店铺");
        shop.setShopDesc("测试描述");
        shop.setShopAddr("测试地址");
        Date date = new Date();
        shop.setLastEditTime(date);
        System.out.println(date);

        int effectedNum = shopDao.updateShop(shop);
        assertEquals(1,effectedNum);

    }
}
