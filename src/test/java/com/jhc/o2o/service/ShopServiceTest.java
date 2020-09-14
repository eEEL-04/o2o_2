package com.jhc.o2o.service;

import com.jhc.o2o.dto.ImageHolder;
import com.jhc.o2o.dto.ShopExecution;
import com.jhc.o2o.entity.Area;
import com.jhc.o2o.entity.PersonInfo;
import com.jhc.o2o.entity.Shop;
import com.jhc.o2o.entity.ShopCategory;
import com.jhc.o2o.enums.ShopStateEnum;
import com.jhc.o2o.exceptions.ShopOperationException;
import com.jhc.o2o.util.ImageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopServiceTest {

    @Autowired
    private ShopService shopService;

    @Test
    public void testGetShopList(){
        Shop shopCondition = new Shop();
        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(1L);
        shopCondition.setShopCategory(sc);
        ShopExecution se = shopService.getShopList(shopCondition, 2, 2);
        for (Shop shop : se.getShopList()) {
            System.out.println(shop);
        }
        System.out.println("查询总数："+se.getCount());
    }

    @Test
    public void testModifyShop() throws ShopOperationException,FileNotFoundException{
        Shop shop = new Shop();
        shop.setShopId(7L);
        shop.setShopName("修改后的店铺名称");
        File shopImg = new File("E:/image/dabai.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder imageHolder = new ImageHolder(shopImg.getName(),is);
        ShopExecution shopExecution = shopService.modifyShop(shop, imageHolder);
        System.out.println(shopExecution.getShop());
    }

    @Test
    public void testAddShop() throws FileNotFoundException {

        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();

        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(1L);

        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);

        shop.setShopName("测试店铺5");
        shop.setShopDesc("test5");
        shop.setShopAddr("test5");
        shop.setPhone("test5");

        shop.setAdvice("审核中");

        File shopImg = new File("E:/image/xiaohuangren.jpg");

        InputStream is = new FileInputStream(shopImg);

        ImageHolder imageHolder = new ImageHolder(shopImg.getName(),is);
        ShopExecution shopExecution = shopService.addShop(shop, imageHolder);

        assertEquals(ShopStateEnum.CHECK.getState(),shopExecution.getState());

    }

    @Test
    public void testBasePath(){
        ImageUtil.printBasePath();
    }
}
