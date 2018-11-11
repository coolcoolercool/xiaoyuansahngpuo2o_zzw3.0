package com.zzw.o2o.dao;

import com.zzw.o2o.entity.PersonInfo;
import com.zzw.o2o.entity.Shop;
import com.zzw.o2o.entity.UserShopMap;
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
 * date: 2018/11/10 9:06
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserShopMapDaoTest {

    @Autowired
    private UserShopMapDao userShopMapDao;

    @Test
    public void queryUserShopMapList() {
    }

    @Test
    public void queryUserShopMap() {
    }

    @Test
    public void testBqueryUserShopMapCount() {
        UserShopMap userShopMap = new UserShopMap();

/*
        List<UserShopMap> userShopMapList = userShopMapDao.queryUserShopMapList(userShopMap, 0, 2);
        assertEquals(2, userShopMapList.size());
        int count = userShopMapDao.queryUserShopMapCount(userShopMap);
        assertEquals(2,count);

        //按店铺去查询
        Shop shop = new Shop();
        shop.setShopId(29L);
        userShopMap.setShop(shop);

        userShopMapList = userShopMapDao.queryUserShopMapList(userShopMap, 0, 2);
        assertEquals(1, userShopMapList.size());

        count = userShopMapDao.queryUserShopMapCount(userShopMap);
        assertEquals(1, count);
*/

        //按用户Id和店铺查询
        userShopMap = userShopMapDao.queryUserShopMap(14L,29L);
        assertEquals("顾客2号", userShopMap.getUserName());
    }

    /**
     * 测试添加功能
     */
    @Test
    public void testAinsertUserShopMap() {
        //创建用户店铺积分统计信息
        UserShopMap userShopMap = new UserShopMap();
        PersonInfo customer = new PersonInfo();
        Shop shop = new Shop();

        customer.setUserId(13L);
        customer.setName("顾客1号");
        userShopMap.setUser(customer);

        shop.setShopId(28L);
        shop.setShopName("zzw个人店铺");
        userShopMap.setShop(shop);

        userShopMap.setCreateTime(new Date());
        userShopMap.setPoint(1);

        int effectNum = userShopMapDao.insertUserShopMap(userShopMap);
        assertEquals(1, effectNum);

        //创建用户店铺积分统计信息
        UserShopMap userShopMap1 = new UserShopMap();
        PersonInfo customer1 = new PersonInfo();
        Shop shop1 = new Shop();

        customer1.setUserId(14L);
        customer1.setName("顾客2号");
        userShopMap1.setUser(customer1);

        shop1.setShopId(29L);
        shop1.setShopName("zzw个人游戏机厅");
        userShopMap1.setShop(shop1);

        userShopMap1.setCreateTime(new Date());
        userShopMap1.setPoint(2);

        int effectNum1 = userShopMapDao.insertUserShopMap(userShopMap1);
        assertEquals(1, effectNum1);
    }

    @Test
    public void updateUserShopMapPoint() {
        UserShopMap userShopMap = new UserShopMap();

        userShopMap = userShopMapDao.queryUserShopMap(14L,29L);
        //assertEquals("积分不一致", 3 == userShopMap.getPoint());

        userShopMap.setPoint(3);

        int effectNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
        assertEquals(1, effectNum);
    }
}