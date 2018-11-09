package com.zzw.o2o.dao;

import com.zzw.o2o.entity.Area;

import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/25 9:23
 */


public interface AreaDao {
    /**
     * 返回区域列表
     * @return areaList
     */
    List<Area> queryArea();
}
