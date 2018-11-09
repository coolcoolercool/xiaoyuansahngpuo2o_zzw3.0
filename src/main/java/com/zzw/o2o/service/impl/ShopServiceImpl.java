package com.zzw.o2o.service.impl;

import com.zzw.o2o.dao.ShopDao;
import com.zzw.o2o.dto.ImageHolder;
import com.zzw.o2o.dto.ShopExecution;
import com.zzw.o2o.entity.Shop;
import com.zzw.o2o.enums.ShopStateEnum;
import com.zzw.o2o.exception.ShopOperationException;
import com.zzw.o2o.service.ShopService;
import com.zzw.o2o.util.FileUtil;
import com.zzw.o2o.util.ImageUtil;
import com.zzw.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/26 9:43
 */

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution getByEmployeeId(long employeeId)
            throws RuntimeException {
        List<Shop> shopList = shopDao.queryByEmployeeId(employeeId);
        ShopExecution se = new ShopExecution();
        se.setShopList(shopList);
        return se;
    }

    @Override
    @Transactional
    public ShopExecution modifyShop(Shop shop, ImageHolder imageHolder)
            throws ShopOperationException {
        if(shop == null || shop.getShopId() == null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }else{
            //1.判断是否需要处理图片
            try {
                //1.1 删除旧的图片
                if(imageHolder.getIns() != null && imageHolder.getFileName() != null && !"".equals(imageHolder.getFileName())){
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    if(tempShop.getShopImg() != null){
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    //1.2 用新的图片生成缩略图
                    addShopImg(shop, imageHolder);
                }
                //2.更新店铺信息
                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop);
                if(effectedNum <= 0){
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                }else{
                    shop = shopDao.queryByShopId(shop.getShopId());
                }
                return new ShopExecution(ShopStateEnum.SUCCESS, shop); //todo 不知道为什么这里shopExecution中的shop是null
            } catch (Exception e) {
                throw new ShopOperationException("modifyShop error : " + e.getMessage());
            }
        }
    }

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize); //从第几行开始取
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if(shopList != null){
            se.setShopList(shopList);
            se.setCount(count);
        }else{
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return se;
    }

    /**
     * 3个步骤需要在一个事务中,添加@Transactional注解
     *
     * 1. 将shop基本信息添加到数据库，返回shopId
     *
     * 2. 根据shopId创建目录,得到图片存储的相对路径
     *
     * 3. 将相对路径更新到数据库
     *
     * Spring默认情况下会对运行期例外(RunTimeException)进行事务回滚(RuntimeException以及子类会回滚，如果只是Exception，不会回滚)
     *
     * （1）注解@Transactional 只能应用到 public 方法才有效
     *
     * （2）在 Spring 的 AOP 代理下，只有目标方法由外部调用，目标方法才由 Spring成的代理对象来管理，这会造成自调用问题。
     * 若同一类中的其他没有@Transactional 注解的方法内部调用有@Transactional 注解的方法，
     * 有@Transactional注解的方法的事务被忽略，不会发生回滚。
     *
     * 上面的两个问题@Transactional 注解只应用到 public 方法和自调用问题，是由于使用 Spring AOP
     * 代理造成的。为解决这两个问题，可以使用 AspectJ 取代 Spring AOP 代理
     *
     * 在应用系统调用声明@Transactional 的目标方法时，Spring Framework 默认使用 AOP
     * 代理，在代码运行时生成一个代理对象，根据@Transactional 的属性配置信息，这个代理对象决定该声明@Transactional
     * 的目标方法是否由拦截器 TransactionInterceptor 来使用拦截，在 TransactionInterceptor
     * 拦截时，会在在目标方法开始执行之前创建并加入事务，并执行目标方法的逻辑,
     * 最后根据执行情况是否出现异常，利用抽象事务管理器AbstractPlatformTransactionManager 操作数据源
     * DataSource 提交或回滚事务
     */
    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点： 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作，RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
     */
    public ShopExecution addShop(Shop shop, ImageHolder imageHolder)
            throws RuntimeException {
        //非空判断
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            //步骤1 设置基本信息，插入shop
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());

            int effectedNum = shopDao.insertShop(shop);
            if (effectedNum <= 0) {
                throw new RuntimeException("店铺创建失败");
            } else { //店铺创建成功
                try {
                    //步骤2 添加成功，则继续处理文件，获取shopId，用于创建图片存放的目录
                    if (imageHolder.getIns() != null) {
                        addShopImg(shop, imageHolder);
                        //步骤3 更新tb_shop中 shop_img字段
                        effectedNum = shopDao.updateShop(shop);
                        if (effectedNum <= 0) {
                            throw new RuntimeException("创建图片地址失败");
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("addShopImg error: "
                            + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("insertShop error: " + e.getMessage());
        }
        //返回店铺的状态 审核中，以及店铺信息
        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    private void addShopImg(Shop shop, ImageHolder imageHolder) {
        String destPath = FileUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnails(imageHolder, destPath);
        shop.setShopImg(shopImgAddr);
    }
}
