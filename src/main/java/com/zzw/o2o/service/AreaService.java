package com.zzw.o2o.service;

import com.zzw.o2o.entity.Area;

import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/24 16:44
 */

/*
* 区域的服务层
* 在接口层传入缓存key的前缀,通过匹配的方式将匹配到的该前缀的所有key均删除
* */
public interface AreaService {
    //redis key的前缀,抽取到接口层,方便使用
    public static final String AREALISTKEY = "arealist";

    /**
     * 区域列表
     * @return
     */
    List<Area> getAreaList();
}
