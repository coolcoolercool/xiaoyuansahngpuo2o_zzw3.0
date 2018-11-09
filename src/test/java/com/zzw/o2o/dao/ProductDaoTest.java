package com.zzw.o2o.dao;


import com.zzw.o2o.entity.Product;
import com.zzw.o2o.entity.ProductCategory;
import com.zzw.o2o.entity.Shop;
import org.junit.Assert;
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

/**
 * author: zzw5005
 * date: 2018/10/29 21:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    public void testAinsertProduct() {

        //注意表中的外键关系，确保这些数据在对应的表中的存在
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(11L);

        //注意表中的外键关系，确保这些数据在对应的表中的存在
        Shop shop = new Shop();
        shop.setShopId(20L);

        //初始化三个商品实例并添加进shopId为1的店铺
        Product product = new Product();
        product.setProductName("测试1");
        product.setProductDesc("测试1Desc1");
        product.setImgAddr("/aaa/bbb");
        product.setNormalPrice("10");
        product.setPromotionPrice("8");
        product.setPriority(34);
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
        product.setEnableStatus(1);

        product.setProductCategory(productCategory);
        product.setShop(shop);

        Product product1 = new Product();
        product1.setProductName("测试2");
        product1.setProductDesc("测试2Desc2");
        product1.setImgAddr("/aaa/bbb");
        product1.setNormalPrice("10");
        product1.setPromotionPrice("8");
        product1.setPriority(34);
        product1.setCreateTime(new Date());
        product1.setLastEditTime(new Date());
        product1.setEnableStatus(0);
        product1.setProductCategory(productCategory);
        product1.setShop(shop);

        Product product2 = new Product();
        product2.setProductName("测试2");
        product2.setProductDesc("测试2Desc2");
        product2.setImgAddr("/aaa/bbb");
        product2.setNormalPrice("10");
        product2.setPromotionPrice("8");
        product2.setPriority(34);
        product2.setCreateTime(new Date());
        product2.setLastEditTime(new Date());
        product2.setEnableStatus(0);
        product2.setProductCategory(productCategory);
        product2.setShop(shop);

        //判断是否添加成功
        int effectNum = productDao.insertProduct(product);
        Assert.assertEquals(1, effectNum);

        int effectNum1 = productDao.insertProduct(product1);
        Assert.assertEquals(1, effectNum1);

        int effectNum2 = productDao.insertProduct(product2);
        Assert.assertEquals(1, effectNum2);
    }



    @Test
    public void testBupdateProduct() {
        // 注意表中的外键关系，确保这些数据在对应的表中的存在
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(11L);

        // 注意表中的外键关系，确保这些数据在对应的表中的存在
        Shop shop = new Shop();
        shop.setShopId(20L);

        Product product = new Product();
        product.setProductName("modifyProduct");
        product.setProductDesc("modifyProduct desc");
        product.setImgAddr("/mmm/dd");
        product.setNormalPrice("35");
        product.setPromotionPrice("30");
        product.setPriority(60);
        product.setPoint(3);
        product.setLastEditTime(new Date());
        product.setEnableStatus(1);

        product.setProductCategory(productCategory);
        product.setShop(shop);

        // 设置productId
        product.setProductId(16L);

        int effectNum = productDao.updateProduct(product);
        Assert.assertEquals(1, effectNum);

    }

    @Test
    public void selectProductListAndCount() {
        int rowIndex = 0;  //这个索引是从0开始的
        int pageSize = 3;
        List<Product> productList = new ArrayList<Product>();
        int effectNum = 0;

        Shop shop = new Shop();
        shop.setShopId(15L);

        Product productCondition = new Product();
        productCondition.setShop(shop);

        productList = productDao.selectProductList(productCondition, rowIndex, pageSize);
        Assert.assertEquals(3, productList.size());

        effectNum = productDao.selectCountProduct(productCondition);
        Assert.assertEquals(4, effectNum);

        System.out.println("==========================================");

        Shop shop2 = new Shop();
        shop2.setShopId(20L);

        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(11L);

        Product productCondition2 = new Product();
        productCondition2.setShop(shop2);
        productCondition2.setProductCategory(productCategory);
        productCondition2.setProductName("鲜榨");

        productList = productDao.selectProductList(productCondition2, rowIndex, pageSize);
        Assert.assertEquals(2, productList.size());

        effectNum = productDao.selectCountProduct(productCondition2);
        Assert.assertEquals(2, effectNum);

    }


    @Test
    public void updateProductCategory2Null() {
        long productCategoryId = 15L;
        long shopId = 20L;
        int effectNum = productDao.updateProductCategoryToNull(productCategoryId, shopId);

        /*productCategoryId = 36L;
        effectNum = productDao.updateProductCategory2Null(productCategoryId, shopId);*/
        Assert.assertEquals(2, effectNum);
    }
}