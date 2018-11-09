package com.zzw.o2o.web.frontend;

import com.zzw.o2o.dto.ProductExecution;
import com.zzw.o2o.entity.Product;
import com.zzw.o2o.entity.ProductCategory;
import com.zzw.o2o.entity.Shop;
import com.zzw.o2o.service.ProductCategoryService;
import com.zzw.o2o.service.ProductService;
import com.zzw.o2o.service.ShopService;
import com.zzw.o2o.util.HttPServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: zzw5005
 * date: 2018/11/3 20:05
 */

@Controller
@RequestMapping("/frontend")
public class ShopDetailController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;


    /**
     * 获取店铺信息以及该店铺下面的商品类型列表
     * @param request
     * @return
     */
    @RequestMapping(value="/listshopdetailpageinfo", method= RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShopDetailPageInfo(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //获取前端传过来的shopId
        long shopId = HttPServletRequestUtil.getLong(request,"shopId");
        Shop shop = null;
        List<ProductCategory> productCategoryList = null;
        if(shopId != -1){
            //获取店铺Id为shopId的店铺信息
            shop = shopService.getByShopId(shopId);
            //获取店铺下面的商品类型列表
            productCategoryList = productCategoryService.getProductCategoryList(shopId);
            modelMap.put("shop",shop);
            modelMap.put("productCategoryList",productCategoryList);
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap;
    }


    /**
     * 根据查询条件分页列出该店下面的所有商品
     * @param request
     * @return
     */
    @RequestMapping(value="/listproductsbyshop")
    @ResponseBody
    private Map<String, Object> listProductsByShop(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        int pageIndex = HttPServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttPServletRequestUtil.getInt(request, "pageSize");
        long shopId = HttPServletRequestUtil.getLong(request, "shopId");

        if((pageIndex > -1) && (pageSize > -1)){
            long productCategoryId = HttPServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttPServletRequestUtil.getString(request, "productName");
            Product productCondition = compactProductCondition4Search(shopId, productCategoryId, productName);
            //按照传入的查询条件以及分页信息返回相应的商品列表以及总数
            ProductExecution pe = productService.queryProductionList(productCondition, pageIndex, pageSize);
            modelMap.put("productList", pe.getProductList());
            modelMap.put("count",pe.getCount());
            modelMap.put("success",true);
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    /**
     * 根据查询条件,并将条件封装到ProductCondition对象里里返回
     * @param shopId
     * @param productCategoryId
     * @param productName
     * @return
     */
    private Product compactProductCondition4Search(long shopId, long productCategoryId, String productName){
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);

        if(productCategoryId != -1L){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }

        if(productName != null){
            productCondition.setProductName(productName);
        }
        productCondition.setEnableStatus(1);
        return productCondition;
    }
}
