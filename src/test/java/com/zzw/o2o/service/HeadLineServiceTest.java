package com.zzw.o2o.service;


import com.zzw.o2o.entity.HeadLine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/31 20:18
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class HeadLineServiceTest {

    @Autowired
    private HeadLineService headLineService;

    @Test
    public void queryHeadLineList() throws IOException {

        HeadLine headLineCondition = new HeadLine();

        // 状态 0 不可用 1 可用
        headLineCondition.setEnableStatus(0);
        // 查询不可用的头条信息
        List<HeadLine> headLineList = headLineService.queryHeadLineList(headLineCondition);
        Assert.assertEquals(1, headLineList.size());
        for (HeadLine headLine : headLineList) {
            System.out.println("0 -> "  + headLine);
        }
        // 查询可用的头条信息
        headLineCondition.setEnableStatus(1);
        headLineList = headLineService.queryHeadLineList(headLineCondition);
        Assert.assertEquals(4, headLineList.size());
        for (HeadLine headLine : headLineList) {
            System.out.println("1 -> " + headLine);
        }

        //再次查询 不同状态的头条信息
        // 状态 0 不可用 1 可用
        headLineCondition.setEnableStatus(0);
        // 查询不可用的头条信息
        headLineList = headLineService.queryHeadLineList(headLineCondition);
        Assert.assertEquals(1, headLineList.size());
        for (HeadLine headLine : headLineList) {
            System.out.println("0 -> "  + headLine);
        }
        // 查询可用的头条信息
        headLineCondition.setEnableStatus(1);
        headLineList = headLineService.queryHeadLineList(headLineCondition);
        Assert.assertEquals(4, headLineList.size());
        for (HeadLine headLine : headLineList) {
            System.out.println("1 -> " + headLine);
        }
    }
}