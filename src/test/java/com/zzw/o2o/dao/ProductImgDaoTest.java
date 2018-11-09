package com.zzw.o2o.dao;


import com.zzw.o2o.entity.ProductImg;
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
 * date: 2018/10/29 21:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductImgDaoTest{

    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void testAbatchInsertProductImg() {
        //productId为1的商品里添加两个详情图片
        ProductImg productImg1 = new ProductImg();
        productImg1.setImgAddr("/xiaogongzhu/xxx");
        productImg1.setImgDesc("商品详情图片1");
        productImg1.setPriority(11);
        productImg1.setCreateTime(new Date());
        productImg1.setProductId(16L);

        ProductImg productImg2 = new ProductImg();
        productImg2.setImgAddr("/xiaogongzhu/xxx");
        productImg2.setImgDesc("商品详情图片1");
        productImg2.setPriority(11);
        productImg2.setCreateTime(new Date());
        productImg2.setProductId(16L);

        //添加到productimgList中
        List<ProductImg> productImgList = new ArrayList<>();
        productImgList.add(productImg1);
        productImgList.add(productImg2);

        //调用接口批量新增商品详情图片
        int effectedNum = productImgDao.batchInsertProductImg(productImgList);

        Assert.assertEquals(2, effectedNum);
    }


    @Test
    public void testCdeleteProductImgById() {
        Long productId = 16L;
        int effectNum = productImgDao.deleteProductImgById(productId);
        Assert.assertEquals(2, effectNum);
    }
}