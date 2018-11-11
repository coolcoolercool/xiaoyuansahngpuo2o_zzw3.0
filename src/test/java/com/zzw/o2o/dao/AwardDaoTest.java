package com.zzw.o2o.dao;

import com.zzw.o2o.entity.Award;
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
 * date: 2018/11/9 21:52
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AwardDaoTest {

    @Autowired
    private AwardDao awardDao;

    /**
     * 测试查询功能
     */
    @Test
    public void testBqueryAwardList() throws Exception{
        Award award = new Award();
        List<Award> awardList = awardDao.queryAwardList(award, 0, 3);
        assertEquals(3, awardList.size());

        int count = awardDao.queryAwardCount(award);
        assertEquals(4, count);

        award.setAwardName("奖品");
        awardList = awardDao.queryAwardList(award, 0, 3);
        assertEquals(2, awardList.size());
        count = awardDao.queryAwardCount(award);
        assertEquals(2, count);
    }

    @Test
    public void queryAwardCount() {
    }

    @Test
    public void queryAwardByAwardId() {
    }

    /**
     * 测试添加功能
     */
    @Test
    public void testAinsertAward() {
        long shopId = 28L;
        //创建奖品1
        Award award1 = new Award();
        award1.setAwardName("我是奖品1");
        award1.setAwardImg("我是奖品1的图片");
        award1.setPoint(1);
        award1.setPriority(11);
        award1.setEnableStatus(1);
        award1.setCreateTime(new Date());
        award1.setLastEditTime(new Date());
        award1.setShopId(shopId);
        int effectNum1 = awardDao.insertAward(award1);
        assertEquals(1, effectNum1);

        //创建奖品2
        Award award2 = new Award();
        award2.setAwardName("我是奖品2");
        award2.setAwardImg("我是奖品2的图片");
        award2.setPoint(2);
        award2.setPriority(22);
        award2.setEnableStatus(1);
        award2.setCreateTime(new Date());
        award2.setLastEditTime(new Date());
        award2.setShopId(shopId);
        int effectNum2 = awardDao.insertAward(award2);
        assertEquals(1, effectNum2);

        //创建奖品3
        Award award3 = new Award();
        award3.setAwardName("红茶");
        award3.setAwardImg("红茶的图片");
        award3.setPoint(3);
        award3.setPriority(33);
        award3.setEnableStatus(1);
        award3.setCreateTime(new Date());
        award3.setLastEditTime(new Date());
        award3.setShopId(shopId);
        int effectNum3 = awardDao.insertAward(award3);
        assertEquals(1, effectNum3);

        //创建奖品4
        Award award4 = new Award();
        award4.setAwardName("绿茶");
        award4.setAwardImg("绿茶的图片");
        award4.setPoint(4);
        award4.setPriority(44);
        award4.setEnableStatus(1);
        award4.setCreateTime(new Date());
        award4.setLastEditTime(new Date());
        award4.setShopId(shopId);
        int effectNum4 = awardDao.insertAward(award4);
        assertEquals(1, effectNum4);
    }

    /**
     * 测试更新的功能
     */
    @Test
    public void testCupdateAward() {
        Award awardCondition = new Award();
        awardCondition.setAwardName("我是奖品1");
        //按照特定的名字查询返回特定的奖品
        List<Award> awardList = awardDao.queryAwardList(awardCondition, 0, 1);
        //修改该奖品的名字
        awardList.get(0).setAwardName("我确实是奖品1");
        int effectedNum = awardDao.updateAward(awardList.get(0));
        assertEquals(1, effectedNum);
        //将修改名字后的奖品找出来并验证
        Award award = awardDao.queryAwardByAwardId(awardList.get(0).getAwardId());
        assertEquals("我确实是奖品1",award.getAwardName());
    }

    /**
     * 测试删除功能
     */
    @Test
    public void deleteAward() {
        Award awardCondition = new Award();
        awardCondition.setAwardName("奖品");
        //查询出所有名字包含"奖品"的奖品并删除
        List<Award> awardList = awardDao.queryAwardList(awardCondition, 0, 2);
        assertEquals(2, awardList.size());
        for(Award award : awardList){
            int effectNum = awardDao.deleteAward(award.getAwardId(), award.getShopId());
            assertEquals(1, effectNum);
        }
    }
}