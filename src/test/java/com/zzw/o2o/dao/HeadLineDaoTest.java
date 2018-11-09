package com.zzw.o2o.dao;


import com.zzw.o2o.entity.HeadLine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/31 20:00
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class HeadLineDaoTest {

    @Autowired
    private HeadLineDao headLineDao;

    @Test
    public void selectHeadLineList() {
        HeadLine headLineCondition = new HeadLine();

        headLineCondition.setEnableStatus(0);

        //查询不可用的头条信息 1条
        List<HeadLine> headLineList = headLineDao.selectHeadLineList(headLineCondition);
        Assert.assertEquals(1, headLineList.size());
        for(HeadLine headLine : headLineList){
            System.out.println(headLine);
        }

        //查询可用的头条信息 4条
        headLineCondition.setEnableStatus(1);
        headLineList = headLineDao.selectHeadLineList(headLineCondition);
        Assert.assertEquals(4, headLineList.size());
        for(HeadLine headLine : headLineList){
            System.out.println(headLine);
        }

        //查询全部状态的头条信息 4条
        headLineList = headLineDao.selectHeadLineList(new HeadLine());
        Assert.assertEquals(5, headLineList.size());
        for(HeadLine headLine : headLineList){
            System.out.println(headLine);
        }
    }
}