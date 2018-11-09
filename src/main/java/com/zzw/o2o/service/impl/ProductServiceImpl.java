package com.zzw.o2o.service.impl;

import com.zzw.o2o.dao.ProductDao;
import com.zzw.o2o.dao.ProductImgDao;
import com.zzw.o2o.dto.ImageHolder;
import com.zzw.o2o.dto.ProductExecution;
import com.zzw.o2o.entity.Product;
import com.zzw.o2o.entity.ProductImg;
import com.zzw.o2o.enums.ProductStateEnum;
import com.zzw.o2o.exception.ProductOperationException;
import com.zzw.o2o.service.ProductService;
import com.zzw.o2o.util.FileUtil;
import com.zzw.o2o.util.ImageUtil;
import com.zzw.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/30 8:41
 */


@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    /**
     *
     * @param productId
     * @return
     */
    @Override
    public Product queryProductById(Long productId) {
        return productDao.selectProductById(productId);
    }

    /**
     * 1.若缩略图参数有值，则处理缩略图。若原先存在若略图则先删除再添加新图，之后获取缩略图相对路径并赋值给product
     * 2.若院系那商品详情图列表参数有值，对商品详情图列表进行同样的操作
     * 3.将tb_product_img下面的该商品原先的商品详情图记录全部清除
     * 4.更新tb_product和tb_product_img的信息
     * @param product
     * @param imageHolder
     * @param prodImgDetailList
     * @return
     * @throws ProductOperationException
     */
    @Override
    @Transactional
    public ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> prodImgDetailList)
            throws ProductOperationException {
        //空值判断
        if(product != null && product.getShop() != null  && product.getShop().getShopId() != null){  //删除了这个判断条件 product.getProductCategory().getProductCategoryId() != null
            //给商品设置上默认属性
            product.setLastEditTime(new Date());

            if(imageHolder != null){
                Product tempProduct = productDao.selectProductById(product.getProductId());

                if(tempProduct.getImgAddr() != null){
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                addProductImg(product, imageHolder);
            }

            //如果有新存入的商品详情图，则将原先的删除，并添加新的图片
            if(prodImgDetailList != null && prodImgDetailList.size() > 0){
                deleteProductImgs(product.getProductId());
                addProductDetailImgs(product, prodImgDetailList);
            }

            try {
                //更新商品信息
                int effectNum = productDao.updateProduct(product);
                if(effectNum <= 0){
                    throw new ProductOperationException("商品信息更新失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            } catch (ProductOperationException e) {
                throw new ProductOperationException("商品信息更新失败: " + e.getMessage());
            }
        }else{
            return new ProductExecution(ProductStateEnum.NULL_PARAMETER);
        }
    }

    private void deleteProductImgs(Long productId){
        //根据productId获取原来的图片
        List<ProductImg> productImgList = productImgDao.selectProductImgList(productId);
        //删除原来的图片
        for(ProductImg productImg : productImgList){
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
        }
        //删除数据库里原有的图片信息
        productImgDao.deleteProductImgById(productId);
    }

    /**
     * 支持分页功能的查询product
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws ProductOperationException
     */
    @Override
    public ProductExecution queryProductionList(Product productCondition, int pageIndex, int pageSize) throws ProductOperationException {
        List<Product> productList = null;
        int count = 0;

        try {
            //页码转换成数据库的行码，并调用dao层取回指定页码的商品列表
            int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            productList = productDao.selectProductList(productCondition, rowIndex, pageSize);
            //基于同样的查询条件返回该查询条件下的商品总数
            count = productDao.selectCountProduct(productCondition);
        } catch (Exception e) {
            e.printStackTrace();
            new ProductExecution(ProductStateEnum.INNER_ERROR);
        }

        return new ProductExecution(ProductStateEnum.SUCCESS, productList, count);
    }

    /**
     * 按照四步进行处理
     * 1.处理商品的缩略图，获取相对路径，为了调用dao层的时候写入 tb_product中的 img_addr字段有值
     * 2.写入tb_product ，获取product_id
     * 3.集合product_id 批量处理商品详情图片
     * 4.将商品详情图片 批量更新到 tb_product_img表
     * @param product
     * @param imageHolder
     * @param prodImgDetailList
     * @return
     * @throws ProductOperationException
     */
    @Override
    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> prodImgDetailList)
            throws ProductOperationException {
        if(product != null && product.getShop() != null && product.getShop().getShopId() != null
        && product.getProductCategory().getProductCategoryId() != null){
            //设置默认的属性1展示
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            product.setEnableStatus(1); //默认为上架的状态

            //如果文件的输入流和文件名不为空，添加文件到特定目录，并将相对路径设置给product，
            //这样product就有了ImgAddr，为下一步的插入tr_product提供了数据来源
            if(imageHolder != null){
                addProductImg(product, imageHolder);
            }

            try {
                //创建商品的信息
                int effectNum = productDao.insertProduct(product);
                if(effectNum <= 0){
                    throw new ProductOperationException("商品创建失败");
                }
                //若商品详情图不为空则添加
                if(prodImgDetailList != null && prodImgDetailList.size() > 0){
                    addProductDetailImgs(product, prodImgDetailList);
                }
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            } catch (Exception e) {
                throw new ProductOperationException("创建商品失败: " + e.getMessage());
            }
        }else{
            return new ProductExecution(ProductStateEnum.NULL_PARAMETER);
        }
    }



    private void addProductImg(Product product, ImageHolder imageHolder){
        //根据ShopId获取图片存储的相对路径
        String relativePath = FileUtil.getShopImagePath(product.getShop().getShopId());
        //添加图片到指定的目录
        String relativeAddr = ImageUtil.generateThumbnails(imageHolder, relativePath);

        product.setImgAddr(relativeAddr);
    }

    /**
     * 处理详情图片，并写入tb_product_img
     * @param product
     * @param prodImgDetailList
     */
    private void addProductDetailImgs(Product product, List<ImageHolder> prodImgDetailList){
        String relativePath = FileUtil.getShopImagePath(product.getShop().getShopId());

        List<String> imgAddrList = ImageUtil.generateNormalImgs(prodImgDetailList, relativePath);

        if(imgAddrList != null && imgAddrList.size() > 0){
            List<ProductImg> productImgList = new ArrayList<>();
            for(String imgAddr : imgAddrList){
                ProductImg productImg = new ProductImg();
                productImg.setImgAddr(imgAddr);
                productImg.setProductId(product.getProductId());
                productImg.setCreateTime(new Date());
                productImgList.add(productImg);
            }

            //todo 这里与原来是06:04处的代码有所不同
            try {
                int effectNum = productImgDao.batchInsertProductImg(productImgList);
                if(effectNum <= 0){
                    throw new ProductOperationException("创建商品详情图片失败");
                }
            } catch (ProductOperationException e) {
                e.printStackTrace();
                throw new ProductOperationException("创建商品详情图片失败" + e.toString());
            }
        }
    }
}

/*
*
10:29:52.603 [http-apr-8080-exec-9] DEBUG org.mybatis.spring.SqlSessionUtils - Creating a new SqlSession
10:29:52.611 [http-apr-8080-exec-9] DEBUG org.mybatis.spring.SqlSessionUtils - SqlSession
[org.apache.ibatis.session.defaults.DefaultSqlSession@1f005e72] was not registered for synchronization because synchronization is not active
* */
