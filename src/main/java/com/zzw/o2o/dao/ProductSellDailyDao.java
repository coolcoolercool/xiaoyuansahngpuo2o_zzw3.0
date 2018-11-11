package com.zzw.o2o.dao;

import com.zzw.o2o.entity.ProductSellDaily;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * author: zzw5005
 * date: 2018/11/10 16:12
 */


public interface ProductSellDailyDao {

    /**
     * 根据查询条件返回商铺日销售的统计列表
     * @param productSellDailyCondition
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductSellDaily> queryProductSellDailyList(
            @Param("productSellDailyCondition") ProductSellDaily productSellDailyCondition,
            @Param("beginTime") Date beginTime,
            @Param("endTime") Date endTime
            );

    /**
     * 统计平台所有商品的日销售量
     * 这个函数是以商品id为类型,将所有商铺下销售该商品的销量累加
     * 这里最好是每家店铺的商品都是独有,每家店铺商品都是不一样的,必须要有这个限制
     * 也就是同一件商品只能在一家店铺中进行销售
     * @return
     */
    int insertProductSellDaily();
}
