package com.zzw.o2o.service;


import com.zzw.o2o.dto.ImageHolder;
import com.zzw.o2o.dto.ProductExecution;
import com.zzw.o2o.entity.Product;
import com.zzw.o2o.entity.ProductCategory;
import com.zzw.o2o.entity.Shop;
import com.zzw.o2o.enums.ProductStateEnum;
import com.zzw.o2o.exception.ShopOperationException;
import org.junit.Assert;
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

/**
 * author: zzw5005
 * date: 2018/10/30 15:43
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest{
    @Autowired
    private ProductService productService;

    @Test
    public void testQueryProductListAndCount(){
        int pageIndex = 1;
        int pageSize = 3;

        Shop shop2 = new Shop();
        shop2.setShopId(15L);

        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(9L);

        Product productCondition = new Product();
        productCondition.setShop(shop2);
        productCondition.setProductCategory(productCategory);
        //productCondition.setProductName("test");


        ProductExecution productExecution = productService.queryProductionList(productCondition, pageIndex, pageSize);
        // 操作成功的状态为1
        Assert.assertEquals(1, productExecution.getState());
        Assert.assertEquals(3, productExecution.getProductList().size());
        Assert.assertEquals(4, productExecution.getCount());

        // 从第2页开始取，每页依然取3条
        pageIndex = 2;
        productExecution = productService.queryProductionList(productCondition, pageIndex, pageSize);
        Assert.assertEquals(1, productExecution.getState());
        Assert.assertEquals(1, productExecution.getProductList().size());
        Assert.assertEquals(4, productExecution.getCount());

    }

    @Test
    public void addProduct() throws ShopOperationException, FileNotFoundException {

        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(41L);
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(11L);
        product.setShop(shop);
        product.setProductCategory(pc);
        product.setProductName("测试商品1");
        product.setProductDesc("测试商品1");
        product.setPriority(20);
        product.setCreateTime(new Date());
        product.setEnableStatus(ProductStateEnum.SUCCESS.getState());

        File productFile = new File("F:\\javaCode\\OfferProduct\\xioayuanshangpu\\Image\\win\\zhende.png");
        InputStream ins = new FileInputStream(productFile);
        ImageHolder imageHolder = new ImageHolder(ins, productFile.getName());

        List<ImageHolder> prodImgDetailList = new ArrayList<>();

        File productDetailFile1 = new File("F:\\javaCode\\OfferProduct\\xioayuanshangpu\\Image\\win\\yiliya.png");
        InputStream ins1 = new FileInputStream(productDetailFile1);
        ImageHolder imageHolder1 = new ImageHolder(ins1, productDetailFile1.getName());

        File productDetailFile2 = new File("F:\\javaCode\\OfferProduct\\xioayuanshangpu\\Image\\win\\jiutun.png");
        InputStream ins2 = new FileInputStream(productDetailFile2);
        ImageHolder imageHolder2 = new ImageHolder(ins2, productDetailFile1.getName());

        prodImgDetailList.add(imageHolder1);
        prodImgDetailList.add(imageHolder2);

        ProductExecution pe = productService.addProduct(product, imageHolder, prodImgDetailList);

        Assert.assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
    }

    @Test
    public void modifyProduct() throws Exception{
        // 注意表中的外键关系，确保这些数据在对应的表中的存在
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(15L);

        // 注意表中的外键关系，确保这些数据在对应的表中的存在
        Shop shop = new Shop();
        shop.setShopId(20L);

        // 构造Product
        Product product = new Product();
        product.setProductName("offical_product");
        product.setProductDesc("product offical desc");

        product.setNormalPrice("100");
        product.setPromotionPrice("80");
        product.setPriority(66);
        product.setPoint(2);
        product.setLastEditTime(new Date());
        product.setProductCategory(productCategory);
        product.setShop(shop);

        product.setProductId(24L);
        // 构造 商品图片
        File productFile = new File("F:\\javaCode\\OfferProduct\\xioayuanshangpu\\Image\\others\\naicha.jpg");
        InputStream ins = new FileInputStream(productFile);
        ImageHolder imageHolder = new ImageHolder(ins, productFile.getName());

        // 构造商品详情图片
        List<ImageHolder> prodImgDetailList = new ArrayList<ImageHolder>();

        File productDetailFile1 = new File("F:\\javaCode\\OfferProduct\\xioayuanshangpu\\Image\\others\\women1.jpg");
        InputStream ins1 = new FileInputStream(productDetailFile1);
        ImageHolder imageHolder1 = new ImageHolder(ins1, productDetailFile1.getName());

        File productDetailFile2 = new File("F:\\javaCode\\OfferProduct\\xioayuanshangpu\\Image\\others\\women2.jpg");
        InputStream ins2 = new FileInputStream(productDetailFile2);
        ImageHolder imageHolder2 = new ImageHolder(ins2, productDetailFile2.getName());

        prodImgDetailList.add(imageHolder1);
        prodImgDetailList.add(imageHolder2);

        // 调用服务
        ProductExecution pe = productService.modifyProduct(product, imageHolder, prodImgDetailList);
        Assert.assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
    }
}