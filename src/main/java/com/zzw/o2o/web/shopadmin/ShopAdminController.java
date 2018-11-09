package com.zzw.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * author: zzw5005
 * date: 2018/10/27 10:42
 */

/*
* 为了安全，我们将shopoperation.html 迁移到/o2o/src/main/webapp/WEB-INF/html/shop/shopoperation.html。
* 通过servlet来控制访问，这样的话，我们就需要来根据请求路径来进行页面的跳转
* 主要用来解析路由并转发到相应的html文件中
* */

@Controller
@RequestMapping("/shopadmin")
public class ShopAdminController {

    @RequestMapping(value = "/shopoperation", method = RequestMethod.GET)
    public String shopOperation(){
        //这里之所以没有添加后缀html，因为我们在web.xml中设置视图解析器的前缀和后缀
        //前缀是根目录，后缀是html
        //转发到店铺注册/编辑页面
        return "shop/shopoperation";
    }

    //涉及到两个页面，不允许直接访问，所以需要在Controller层配置路由转发
    @RequestMapping(value = "/shoplist")
    public String shopList(){
        //转发到店铺列表页面
        return "shop/shoplist"; //这里添加的路径的是对应的html，也就是跳转到shoplist.html页面上
    }


    @RequestMapping(value = "/shopmanagement", method = RequestMethod.GET)
    //转发到店铺管理页面
    public String shopManagement() {
        return "shop/shopmanagement";
    }


    @RequestMapping(value="/productcategorymanage", method=RequestMethod.GET)
    //转发到商品类型管理页面
    public String productCategoryManage(){
        return "shop/productcategorymanage";
    }


    @RequestMapping(value="/productoperation", method=RequestMethod.GET)
    //转发到商品添加/编辑页面
    public String productManage(){
        return "shop/productoperation";
    }

    @RequestMapping(value="/productmanagement", method=RequestMethod.GET)
    //转发到商品管理页面
    public String productManagement(){
        return "shop/productmanagement";
    }

    @RequestMapping(value="/localauthlogin", method= RequestMethod.GET)
    //转发到用户登陆页面
    public String localAuthLogin(){
        return "shop/localauthlogin";
    }

}
