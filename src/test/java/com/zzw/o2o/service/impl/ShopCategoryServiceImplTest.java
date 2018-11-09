package com.zzw.o2o.service.impl;


import com.zzw.o2o.entity.ShopCategory;
import com.zzw.o2o.service.ShopCategoryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/27 15:50
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopCategoryServiceImplTest {
    @Autowired
    private ShopCategoryService shopCategoryService;

    @Test
    public void getShopCategoryList() {
        ShopCategory shopCategory = new ShopCategory();
        List<ShopCategory> shopCategories = shopCategoryService.getShopCategoryList(shopCategory);
        Assert.assertEquals(19, shopCategories.size());
        for(ShopCategory shopCategory1 : shopCategories){
            System.out.println(shopCategory1);
        }
    }
}