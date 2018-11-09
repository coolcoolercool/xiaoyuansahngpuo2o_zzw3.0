package com.zzw.o2o.service;

import com.zzw.o2o.dto.ImageHolder;
import com.zzw.o2o.dto.ShopExecution;
import com.zzw.o2o.entity.Shop;
import com.zzw.o2o.exception.ShopOperationException;

/**
 * author: zzw5005
 * date: 2018/10/26 9:34
 */


public interface ShopService {
    /**
     * 查询该用户下的店铺信息
     * @param employeeId
     * @return
     * @throws RuntimeException
     */
    ShopExecution getByEmployeeId(long employeeId) throws RuntimeException;

    /**
     * 获取商铺列表. 在这一个方法中同样的会调用查询总数的DAO层方法，封装到ShopExecution中
     * 根据shopCondition分页返回相应的店铺列表
     * @param shopCondition
     * @param pageIndex 前端页面 只有第几页 第几页 定义为pageIndex
     * @param pageSize  展示的行数
     * @return
     * @throws ShopOperationException
     */
    ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) throws ShopOperationException ;

    /**
     * 通过店铺Id获取店铺信息
     * @param shopId
     * @return
     */
    Shop getByShopId(long shopId);


    /**
     * 更新店铺信息，包括对图片的处理
     * @param shop
     * @param imageHolder
     * @return
     * @throws ShopOperationException
     */
    ShopExecution modifyShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException;


    /**
     *  创建商铺
     *  修改入参，将File类型入参改为inputStream，同时增加String类型的文件名称
     * @param shop
     * @param imageHolder
     * @return
     * @throws RuntimeException
     */
    ShopExecution addShop(Shop shop, ImageHolder imageHolder) throws RuntimeException;
}
