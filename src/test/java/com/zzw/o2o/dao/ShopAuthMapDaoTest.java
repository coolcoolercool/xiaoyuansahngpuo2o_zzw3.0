package com.zzw.o2o.dao;

import com.zzw.o2o.entity.PersonInfo;
import com.zzw.o2o.entity.Shop;
import com.zzw.o2o.entity.ShopAuthMap;
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
 * date: 2018/11/10 20:45
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShopAuthMapDaoTest {

     @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    /**
     * 测试查询功能
      */
    @Test
    public void testBqueryShopAuthMapListByShopId() {
        //测试queryShopAuthMapListByShopId
        List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(29L,0, 1);
        assertEquals(1, shopAuthMapList.size());

        //测试queryShopAuthMapById
        ShopAuthMap shopAuthMap = shopAuthMapDao.
                queryShopAuthMapById(shopAuthMapList.get(0).getShopAuthId());
        assertEquals("白富美", shopAuthMap.getTitle());

        System.out.println("employee name is : " + shopAuthMap.getEmployee().getName());
        System.out.println("shopName is : " + shopAuthMap.getShop().getShopName());

        //测试queryShopAuthCountByShopId
        int count = shopAuthMapDao.queryShopAuthCountByShopId(29L);
        assertEquals(1, count);
    }

    @Test
    public void queryShopAuthCountByShopId() {
    }

    /**
     * 测试添加方法
     */
    @Test
    public void testAinsertShopAuthMap() {
        //创建店铺授权信息1
        ShopAuthMap shopAuthMap = new ShopAuthMap();
        PersonInfo employee = new PersonInfo();
        Shop shop = new Shop();

        employee.setUserId(15L);
        employee.setName("雇员1号");
        shopAuthMap.setEmployee(employee);

        shop.setShopId(28L);
        shopAuthMap.setShop(shop);
        shopAuthMap.setTitle("CEO");
        shopAuthMap.setTitleFlag(1);
        shopAuthMap.setCreateTime(new Date());
        shopAuthMap.setLastEditTime(new Date());
        shopAuthMap.setEnableStatus(1);

        int effectNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
        assertEquals(1, effectNum);

        //创建店铺授权信息2
        ShopAuthMap shopAuthMap1 = new ShopAuthMap();
        PersonInfo employee1 = new PersonInfo();
        Shop shop1 = new Shop();

        employee1.setUserId(16L);
        employee1.setName("雇员2号");
        shopAuthMap1.setEmployee(employee1);

        shop1.setShopId(29L);
        shopAuthMap1.setShop(shop1);
        shopAuthMap1.setTitle("白富美");
        shopAuthMap1.setTitleFlag(1);
        shopAuthMap1.setCreateTime(new Date());
        shopAuthMap1.setLastEditTime(new Date());
        shopAuthMap1.setEnableStatus(1);

        int effectNum1 = shopAuthMapDao.insertShopAuthMap(shopAuthMap1);
        assertEquals(1, effectNum1);
    }

    /**
     * 测试更新功能
     */
    @Test
    public void testCupdateShopAuthMap() {
        List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(29L, 0, 2);
        ShopAuthMap shopAuthMap = shopAuthMapList.get(0);

        System.out.println(shopAuthMap.getShop().getShopId() + "\n");
        shopAuthMap.setTitle("迎娶白富美");
        shopAuthMap.setTitleFlag(2);
        int effectNum = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
        assertEquals(1, effectNum);
    }

    /**
     * 测试删除功能
     */
    @Test
    public void testDdeleteShopAuthMap() {
        List<ShopAuthMap> shopAuthMapList1 = shopAuthMapDao.queryShopAuthMapListByShopId(28L, 0, 1);
        ShopAuthMap shopAuthMap = shopAuthMapList1.get(0);
        System.out.println(shopAuthMap.getEmployee().getName() + "\n");

        System.out.println(shopAuthMap.getShop().getShopId() + "\n");

        System.out.println(shopAuthMap.getShopAuthId() + "\n");

        List<ShopAuthMap> shopAuthMapList2 = shopAuthMapDao.queryShopAuthMapListByShopId(29L, 0, 1);
        ShopAuthMap shopAuthMap1 = shopAuthMapList2.get(0);

        int effectNum = shopAuthMapDao.deleteShopAuthMap
                (shopAuthMap.getShopAuthId());
        assertEquals(1, effectNum);

        effectNum = shopAuthMapDao.deleteShopAuthMap
                (shopAuthMap1.getShopAuthId());
        assertEquals(1, effectNum);
    }

    @Test
    public void queryShopAuthMapById() {
    }
}