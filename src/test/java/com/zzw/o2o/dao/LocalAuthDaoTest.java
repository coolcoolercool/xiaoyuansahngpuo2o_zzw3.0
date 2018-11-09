package com.zzw.o2o.dao;


import com.zzw.o2o.entity.LocalAuth;
import com.zzw.o2o.entity.PersonInfo;
import com.zzw.o2o.util.MD5;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * author: zzw5005
 * date: 2018/11/7 10:16
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthDaoTest {

    @Autowired
    private LocalAuthDao localAuthDao;

    private static final String userName = "testusername";
    private static final String password = "testpassword";

    @Test
    public void testBqueryLocalByUserNameAndPwd() {
        LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd("zzw", MD5.getMd5("12345"));
        System.out.println(localAuth.toString());
    }

    @Test
    public void testCqueryLocalByUserId() {
        //按照用户id查询平台账号,进而获取用户信息
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(9L);
        assertEquals("龙州一条街客服",localAuth.getPersonInfo().getName());
    }

    @Test
    public void testAinsertLocalAuth() {
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(12L);

        localAuth.setUserName("testzzw1");
        localAuth.setPassword(MD5.getMd5("123456"));
        localAuth.setCreateTime(new Date());
        localAuth.setLastEditTime(new Date());

        localAuth.setPersonInfo(personInfo);

        int effectNum = localAuthDao.insertLocalAuth(localAuth);
        Assert.assertEquals(1, effectNum);

    }

    @Test
    public void testDupdateLocalAuth() {
        Date now = new Date();
        String password = MD5.getMd5("123456");
        String newPassword = MD5.getMd5("root");
        int effectedNum = localAuthDao.updateLocalAuth(12L,"testzzw1",password, newPassword, now);
        assertEquals(1,effectedNum);
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(12L);
        System.out.println(localAuth.getPassword());
    }


}