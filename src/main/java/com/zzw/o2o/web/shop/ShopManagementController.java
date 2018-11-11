package com.zzw.o2o.web.shop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzw.o2o.dto.ImageHolder;
import com.zzw.o2o.dto.ShopExecution;
import com.zzw.o2o.entity.Area;
import com.zzw.o2o.entity.PersonInfo;
import com.zzw.o2o.entity.Shop;
import com.zzw.o2o.entity.ShopCategory;
import com.zzw.o2o.enums.ShopStateEnum;
import com.zzw.o2o.service.AreaService;
import com.zzw.o2o.service.ShopCategoryService;
import com.zzw.o2o.service.ShopService;
import com.zzw.o2o.util.CodeUtil;
import com.zzw.o2o.util.HttPServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: zzw5005
 * date: 2018/10/27 8:52
 */

/*
* 实现店铺管理的相关逻辑
* */
@Controller
@RequestMapping("/shopadmin") //这里采用了网页的路径，视频中的路径为shop
public class ShopManagementController {
    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private AreaService areaService;

    /**
     * 从商铺列表页面中，点击“进入”按钮进入
     * 某个商铺的管理页面的时候，对session中的数据的校验从而进行页面的跳转，
     * 是否跳转到店铺列表页面或者可以直接操作该页面
     * 访问形式如下 http://ip:port/o2o/shopadmin/shopmanagement?shopId=xxx
     * @param request
     * @return
     */
    @RequestMapping(value="/getshopmanagementinfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopManagementInfo(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //获取shopId
        long shopId = HttPServletRequestUtil.getLong(request, "shopId");

        if(shopId < 0){
            //尝试从当前session中获取
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            if(currentShop == null){
                //如果当前session中也没有shop信息，告诉view层，重定向到之前的页面
                modelMap.put("redirect",true);
                modelMap.put("url","/o2o/shopadmin/shoplist");
            }else{
                //告诉view层，进入该页面
                modelMap.put("redirect", false);
                modelMap.put("shopId", currentShop.getShopId());
            }
        }else{ //shopId合法的话
            Shop shop = new Shop();
            shop.setShopId(shopId);
            //将currentShop放入到session中
            request.getSession().setAttribute("currentShop", shop);
            modelMap.put("redirect",false);
        }
        return modelMap;
    }

    /**
     * 从session中获取
     * @param request
     * @return
     */
    @RequestMapping(value="/getshoplist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopList(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        PersonInfo user = new PersonInfo();
/*        user.setUserId(8L);
        user.setName("夏天的风");
        request.getSession().setAttribute("user", user);*/
        user = (PersonInfo) request.getSession().getAttribute("user");

        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
            modelMap.put("shopList", se.getShopList());

            //拦截器添加语句,列出店铺成功以后,将店铺放入session中作为权限验证依据,即该账号只能操作它自己的店铺
            request.getSession().setAttribute("shoplist", se.getShopList());

            modelMap.put("user", user);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    /**
     * 根据shopId获取shop信息， 接收前端的请求，获取shopId ，所以入参为HttpServletReques
     * 不需要VIEW展现层模块，直接显示到客户端的内容。 将内容或对象作为 HTTP 响应正文返回
     * @param request
     * @return
     */
    @RequestMapping(value="/getshopinfobyid", method=RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopInfoId(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();

        //shopId为和前端约定好的变量
        Long shopId = HttPServletRequestUtil.getLong(request, "shopId");

        try {
            if(shopId >= 0){
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();

                modelMap.put("success",true);
                modelMap.put("shop",shop);
                modelMap.put("areaList", areaList);
            }else{
                modelMap.put("success", false);
                modelMap.put("errMsg","shopId不合法");
            }
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value="/modifyshop", method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyShop(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<String, Object>();

        // 0. 验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码不正确");
            return modelMap;
        }
        // 1. 接收并转换相应的参数，包括shop信息和图片信息
        // 1.1 shop信息

        // shopStr 是和前端约定好的参数值，后端从request中获取request这个值来获取shop的信息
        String shopStr = HttPServletRequestUtil.getString(request, "shopStr");
        // 使用jackson-databind 将json转换为pojo
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            // 将json转换为pojo
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            e.printStackTrace();
            // 将错误信息返回给前台
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        // 1.2 图片信息 基于Apache Commons FileUpload的文件上传 （ 修改商铺信息 图片可以不更新）

        // Spring MVC中的 图片存在CommonsMultipartFile中
        CommonsMultipartFile shopImg = null;
        // 从request的本次会话中的上线文中获取图片的相关内容
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // shopImg是和前端约定好的变量名
            shopImg = (CommonsMultipartFile) multipartRequest.getFile("shopImg");
        }

        // 2. 修改店铺
        if (shop != null && shop.getShopId() != null) {
            // Session 部分的 PersonInfo 修改商铺是不需要的设置的。
            // 修改店铺
            ShopExecution se = null;
            try {
                if (shopImg != null) {
                    se = shopService.modifyShop(shop, new ImageHolder(shopImg.getInputStream(), shopImg.getOriginalFilename()));
                } else {
                    se = shopService.modifyShop(shop, null);
                }
                // 成功
                if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    modelMap.put("errMsg", "修改成功");
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (Exception e) {
                e.printStackTrace();
                modelMap.put("success", false);
                modelMap.put("errMsg", "ModifyShop Error");
            }
        } else {
            // 将错误信息返回给前台
            modelMap.put("success", false);
            modelMap.put("errMsg", "ShopId不合法");
        }
        return modelMap;
    }


    /**
     * 初始化区域信息和shopCategory信息，返回给前端表单页面
     * @return
     */
    //这里是对应前端的 var intiUrl = '/o2o/shopadmin/getshopinitinfo'
    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo(){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList = null;
        List<Area> areaList = null;
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            // 返回success shopCategoryList  areaList,前端通过 data.success来判断从而展示shopCategoryList和areaList的数据
            modelMap.put("success", true);
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }


    @RequestMapping(value = "/registershop", method= RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<>();
        //验证码识别
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        //1.接收并转化成相应的参数，包括店铺信息以及图片信息

        //1.1shop信息
        //shopStr是和前端约定好的参数值，后端从request中获取request这个值来获取shop的信息
        String shopStr = HttPServletRequestUtil.getString(request, "shopStr");
        //使用jackson-databind将json转换成pojo中的shop
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;

        try {
            //将前端的json转换成后端的shop实体类
            shop = mapper.readValue(shopStr,Shop.class);
        } catch (IOException e) {
            //返回给前端的错误信息
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        CommonsMultipartFile shopImg = null;
        //从request的本次会话中获取图片的相关内容
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());

        if(commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            //shopImg是前端约定好的变量名
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }else{
            //将错误信息返回给前端
            modelMap.put("success",false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }

        //2.注册店铺
        if(shop != null && shopImg != null){
            //店主信息personInfo的信息，肯定登陆才能注册店铺
            //这部分我们从session中获取，尽量不依赖前端，
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);

            ShopExecution se = null;
            try {
                se = shopService.addShop(shop, new ImageHolder(shopImg.getInputStream(), shopImg.getOriginalFilename()));
                if(se.getState() == ShopStateEnum.CHECK.getState()){
                    modelMap.put("success",true);
                    //该用户可以操作的店铺列表
                    @SuppressWarnings("unchecked")
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if(shopList == null || shopList.size() == 0){
                        shopList = new ArrayList<>();
                    }
                    shopList.add(se.getShop());
                    request.getSession().setAttribute("shopList",shopList);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg", se.getStateInfo());
            }
            return modelMap;
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
    }

}
