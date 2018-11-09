package com.zzw.o2o.service.impl;

import com.zzw.o2o.dao.ProductCategoryDao;
import com.zzw.o2o.dao.ProductDao;
import com.zzw.o2o.dto.ProductCategoryExecution;
import com.zzw.o2o.entity.ProductCategory;
import com.zzw.o2o.enums.ProductCategoryStateEnum;
import com.zzw.o2o.exception.ProductCategoryOperationException;
import com.zzw.o2o.exception.ProductOperationException;
import com.zzw.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/29 9:35
 */

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public List<ProductCategory> getProductCategoryList(Long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Override
    @Transactional
    public ProductCategoryExecution addProductCategory(List<ProductCategory> productCategoryList)
            throws ProductCategoryOperationException {
        //非空判断
        if(productCategoryList != null && productCategoryList.size() > 0){

            try {
                //批量增加ProductCategory
                int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
                if(effectedNum > 0){
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS, productCategoryList);
                }else{
                    return new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ProductCategoryOperationException("batchAddProductCategory Error: " + e.getMessage());
            }
        }else{
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }

    /**
     * 需要先将该商品目录下的商品的类别Id置为空，然后再删除该商品目录， 因此需要事务控制@Transactional
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws ProductCategoryOperationException
     */
    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException {
        //第一步 需要先将该商品目录下的商品的类型Id置为空,解除tb_product里的商品与该productCategoryId关联
        try {
            int effectNum = productDao.updateProductCategoryToNull(productCategoryId, shopId);
            if(effectNum < 0){
                throw new ProductOperationException("商品类型更新失败");
            }
        } catch (ProductOperationException e) {
            throw new ProductOperationException(e.getMessage());
        }

        //第二步，删除该商品目录,删除该productCategory

        try {
            int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
            if(effectedNum > 0){
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }else{
                throw new ProductCategoryOperationException("删除商品类型失败");
            }
        } catch (Exception e) {
            throw new ProductCategoryOperationException("deleteProductCategory error : "  + e.getMessage());
        }
    }

}
