package com.zzw.o2o.dao;

import com.zzw.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/25 16:37
 */


public interface ShopDao {
    /**
     * 通过employee id 查询店铺
     *
     * @param employeeId
     * @return List<shop>
     */
    List<Shop> queryByEmployeeId(long employeeId);

    /**
     *  带有分页功能的查询商铺列表
     *  可输入的查询条件：商铺名（要求模糊查询） 区域Id 商铺状态 商铺类别 owner
     *  (注意在sqlmapper中按照前端入参拼装不同的查询语句)
     * @param shopCondition
     * @param rowIndex 从第几行开始取
     * @param pageSize 返回多少行数据（页面上的数据量）比如 rowIndex为1,pageSize为5 即为 从第一行开始取，取5行数据
     * @return List<Shop>
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
                             @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 按照条件查询 符合前台传入的条件的商铺的总数
     * @param shopCondition
     * @return
     */
    int queryShopCount(@Param("shopCondition") Shop shopCondition);

    /**
     * 通过shopId查询店铺
     * @param shopId
     * @return shop
     */
    Shop queryByShopId(Long shopId);

    /**
     * 新增店铺
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     * @return
     */
    int updateShop(Shop shop);
}
