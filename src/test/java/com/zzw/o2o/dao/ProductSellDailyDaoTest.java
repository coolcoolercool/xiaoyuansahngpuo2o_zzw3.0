package com.zzw.o2o.dao;

import com.zzw.o2o.entity.ProductSellDaily;
import com.zzw.o2o.entity.Shop;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * author: zzw5005
 * date: 2018/11/10 16:46
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductSellDailyDaoTest {

    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    /**
     * 测试查询功能
     */
    @Test
    public void testBqueryProductSellDailyList() {
        ProductSellDaily productSellDaily = new ProductSellDaily();
        //添加商铺信息去查询
        Shop shop = new Shop();
        shop.setShopId(28L);
        productSellDaily.setShop(shop);
        List<ProductSellDaily> productSellDailyList =
                productSellDailyDao.queryProductSellDailyList(productSellDaily, null, null);
        assertEquals(2, productSellDailyList.size());
    }

    /**
     * 测试添加功能
     */
    @Test
    public void testAinsertProductSellDaily() {
        //创建商品日销售统计
        int effectNum = productSellDailyDao.insertProductSellDaily();
        assertEquals(3, effectNum);
    }





}