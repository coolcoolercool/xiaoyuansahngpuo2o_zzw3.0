package com.zzw.o2o.service;

import com.zzw.o2o.entity.ShopCategory;

import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/27 15:44
 */


public interface ShopCategoryService {
    // redis key的前缀，抽取到接口层，方便使用
    public static final String SCLISTKEY = "shopcategory";

    /**
     * 根据查询条件获取ShopCategory列表
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);


}
