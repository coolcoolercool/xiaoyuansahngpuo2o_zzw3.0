package com.zzw.o2o.dao;

import com.zzw.o2o.entity.Area;
import com.zzw.o2o.entity.PersonInfo;
import com.zzw.o2o.entity.Shop;
import com.zzw.o2o.entity.ShopCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * author: zzw5005
 * date: 2018/10/25 16:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopDaoTest {
    @Autowired
    private ShopDao shopDao;

    @Test
    public void insertShop() {
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();

        /*
        * 这里的取值需要注意。因为tb_shop表中有外键约束，因此务必确保设置的这几个id在对应的外键表中
        * 是存在的，需要在表tb_person_info,tb_area, tb_shop_category分别添加对应id的数据，
        * 否则在插入tb_shop的时候会抛出异常，导致插入失败
        * */
        owner.setUserId(1L);
        area.setAreaId(1L);
        shopCategory.setShopCategoryId(1L);

        shop.setOwner(owner);
        shop.setShopCategoryId(shopCategory.getShopCategoryId());
        shop.setShopName("测试的店铺");
        shop.setShopDesc("test");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopImg("test");

        shop.setPriority(10);
        shop.setCreateTime(new Date());
        shop.setLastEditTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("在审核中");

        shop.setShopCategory(shopCategory);
        shop.setArea(area);

        int effectNum = shopDao.insertShop(shop);
        assertEquals(1, effectNum);
    }

    @Test
    public void testUpdateShop(){
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopDesc("测试描述");
        shop.setShopAddr("测试地址");
        shop.setLastEditTime(new Date());
        int effectedNum = shopDao.updateShop(shop);
        assertEquals(1, effectedNum);
    }

    @Test
    public void queryByShopId() {
        long shopId = 15L;
        Shop shop = shopDao.queryByShopId(shopId);
        System.out.println("areaId: " + shop.getArea().getAreaId());
        System.out.println("areaname: "  + shop.getArea().getAreaName());
    }

    @Test
    public void queryShopList() {
        Shop shopCondition = new Shop();
        ShopCategory childCategory = new ShopCategory();
        ShopCategory parentCategory = new ShopCategory();
        parentCategory.setShopCategoryId(10L);
        childCategory.setParentId(parentCategory);
        shopCondition.setShopCategory(childCategory);

        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 5);
        int count = shopDao.queryShopCount(shopCondition);
        System.out.println("店铺列表的大小: " + shopList.size());
        System.out.println("店铺总数: " + count);

        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(14L);
        shopCondition.setShopCategory(sc);
        List<Shop> shopList1 = shopDao.queryShopList(shopCondition, 0, 2);
        int count1 = shopDao.queryShopCount(shopCondition);
        System.out.println("店铺列表的大小: " + shopList1.size());
        System.out.println("店铺总数: " + count1);
    }

    @Test
    public void queryShopCount() {
    }
}