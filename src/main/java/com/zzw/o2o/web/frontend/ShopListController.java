package com.zzw.o2o.web.frontend;

import com.zzw.o2o.dto.ShopExecution;
import com.zzw.o2o.entity.Area;
import com.zzw.o2o.entity.Shop;
import com.zzw.o2o.entity.ShopCategory;
import com.zzw.o2o.service.AreaService;
import com.zzw.o2o.service.ShopCategoryService;
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
 * date: 2018/11/3 9:32
 */

@Controller
@RequestMapping("/frontend")
public class ShopListController {
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ShopService shopService;

    /**
     * 返回商品列表里的ShopCategory列表(二级或者一级),以及区域信息列表
     * @param request
     * @return
     */
    @RequestMapping(value="/listshopspageinfo", method= RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShopsPageInfo(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //试着从前端请求中获取parentId
        long parentId = HttPServletRequestUtil.getLong(request, "parentId");
        List<ShopCategory> shopCategoryList = null;
        if(parentId != -1){
            //如果parentId存在,则取出该以及ShopCategory下面的二级ShopCategory列表
            try {
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryCondition.setParentId(parent);
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
            } catch (Exception e) {
                modelMap.put("success",false);
                modelMap.put("errMsg", e.getMessage());
            }
        }else{
            try {
                //如果parentId不存在,则取出所有一级ShopCategory(用户在首页选择的是全部商店列表)
                shopCategoryList = shopCategoryService.getShopCategoryList(null);
            } catch (Exception e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }
        modelMap.put("shopCategoryList",shopCategoryList);
        List<Area> areaList = null;
        try {
            areaList = areaService.getAreaList();
            modelMap.put("areaList", areaList);
            modelMap.put("success",true);
            return modelMap;
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    /**
     * 获取指定查询条件下的店铺列表
     * @param request
     * @return
     */
    @RequestMapping(value="/listshops", method=RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShops(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //获取页码
        int pageIndex = HttPServletRequestUtil.getInt(request, "pageIndex");
        //获取一页需要显示的数据条数
        int pageSize = HttPServletRequestUtil.getInt(request, "pageSize");
        //非空判断
        if((pageIndex > -1) && (pageSize > -1)){
            //试着获取一级类型Id
            long parentId = HttPServletRequestUtil.getLong(request, "parentId");
            //试着获取特定的二级类型
            long shopCategoryId = HttPServletRequestUtil.getLong(request, "shopCategoryId");
            //试着获取区域Id
            int areaId = HttPServletRequestUtil.getInt(request, "areaId");
            //试着获取模糊查询的名字
            String shopName = HttPServletRequestUtil.getString(request, "shopName");
            //获取组合以后的查询条件
            Shop shopCondition = compactShopCondition4Search(parentId, shopCategoryId, areaId, shopName);
            //根据查询条件和分页信息获取店铺列表,并返回总数
            ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);

            modelMap.put("shopList",se.getShopList());
            modelMap.put("count",se.getCount());
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageSize or pageIndex");
        }

        return modelMap;
    }

    /**
     * 组合查询条件,并将条件封装到ShopCategory对象里返回
     * @param parentId
     * @param shopCategoryId
     * @param areaId
     * @param shopName
     * @return
     */
    private Shop compactShopCondition4Search(long parentId, long shopCategoryId, long areaId, String shopName){
        Shop shopCondition = new Shop();
        if(parentId != -1L){
            //查询某个一级ShopCategory下面的所有二级ShopCategory里面的店铺列表
            //这里做了改动,采用网页的方法,添加了子类category
            ShopCategory childCategory = new ShopCategory();
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParentId(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        if(shopCategoryId != -1){
            //查询某个二级ShopCategory下面的店铺列表
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if(areaId != -1L){
            //查询位于某个区域Id下的店铺列表
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }

        if(shopName != null){
            //查询名字里包含shopName的店铺列表
            shopCondition.setShopName(shopName);
        }
        //前端展示的店铺都是审核成功的店铺
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }
}
