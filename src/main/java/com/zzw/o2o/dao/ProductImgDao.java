package com.zzw.o2o.dao;

import com.zzw.o2o.entity.ProductImg;

import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/29 21:04
 */

/*
* 商品图片
* */
public interface ProductImgDao  {
    /**
     * 一个商品下可能拥有多个图片，所以这里是批量新增商品详情图片
     * @param productImgList
     * @return
     */
    int batchInsertProductImg(List<ProductImg> productImgList);

    /**
     * 根据productId查询商铺对应的图片详情图
     * @param productId
     * @return
     */
    List<ProductImg> selectProductImgList(Long productId);

    /**
     * 删除商品对应的所有商品详情图
     * @param productId
     * @return
     */
    int deleteProductImgById(Long productId);

}
