package com.zzw.o2o.service;


import com.zzw.o2o.dto.LocalAuthExecution;
import com.zzw.o2o.entity.LocalAuth;
import com.zzw.o2o.enums.WechatAuthStateEnum;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

/**
 * author: zzw5005
 * date: 2018/11/7 10:15
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthServiceTest {
    @Autowired
    private LocalAuthService localAuthService;

    @Test
    public void getLocalAuthByUserNameAndPwd() {
        LocalAuth localAuth = localAuthService.getLocalAuthByUserNameAndPwd("zzw","12345");
        Assert.assertEquals("zzw", localAuth.getUserName());
        System.out.println(localAuth.toString());
    }

    @Test
    public void getLocalAuthByUserId() {
    }

    @Test
    public void register() {
    }

    @Test
    public void bindLocalAuth() {
    }

    @Test
    public void modifyLocalAuth() {
        long userId = 12L;
        String userName = "testzzw1";
        String password = "root";
        String newPassword = "123456";
        LocalAuthExecution lae = localAuthService.modifyLocalAuth(userId, userName,password,newPassword);
        assertEquals(WechatAuthStateEnum.SUCCESS.getState(), lae.getState());
        //通过账号密码找到修改后的localAuth
        LocalAuth localAuth = localAuthService.getLocalAuthByUserNameAndPwd(userName, newPassword);
        //打印用户名字看看跟预期是否相符
        System.out.println(localAuth.getPersonInfo().getName());
    }
}