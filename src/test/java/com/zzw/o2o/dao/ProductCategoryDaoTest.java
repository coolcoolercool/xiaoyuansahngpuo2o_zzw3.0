package com.zzw.o2o.dao;


import com.zzw.o2o.entity.ProductCategory;
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

import static org.junit.Assert.*;

/**
 * author: zzw5005
 * date: 2018/10/29 9:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //控制测试方法的顺序,这里选择方法的名字顺序来执行，然后在方法前面加上自己想要的顺序的字母即可控制方法的执行顺序
public class ProductCategoryDaoTest{
    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Test
    public void testBqueryProductCategoryList() {
        long shopId = 15L;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
        System.out.println("该店铺自定义类型数为: " + productCategoryList.size());
    }

    @Test
    public void testAbatchInsertProductCategory() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryName("商品类型1");
        productCategory.setProductCategoryDesc("商品类型1");
        productCategory.setPriority(1);
        productCategory.setCreateTime(new Date());
        productCategory.setLastEditTime(new Date());
        productCategory.setShopId(15L);
        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setProductCategoryName("商品类型2");
        productCategory1.setProductCategoryDesc("商品类型2");
        productCategory1.setPriority(2);
        productCategory1.setCreateTime(new Date());
        productCategory1.setLastEditTime(new Date());
        productCategory1.setShopId(15L);

        List<ProductCategory> productCategoryList = new ArrayList<>();
        productCategoryList.add(productCategory);
        productCategoryList.add(productCategory1);
        int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
        assertEquals(2, effectedNum);

    }

    /**
     * Junit 4.11里增加了指定测试方法执行顺序的特性.
     * 测试类的执行顺序可通过对测试类添加注解@FixMethodOrder(value) 来指定,其中value 为执行顺序三种执行顺序可供选择：
     *   默认（MethodSorters.DEFAULT）,
     *     默认顺序由方法名hashcode值来决定，如果hash值大小一致，则按名字的字典顺序确定。由于hashcode的生成和操作系统相关，
     *     (以native修饰），所以对于不同操作系统，可能会出现不一样的执行顺序，在某一操作系统上，多次执行的顺序不变
     *   按方法名（ MethodSorters.NAME_ASCENDING）【推荐】,
     *      按方法名称的进行排序，由于是按字符的字典顺序，所以以这种方式指定执行顺序会始终保持一致；
     *      不过这种方式需要对测试方法有一定的命名规则，如 测试方法均以testNNN开头（NNN表示测试方法序列号 001-999）
     *   JVM（MethodSorters.JVM）
     *      按JVM返回的方法名的顺序执行，此种方式下测试方法的执行顺序是不可预测的，即每次运行的顺序可能都不一样
     */
    @Test
    public void testCdeleteProductCategory() {
        Long shopId = 15L;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
        for(ProductCategory pc : productCategoryList){
            if("商品类型1".equals(pc.getProductCategoryName()) ||
                    "商品类型2".equals(pc.getProductCategoryName()) ){
                int effectedNum = productCategoryDao.deleteProductCategory(pc.getProductCategoryId(), shopId);
                assertEquals(1, effectedNum);
            }
        }
    }
}