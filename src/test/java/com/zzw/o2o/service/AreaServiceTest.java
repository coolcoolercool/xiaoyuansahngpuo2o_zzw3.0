package com.zzw.o2o.service;


import com.zzw.o2o.entity.Area;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * author: zzw5005
 * date: 2018/10/25 10:04
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest{
    @Autowired //通过@service标签，和@Autowried标签，可以自动将service的实现类自动注入
    private AreaService areaService;

    @Autowired
    private CacheService cacheService;

    @Test
    public void getAreaList() {
        // 首次从db中加载
        List<Area> areaList = areaService.getAreaList();
        for (Area area : areaList) {
            System.out.println("||---->" + area.toString());
        }

        // 再次查询从redis中获取
        areaList = areaService.getAreaList();
        for (Area area : areaList) {
            System.out.println("**---->" + area.toString());
        }
        // 清除缓存
        cacheService.removeFromCache(AreaService.AREALISTKEY);

        // 再次查询 从db中获取
        areaList = areaService.getAreaList();
        for (Area area : areaList) {
            System.out.println("**---->" + area.toString());
        }

        // 再次查询从redis中获取
        areaList = areaService.getAreaList();
        for (Area area : areaList) {
            System.out.println("||---->" + area.toString());
        }
    }
}