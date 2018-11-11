package com.zzw.o2o.dao;

import com.zzw.o2o.entity.Award;
import com.zzw.o2o.entity.PersonInfo;
import com.zzw.o2o.entity.Shop;
import com.zzw.o2o.entity.UserAwardMap;
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
 * date: 2018/11/10 9:27
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserAwardMapDaoTest {

    @Autowired
    private UserAwardMapDao userAwardMapDao;

    @Autowired
    private AwardDao awardDao;

    @Autowired
    private PersonInfoDao personInfoDao;

    /**
     * 测试查询功能
     */
    @Test
    public void queryUserAwardMapList() {
        UserAwardMap userAwardMap = new UserAwardMap();

        List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 3);
        assertEquals(3, userAwardMapList.size());
        int count = userAwardMapDao.queryUserAwardMapCount(userAwardMap);
        assertEquals(5, count);

        PersonInfo user = new PersonInfo();
        user.setName("顾客");
        userAwardMap.setUser(user);

        userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 3);
        assertEquals(3, userAwardMapList.size());

        count = userAwardMapDao.queryUserAwardMapCount(userAwardMap);
        assertEquals(4, count);

        //userAwardMap = userAwardMapDao.queryUserAwardMapById(userAwardMapList.get(0).getUserAwardId());
        userAwardMap = userAwardMapList.get(0);
        assertEquals("红茶", userAwardMap.getAwardName());
    }

    @Test
    public void queryUserAwardMapCount() {
    }

    @Test
    public void queryUserAwardMapById() {
    }

    @Test
    public void testAinsertUserAwardMap() {
        //创建用户奖品映射信息1
       /* UserAwardMap userAwardMap = new UserAwardMap();
        PersonInfo customer = new PersonInfo();
        Award award = new Award();
        Shop shop = new Shop();

        customer = personInfoDao.queryPersonInfoById(13L);
        userAwardMap.setUser(customer);
        userAwardMap.setUserId(13L);
        userAwardMap.setUserName(customer.getName());

        award = awardDao.queryAwardByAwardId(3L);
        userAwardMap.setAward(award);
        userAwardMap.setAwardId(3L);
        userAwardMap.setAwardName(award.getAwardName());

        shop.setShopId(28L);
        userAwardMap.setShop(shop);
        userAwardMap.setShopId(28L);

        userAwardMap.setCreateTime(new Date());
        userAwardMap.setUsedStatus(1);
        userAwardMap.setPoint(1);
        int effectNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
        assertEquals(1, effectNum);

        //创建用户奖品映射信息2
        UserAwardMap userAwardMap2 = new UserAwardMap();
        PersonInfo customer2 = new PersonInfo();
        Award award2 = new Award();
        Shop shop2 = new Shop();

        customer2 = personInfoDao.queryPersonInfoById(13L);
        userAwardMap2.setUser(customer2);
        userAwardMap2.setUserId(13L);
        userAwardMap2.setUserName(customer2.getName());

        award2 = awardDao.queryAwardByAwardId(4L);
        userAwardMap2.setAward(award2);
        userAwardMap2.setAwardId(4L);
        userAwardMap2.setAwardName(award2.getAwardName());

        shop2.setShopId(29L);
        userAwardMap2.setShop(shop2);
        userAwardMap2.setShopId(29L);

        userAwardMap2.setCreateTime(new Date());
        userAwardMap2.setUsedStatus(0);
        userAwardMap2.setPoint(2);
        int effectNum2 = userAwardMapDao.insertUserAwardMap(userAwardMap2);
        assertEquals(1, effectNum2);*/

        UserAwardMap userAwardMap = new UserAwardMap();
        PersonInfo customer = new PersonInfo();
        Award award = new Award();
        Shop shop = new Shop();

        customer = personInfoDao.queryPersonInfoById(13L);
        userAwardMap.setUser(customer);

        award = awardDao.queryAwardByAwardId(3L);
        userAwardMap.setAward(award);

        shop.setShopId(28L);
        userAwardMap.setShop(shop);

        userAwardMap.setCreateTime(new Date());
        userAwardMap.setUsedStatus(1);
        userAwardMap.setPoint(1);
        int effectNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
        assertEquals(1, effectNum);
    }

    /**
     * 测试更新功能
     */
    @Test
    public void updateUserAwardMap() {
        UserAwardMap userAwardMap = new UserAwardMap();
        PersonInfo customer = new PersonInfo();

        customer.setName("顾客");
        userAwardMap.setUser(customer);

        List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 4);

        //assertTrue("Error, 积分不一致", 0 == userAwardMapList.get(2).getUsedStatus());
        //System.out.println(userAwardMapList.get(3).getUsedStatus());

        /*for(UserAwardMap userAwardMap1 : userAwardMapList){
            System.out.println(userAwardMap1);
        }*/

        userAwardMapList.get(3).setUsedStatus(1);
        int effectMum = userAwardMapDao.updateUserAwardMap(userAwardMapList.get(3));
        assertEquals(1, effectMum);
    }
}