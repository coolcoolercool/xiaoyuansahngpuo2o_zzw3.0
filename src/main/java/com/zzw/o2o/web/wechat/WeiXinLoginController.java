package com.zzw.o2o.web.wechat;

import com.zzw.o2o.dto.ShopExecution;
import com.zzw.o2o.dto.WechatAuthExecution;
import com.zzw.o2o.entity.PersonInfo;
import com.zzw.o2o.entity.WechatAuth;
import com.zzw.o2o.enums.WechatAuthStateEnum;
import com.zzw.o2o.service.PersonInfoService;
import com.zzw.o2o.service.ShopAuthMapService;
import com.zzw.o2o.service.ShopService;
import com.zzw.o2o.service.WechatAuthService;
import com.zzw.o2o.util.weixin.WeiXinUser;
import com.zzw.o2o.util.weixin.WeiXinUserUtil;
import com.zzw.o2o.util.weixin.WeixinUtil;
import com.zzw.o2o.util.weixin.message.pojo.UserAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author: zzw5005
 * date: 2018/11/5 21:20
 */

@Controller
@RequestMapping("wechatlogin")
/*
* 从微信菜单点击调用的接口,可以在url里面增加参数(role_type)来表明是从商家还是玩家按钮进来的,
* 依次区分登陆后跳转不同页面
* 玩家会跳转到index.html页面
* 商家如果没有注册,会跳转到注册页面,否则跳转到任务管理页面
* 如果是商家的授权用户登陆,会跳到授权店铺的任务管理页面
* */
public class WeiXinLoginController {

    private static Logger logger = LoggerFactory.getLogger(WeiXinLoginController.class);

    @Resource
    private PersonInfoService personInfoService;

    @Resource
    private WechatAuthService wechatAuthService;

    @Resource
    private ShopService shopService;

    @Resource
    private ShopAuthMapService shopAuthMapService;

    private static final String FRONTEND = "1";
    private static final String SHOPEND = "2";

    @RequestMapping(value="/logincheck", method={RequestMethod.GET})
    public String doGet(HttpServletRequest request, HttpServletResponse response){
        logger.debug("weixin login get...");
        String code = request.getParameter("code");
        String roleType = request.getParameter("state");
        logger.debug("weixin login code: " + code);
        WechatAuth auth = null;
        WeiXinUser user = null;
        String openId = null;
        if(null != code){
            UserAccessToken token;

            try {
                token = WeiXinUserUtil.getUserAccessToken(code);
                logger.debug("weixin login token: " + user.toString());
                String accessToken = token.getAccessToken();
                openId = token.getOpenId();
                user = WeiXinUserUtil.getUserInfo(accessToken, openId);
                logger.debug("weixin login user: " + user.toString());
                request.getSession().setAttribute("openId", openId);
                auth = wechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e) {
                logger.error("error in getUserAccessToken or getUserInfo or findByOpenId: " +
                        e.toString());
                e.printStackTrace();
            }
        }

        logger.debug("weixin login success.");
        logger.debug("login role_type:" + roleType);
        if(FRONTEND.equals(roleType)){
            PersonInfo personInfo = WeiXinUserUtil.getPersonInfoFromRequest(user);
            if(auth == null){
                personInfo.setCustomerFlag(1);
                auth = new WechatAuth();
                auth.setOpenId(openId);
                auth.setPersonInfo(personInfo);
                WechatAuthExecution we = wechatAuthService.register(auth, null);
                if(we.getState() != WechatAuthStateEnum.SUCCESS.getState()){
                    return null;
                }
            }
            personInfo = personInfoService.getPersonInfoById(auth.getUserId());
            request.getSession().setAttribute("user", personInfo);
            return "frontend/index";
        }
        if(SHOPEND.equals(roleType)){
            PersonInfo personInfo = null;
            WechatAuthExecution we = null;
            if(auth == null){
                auth = new WechatAuth();
                auth.setOpenId(openId);
                personInfo = WeiXinUserUtil.getPersonInfoFromRequest(user);
                personInfo.setShopOwnerFlag(1);
                auth.setPersonInfo(personInfo);
                we = wechatAuthService.register(auth, null);
                if(we.getState() != WechatAuthStateEnum.SUCCESS.getState()){
                    return null;
                }
            }
            personInfo = personInfoService.getPersonInfoById(auth.getUserId());
            request.getSession().setAttribute("user", personInfo);
            ShopExecution se = shopService.getByEmployeeId(personInfo.getUserId());
            request.getSession().setAttribute("user",personInfo);
            if(se.getShopList() != null || se.getShopList().size() <= 0){
                return "shop/registershop";
            }else{
                request.getSession().setAttribute("shopList",se.getShopList());
                return "shop/shoplist";
            }
        }
        return null;
    }
}
