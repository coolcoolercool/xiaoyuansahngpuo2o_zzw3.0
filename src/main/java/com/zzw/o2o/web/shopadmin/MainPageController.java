package com.zzw.o2o.web.shopadmin;

import com.zzw.o2o.entity.HeadLine;
import com.zzw.o2o.entity.ShopCategory;
import com.zzw.o2o.enums.HeadLineStateEnum;
import com.zzw.o2o.enums.ShopCategoryStateEnum;
import com.zzw.o2o.service.HeadLineService;
import com.zzw.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: zzw5005
 * date: 2018/10/31 20:50
 */

/*
* 主页面相关信息,比如头条,轮播图之类的
* */
@Controller
@RequestMapping(value="/frontend")
public class MainPageController {
    @Autowired
    private HeadLineService headLineService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    /**
     * 初始化前端系统的主页信息,包括获取一级店铺类型列表以及头条列表
     * @return
     */
    @RequestMapping(value="listmainpageinfo", method= RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listMainPageInfo(){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        List<HeadLine> headLineList = new ArrayList<HeadLine>();
        try {
            // 查询状态为1的可见的headLine信息
            HeadLine headLineConditon = new HeadLine();
            headLineConditon.setEnableStatus(1);
            headLineList = headLineService.queryHeadLineList(headLineConditon);

            modelMap.put("headLineList", headLineList);
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("errMsg", HeadLineStateEnum.INNER_ERROR.getStateInfo());
        }

        try{
            // 查询parentId为null的一级类别
            shopCategoryList = shopCategoryService.getShopCategoryList(null);
            modelMap.put("shopCategoryList", shopCategoryList);
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", ShopCategoryStateEnum.INNER_ERROR.getStateInfo());
        }

        modelMap.put("success", true);
        return modelMap;
    }




}
