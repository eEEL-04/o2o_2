package com.jhc.o2o.dao;

import com.jhc.o2o.entity.Product;
import com.jhc.o2o.entity.ProductCategory;
import com.jhc.o2o.entity.ProductImg;
import com.jhc.o2o.entity.Shop;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest{
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void testAInsertProduct(){
        Shop shop1 = new Shop();
        shop1.setShopId(1L);
        ProductCategory pc1 = new ProductCategory();
        pc1.setProductCategoryId(1L);

        Product product1 = new Product();
        product1.setProductName("测试1");
        product1.setProductDesc("测试Desc1");
        product1.setImgAddr("缩略图1");
        product1.setPriority(1);
        product1.setEnableStatus(1);
        product1.setCreateTime(new Date());
        product1.setLastEditTime(new Date());
        product1.setShop(shop1);
        product1.setProductCategory(pc1);
        Product product2 = new Product();
        product2.setProductName("测试2");
        product2.setProductDesc("测试Desc2");
        product2.setImgAddr("缩略图2");
        product2.setPriority(2);
        product2.setEnableStatus(1);
        product2.setCreateTime(new Date());
        product2.setLastEditTime(new Date());
        product2.setShop(shop1);
        product2.setProductCategory(pc1);
        Product product3 = new Product();
        product3.setProductName("测试3");
        product3.setProductDesc("测试Desc3");
        product3.setImgAddr("缩略图3");
        product3.setPriority(3);
        product3.setEnableStatus(1);
        product3.setCreateTime(new Date());
        product3.setLastEditTime(new Date());
        product3.setShop(shop1);
        product3.setProductCategory(pc1);

        int effectiveNum = productDao.insertProduct(product1);
        assertEquals(1,effectiveNum);
        effectiveNum = productDao.insertProduct(product2);
        assertEquals(1,effectiveNum);
        effectiveNum = productDao.insertProduct(product3);
        assertEquals(1,effectiveNum);
    }

    @Test
    public void testBQueryProductList(){
        Product productCondition = new Product();

        List<Product> productList = productDao.queryProductList(productCondition,0,3);
        assertEquals(3,productList.size());
        for (Product product : productList) {
            System.out.println(product);
        }

        int count = productDao.queryProductCount(productCondition);
        assertEquals(6,count);

        productCondition.setProductName("测试");
        productList=productDao.queryProductList(productCondition,0,2);
        assertEquals(2,productList.size());
        for (Product product : productList) {
            System.out.println(product);
        }
        count = productDao.queryProductCount(productCondition);
        assertEquals(4,count);

    }

    @Test
    public void testCQueryProductById(){
        long productId =1;
        //productId为1的商品里添加两个详情图片
        /*ProductImg productImg1 = new ProductImg();
        productImg1.setImgAddr("图片1");
        productImg1.setImgDesc("测试图片1");
        productImg1.setPriority(1);
        productImg1.setCreateTime(new Date());
        productImg1.setProductId(1L);
        ProductImg productImg2 = new ProductImg();
        productImg2.setImgAddr("图片2");
        productImg2.setImgDesc("测试图片2");
        productImg2.setPriority(2);
        productImg2.setCreateTime(new Date());
        productImg2.setProductId(1L);
        List<ProductImg> productImgList = new ArrayList<>();
        productImgList.add(productImg1);
        productImgList.add(productImg2);
        int effectNum = productImgDao.batchInsertProductImg(productImgList);
        assertEquals(2,effectNum);*/

        Product product = productDao.queryProductById(productId);
        for (ProductImg productImg : product.getProductImgList()) {
            System.out.println(productImg);
        }

        /*effectNum = productImgDao.deleteProductImgByProductId(productId);
        assertEquals(2,effectNum);*/
    }

    @Test
    public void testDUpdateProduct(){
        Product product = new Product();
        ProductCategory pc = new ProductCategory();
        Shop shop = new Shop();
        shop.setShopId(1L);
        pc.setProductCategoryId(2L);
        product.setProductId(1L);
        product.setShop(shop);
        product.setProductName("第一个商品");
        product.setProductCategory(pc);

        int effectiveNum = productDao.updateProduct(product);
        assertEquals(1,effectiveNum);

    }


    @Test
    public void testEUpdateProductCategoryToNull(){
        int effectiveNum = productDao.updateProductCategoryToNull(2L);
        assertEquals(1,effectiveNum);
    }

}
