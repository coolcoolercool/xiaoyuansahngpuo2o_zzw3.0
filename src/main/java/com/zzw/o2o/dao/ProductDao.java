package com.zzw.o2o.dao;

import com.zzw.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/29 20:58
 */

/*
* 商品相关的接口
* */
public interface ProductDao {
    /**
     * 删除productCategory的时候，需要先将tb_product中将productCategoryId置为null
     * @param productCategoryId
     * @param shopId
     * @return
     */
    int updateProductCategoryToNull(@Param("productCategoryId") long productCategoryId,
                                    @Param("shopId") long shopId);

    /**
     * 支持分页功能的查询product
     * 需要支持根据商品名称(支持模糊查询)，商品状态，shopId和商品类型的查询，以及它们之间的组合查询
     * @param productCondition
     * @param rowIndex 从第几行开始取，索引是从0开始的，第一条数据就是索引0
     * @return
     */
    List<Product> selectProductList(@Param("productCondition") Product productCondition,
                                    @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 按照条件查询，符合前端传入的条件的商品的总数
     * @param productCondition
     * @return
     */
    int selectCountProduct(@Param("productCondition") Product productCondition);

    /**
     * 增加商品
     * @param product
     * @return
     */
    int insertProduct(Product product);

    /**
     * 根据productId查询product
     * @param productId
     * @return
     */
    Product selectProductById(Long productId);

    /**
     * 修改商品
     * @param product
     * @return
     */
    int updateProduct(Product product);

}
