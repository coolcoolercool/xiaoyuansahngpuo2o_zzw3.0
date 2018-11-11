package com.zzw.o2o.dao;

import com.zzw.o2o.entity.PersonInfo;
import com.zzw.o2o.entity.Product;
import com.zzw.o2o.entity.Shop;
import com.zzw.o2o.entity.UserProductMap;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * author: zzw5005
 * date: 2018/11/10 15:14
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserProductMapDaoTest {

    @Autowired
    private UserProductMapDao userProductMapDao;

    @Test
    public void testBqueryUserProductMapList() {
        UserProductMap userProductMap = new UserProductMap();
        PersonInfo customer = new PersonInfo();

        customer.setName("1号");
        userProductMap.setUser(customer);
        List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMapList(userProductMap, 0, 2);

        assertEquals(2, userProductMapList.size());

        int count = userProductMapDao.queryUserProductMapCount(userProductMap);
        assertEquals(3, count);

        //加入商铺信息去查询
        Shop shop = new Shop();
        shop.setShopId(29L);
        userProductMap.setShop(shop);
        userProductMapList = userProductMapDao.queryUserProductMapList(userProductMap, 0, 3);
        assertEquals(2, userProductMapList.size());

        count = userProductMapDao.queryUserProductMapCount(userProductMap);
        assertEquals(2,count);

    }

    @Test
    public void queryUserProductMapCount() {
    }

    /**
     * 测试添加功能
     */
    @Test
    public void testAinsertUserProductMap() {
        //创建用户商品映射信息1
        UserProductMap userProductMap = new UserProductMap();
        PersonInfo customer = new PersonInfo();
        Product product = new Product();
        Shop shop = new Shop();

        customer.setUserId(13L);
        customer.setName("顾客1号");
        userProductMap.setUser(customer);

        product.setProductId(24L);
        product.setProductName("测试购买商品1");
        userProductMap.setProduct(product);

        shop.setShopId(28L);
        shop.setShopName("zzw个人商店");
        userProductMap.setShop(shop);
        userProductMap.setCreateTime(new Date());

        int effectNum = userProductMapDao.insertUserProductMap(userProductMap);
        assertEquals(1, effectNum);

        //创建用户商品映射信息2
        UserProductMap userProductMap2 = new UserProductMap();
        PersonInfo customer2 = new PersonInfo();
        Product product2 = new Product();
        Shop shop2 = new Shop();

        customer2.setUserId(13L);
        customer2.setName("顾客1号");
        userProductMap2.setUser(customer2);

        product2.setProductId(24L);
        product2.setProductName("测试购买商品1");
        userProductMap2.setProduct(product2);

        shop2.setShopId(29L);
        shop2.setShopName("zzw个人游戏机厅");
        userProductMap2.setShop(shop2);
        userProductMap2.setCreateTime(new Date());

        int effectNum2 = userProductMapDao.insertUserProductMap(userProductMap2);
        assertEquals(1, effectNum2);

        //创建用户商品映射信息3
        UserProductMap userProductMap3 = new UserProductMap();
        PersonInfo customer3 = new PersonInfo();
        Product product3 = new Product();
        Shop shop3 = new Shop();

        customer3.setUserId(14L);
        customer3.setName("顾客2号");
        userProductMap3.setUser(customer3);

        product3.setProductId(24L);
        product3.setProductName("测试购买商品1");
        userProductMap3.setProduct(product3);

        shop3.setShopId(29L);
        shop3.setShopName("zzw个人游戏机厅");
        userProductMap3.setShop(shop3);
        userProductMap3.setCreateTime(new Date());

        int effectNum3 = userProductMapDao.insertUserProductMap(userProductMap3);
        assertEquals(1, effectNum3);

        //创建用户商品映射信息4
        UserProductMap userProductMap4 = new UserProductMap();
        PersonInfo customer4 = new PersonInfo();
        Product product4 = new Product();
        Shop shop4 = new Shop();

        customer4.setUserId(13L);
        customer4.setName("顾客1号");
        userProductMap4.setUser(customer4);

        product4.setProductId(23L);
        product4.setProductName("测试2");
        userProductMap4.setProduct(product4);

        shop4.setShopId(29L);
        shop4.setShopName("zzw个人游戏机厅");
        userProductMap4.setShop(shop4);
        userProductMap4.setCreateTime(new Date());

        int effectNum4 = userProductMapDao.insertUserProductMap(userProductMap4);
        assertEquals(1, effectNum4);
    }
}