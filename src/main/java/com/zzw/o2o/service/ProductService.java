package com.zzw.o2o.service;

import com.zzw.o2o.dto.ImageHolder;
import com.zzw.o2o.dto.ProductExecution;
import com.zzw.o2o.entity.Product;
import com.zzw.o2o.exception.ProductOperationException;

import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/30 8:30
 */

public interface ProductService {

    /**
     * 查询
     * @param productCondition
     * @param pageIndex 页数，第几页
     * @param pageSize 一页的大小，一页最多有多少个Product
     * @return
     * @throws ProductOperationException
     */
    ProductExecution queryProductionList(Product productCondition, int pageIndex, int pageSize)
            throws ProductOperationException;

    /**
     * 商品处理，我们不仅需要处理商品的缩略图信息，还要处理商品详情中的多个图片信息
     * 重构前的方法，现在已经废弃了
     */
    /*ProductExecution addProduct(Product product, InputStream prodImgIns, String prodImg,
         List<InputStream> prodImgDetailInsList, List<String> prodImgDetailNameList) throws ProductOperationException;*/


    /**
     * 这里是重构
     * @param product
     * @param imageHolder
     * @param prodImgDetailList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> prodImgDetailList)
        throws ProductOperationException;

    /**
     * 根据productId查询唯一的product,以及图片处理
     * @param productId
     * @return
     */
    Product queryProductById(Long productId);

    /**
     * 修改商品信息以及图片处理
     * @param product
     * @param imageHolder
     * @param prodImgDetailList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> prodImgDetailList)
            throws ProductOperationException;
}
