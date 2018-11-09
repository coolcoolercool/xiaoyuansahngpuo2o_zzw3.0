package com.zzw.o2o.service;

import com.zzw.o2o.dto.ImageHolder;
import com.zzw.o2o.dto.ShopExecution;
import com.zzw.o2o.entity.Area;
import com.zzw.o2o.entity.PersonInfo;
import com.zzw.o2o.entity.Shop;
import com.zzw.o2o.entity.ShopCategory;
import com.zzw.o2o.enums.ShopStateEnum;
import com.zzw.o2o.exception.ShopOperationException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.Date;

/**
 * author: zzw5005
 * date: 2018/10/26 10:34
 */


public class ShopServiceTest {

    @Autowired
    private ShopService shopService;

    @Test
    public void getShopList() {
        Shop shopCondition = new Shop();
        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(14L);
        shopCondition.setShopCategory(sc);
        ShopExecution se = shopService.getShopList(shopCondition, 1, 2);
        System.out.println("店铺列表数: "  + se.getShopList().size());
        System.out.println("店铺总数: " + se.getCount());
    }

    @Test
    public void addShop() throws ShopOperationException, IOException{
        Shop shop = new Shop();
        PersonInfo personInfo = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        personInfo.setUserId(1L);
        area.setAreaId(1L);
        shopCategory.setShopCategoryId(1L);

        shop.setOwner(personInfo);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);

        shop.setOwnerId(personInfo.getUserId());
        shop.setShopName("茶和咖啡1号");
        shop.setShopDesc("暂时休息一下");
        shop.setShopAddr("商业街");
        shop.setPhone("1010101");
        shop.setPriority(50);
        shop.setCreateTime(new Date());
        shop.setLastEditTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");

        File shopFile = new File("F:\\javaCode\\OfferProduct\\xioayuanshangpu\\Image\\win\\zhende.png");

        ShopExecution se = null;
        InputStream ins = null;

        try {
            ins = new FileInputStream(shopFile);
            ImageHolder imageHolder = new ImageHolder(ins, "zhende.png");
            se = shopService.addShop(shop, imageHolder);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
    }

    @Test
    public void getByShopId() {
    }

    @Test
    public void modifyShop() throws ShopOperationException, FileNotFoundException {
        Shop shop = new Shop();
        Area area = new Area();

        shop.setShopId(42L);
        area.setAreaId(2L);

        //shop.setArea(area);
        shop.setShopName("夏日之风风咖啡馆2号");
        File shopImg = new File("F:\\javaCode\\OfferProduct\\xioayuanshangpu\\Image\\others\\coffee.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder imageHolder = new ImageHolder(is, "coffee.jpg");
        //todo 不知道为什么这，这里获取的ShopExecution的中shop为null
        ShopExecution shopExecution = shopService.modifyShop(shop,imageHolder);
        if(shopExecution.equals(null)){
            System.out.println("null");
        }else{
            System.out.println(shopExecution);
        }
    }


}