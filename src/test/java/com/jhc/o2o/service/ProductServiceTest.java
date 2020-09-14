package com.jhc.o2o.service;

import com.jhc.o2o.dto.ImageHolder;
import com.jhc.o2o.dto.ProductExecution;
import com.jhc.o2o.entity.Product;
import com.jhc.o2o.entity.ProductCategory;
import com.jhc.o2o.entity.Shop;
import com.jhc.o2o.enums.ProductStateEnum;
import com.jhc.o2o.exceptions.ShopOperationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Test
    public void testAddProduct() throws FileNotFoundException {
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(1L);

        product.setShop(shop);
        product.setProductCategory(pc);
        product.setProductName("测试商品1");
        product.setProductDesc("测试商品1");
        product.setPriority(20);
        product.setCreateTime(new Date());
        product.setEnableStatus(ProductStateEnum.SUCCESS.getState());

        //创建缩略图文件流
        File thumbnailFile = new File("E:/image/xiaohuangren.jpg");
        InputStream is = new FileInputStream(thumbnailFile);
        ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(),is);
        //创建两个商品详情图文件流并将他们添加到详情图列表中
        List<ImageHolder> productImgList = new ArrayList<>();
        File productImg1 = new File("E:/image/xiaohuangren.jpg");
        InputStream is1 = new FileInputStream(productImg1);
        File productImg2 = new File("E:/image/dabai.jpg");
        InputStream is2 = new FileInputStream(productImg2);
        productImgList.add(new ImageHolder(productImg1.getName(),is1));
        productImgList.add(new ImageHolder(productImg2.getName(),is2));

        ProductExecution pe = productService.addProduct(product,thumbnail,productImgList);
        assertEquals(ProductStateEnum.SUCCESS.getState(),pe.getState());
    }

    @Test
    public void testModifyProduct() throws ShopOperationException, FileNotFoundException {
        Product product = new Product();
        Shop shop=new Shop();
        shop.setShopId(1L);
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(1L);
        product.setProductId(2L);
        product.setShop(shop);
        product.setProductCategory(pc);
        product.setShop(shop);
        product.setProductName("正式商品");
        product.setProductDesc("正式商品");

        File thumbnailFile = new File("E:/image/xiang.jpg");
        InputStream is = new FileInputStream(thumbnailFile);
        ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(),is);

        File productImg1 = new File("E:/image/qie.jpg");
        InputStream is1 = new FileInputStream(productImg1);
        File productImg2 = new File("E:/image/dabai.jpg");
        InputStream is2 = new FileInputStream(productImg2);

        List<ImageHolder> productImgList = new ArrayList<>();
        productImgList.add(new ImageHolder(productImg1.getName(),is1));
        productImgList.add(new ImageHolder(productImg2.getName(),is2));

        ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
        assertEquals(ProductStateEnum.SUCCESS.getState(),pe.getState());


    }

}
