package com.zzw.o2o.service;

import com.zzw.o2o.dto.ProductCategoryExecution;
import com.zzw.o2o.entity.ProductCategory;
import com.zzw.o2o.exception.ProductCategoryOperationException;

import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/29 9:33
 */


public interface ProductCategoryService {

    /**
     * 查询指定某个店铺下的所有商品类型信息
     * @param shopId
     * @return
     */
    List<ProductCategory> getProductCategoryList(Long shopId);

    /**
     * 批量插入ProductCategory
     * @param productCategoryList
     * @return
     * @throws ProductCategoryOperationException
     */
    ProductCategoryExecution addProductCategory(List<ProductCategory> productCategoryList)
            throws ProductCategoryOperationException;

    /**
     * 需要先将该商品目录下的商品的类型Id置为空，然后再删除该商品目录，因此需要事务控制
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws ProductCategoryOperationException
     */
    ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
            throws ProductCategoryOperationException;
}
