package com.zzw.o2o.web.shop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzw.o2o.dto.ImageHolder;
import com.zzw.o2o.dto.ProductExecution;
import com.zzw.o2o.entity.Product;
import com.zzw.o2o.entity.ProductCategory;
import com.zzw.o2o.entity.Shop;
import com.zzw.o2o.enums.ProductStateEnum;
import com.zzw.o2o.exception.ProductOperationException;
import com.zzw.o2o.service.ProductCategoryService;
import com.zzw.o2o.service.ProductService;
import com.zzw.o2o.util.CodeUtil;
import com.zzw.o2o.util.HttPServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: zzw5005
 * date: 2018/10/30 16:12
 */


@Controller
//@RequestMapping("/shopadmin")
public class ProductManagementController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;
    // 最大上传图片数量
    private static final int IMAGEMAXCOUNT = 6;

    @RequestMapping(value="/getproductlist",method=RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> queryProductList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        int pageIndex = HttPServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttPServletRequestUtil.getInt(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute(
                "currentShop");
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null)
                && (currentShop.getShopId() != null)) {
            long productCategoryId = HttPServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttPServletRequestUtil.getString(request, "productName");
            Product productCondition = compactProductCondition4Search(
                    currentShop.getShopId(), productCategoryId, productName);
            ProductExecution pe = productService.queryProductionList(productCondition, pageIndex, pageSize);
            modelMap.put("productList", pe.getProductList());
            modelMap.put("count", pe.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }


    /**
     * 组合查询条件
     * @param shopId
     * @param productCategoryId
     * @param productName
     * @return
     */
    private Product compactProductCondition4Search(Long shopId, long productCategoryId, String productName){
        Product productCondition = new Product();
        //这里shop一定是非空的，因为在操作的时候，会获取包含shop信息的session
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        //若有指定类型的要求，则添加进去
        if(productCategoryId != -1L){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        //若有商品模糊查询的要求，则添加进去
        if(productName != null){
            productCondition.setProductName(productName);
        }
        return productCondition;
    }

    /**
     * 根据productId，返回该product的相关信息
     * 因为只需要传入productId，使用@RequestParam注解。同时也无需传入HttpServletRequest，用不到。
     * 根据页面原型，不仅要加载prodcut的信息，还需要加载对应的目录信息。
     * @param productId
     * @return
     */
    @RequestMapping(value="/getproductbyid", method=RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getProductById(@RequestParam long productId){
        Map<String, Object> modelMap = new HashMap<>();
        //非空判断
        if(productId > -1){
            //获取相片信息
            Product product = productService.queryProductById(productId);
            //获取该店铺下的商品类型列表
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
            modelMap.put("product",product);
            modelMap.put("productCategoryList",productCategoryList);
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }


    @RequestMapping(value="/modifyproduct", method=RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyProduct(HttpServletRequest request){
        //这里 关于上下功能的解决 statusChange 这个变量的处理,以及前端也需要传入这个变量 todo
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        Product product = null;
        // 接收前端传递过来的product
        String productStr = null;
        // 商品图片缩略图（输入流和名称的封装类）
        ImageHolder thumbnail = null;

        // 将HttpServletRequest转型为MultipartHttpServletRequest，可以很方便地得到文件名和文件内容
        MultipartHttpServletRequest multipartHttpServletRequest = null;
        // 接收商品缩略图
        CommonsMultipartFile thumbnailFile = null;
        // 接收商品详情图片
        List<ImageHolder> productDetailImgList = new ArrayList<ImageHolder>();

        // 创建一个通用的多部分解析器
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        // Step2: 使用FastJson提供的api,实例化Product 构造调用service层的第一个参数
        ObjectMapper mapper = new ObjectMapper();
        // 获取前端传递过来的product,约定好使用productStr
        try {
            productStr = HttPServletRequestUtil.getString(request, "productStr");
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        // Step3: 商品缩略图 和 商品详情图 构造调用service层的第二个参数和第三个参数
        try {
            // 判断 request 是否有文件上传,即多部分请求
            if (commonsMultipartResolver.isMultipart(request)) {
                // 将request转换成多部分request
                multipartHttpServletRequest = (MultipartHttpServletRequest) request;

                // 得到缩略图的CommonsMultipartFile ,和前端约定好使用thumbnail 传递
                // ，并构建ImageHolder对象
                thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
                // 转化为ImageHolder，使用service层的参数类型要求
                thumbnail = new ImageHolder(thumbnailFile.getInputStream(), thumbnailFile.getOriginalFilename());

                // 得到 商品详情的列表，和前端约定使用productImg + i 传递 ,并构建ImageHolder对象
                for (int i = 0; i < IMAGEMAXCOUNT; i++) {
                    CommonsMultipartFile productDetailImgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg" + i);
                    if (productDetailImgFile != null) {
                        ImageHolder productDetailImg = new ImageHolder(productDetailImgFile.getInputStream(), productDetailImgFile.getOriginalFilename());
                        productDetailImgList.add(productDetailImg);
                    } else {
                        // 如果从请求中获取的到file为空，终止循环
                        break;
                    }
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传图片不能为空");
                return modelMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        // Step4 调用Service层
        if (product != null && thumbnailFile != null && productDetailImgList.size() > 0) {
            try {
                // 从session中获取shop信息，不依赖前端的传递更加安全
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                // 调用addProduct
                ProductExecution pe = productService.modifyProduct(product, thumbnail, productDetailImgList);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (ProductOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;

    }


    @RequestMapping(value="/changestatus", method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> changeStatus(HttpServletRequest request){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        // 获取前端传递过来的product,约定好使用productStr
        try {
            String productStr = HttPServletRequestUtil.getString(request, "productStr");
            Product product = mapper.readValue(productStr, Product.class);

            Shop tempShop = (Shop) request.getSession().getAttribute("currentShop");
            product.setShop(tempShop);

            ProductExecution pe = productService.modifyProduct(product, null, null);
            if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
                modelMap.put("errMsg", "操作成功");
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", pe.getStateInfo());
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        return modelMap;

    }

    /**
     * @Title: addProduct
     *
     * @Description: 1. 验证码校验
     *
     *               2. 接收前端参数：包括 商品、 商品缩略图、商品详情图片实体类
     *
     *               前端页面通过post方式传递一个包含文件上传的Form会以multipart/form-data请求发送给服务器，
     *
     *               需要告诉DispatcherServlet如何处理MultipartRequest，我们在spring-web.
     *               xml中定义了multipartResolver。
     *
     *               如果某个Request是一个MultipartRequest，它就会首先被MultipartResolver处理，
     *               然后再转发相应的Controller。
     *
     *               在Controller中，
     *               将HttpServletRequest转型为MultipartHttpServletRequest
     *               ，可以非常方便的得到文件名和文件内容
     *
     * @param request
     *
     * @return: Map<String,Object>
     *
     *          注解@ResponseBody 负责将返回的map对象转换为JSON,供前端使用
     */
    @RequestMapping(value = "/addproduct", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Product product = null;
        // 接收前端传递过来的product
        String productStr = null;
        // 商品图片缩略图（输入流和名称的封装类）
        ImageHolder thumbnail = null;

        // 将HttpServletRequest转型为MultipartHttpServletRequest，可以很方便地得到文件名和文件内容
        MultipartHttpServletRequest multipartHttpServletRequest = null;
        // 接收商品缩略图
        CommonsMultipartFile thumbnailFile = null;
        // 接收商品详情图片
        List<ImageHolder> productDetailImgList = new ArrayList<ImageHolder>();

        // 创建一个通用的多部分解析器
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        // Step1:校验验证码
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码不正确");
            return modelMap;
        }

        // Step2: 使用FastJson提供的api,实例化Product 构造调用service层的第一个参数
        ObjectMapper mapper = new ObjectMapper();
        // 获取前端传递过来的product,约定好使用productStr
        try {
            productStr = HttPServletRequestUtil.getString(request, "productStr");
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        // Step3: 商品缩略图 和 商品详情图 构造调用service层的第二个参数和第三个参数
        try {
            // 判断 request 是否有文件上传,即多部分请求
            if (commonsMultipartResolver.isMultipart(request)) {
                // 将request转换成多部分request
                multipartHttpServletRequest = (MultipartHttpServletRequest) request;

                // 得到缩略图的CommonsMultipartFile ,和前端约定好使用thumbnail 传递
                // ，并构建ImageHolder对象
                thumbnailFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
                // 转化为ImageHolder，使用service层的参数类型要求
                thumbnail = new ImageHolder(thumbnailFile.getInputStream() ,thumbnailFile.getOriginalFilename());

                // 得到 商品详情的列表，和前端约定使用productImg + i 传递 ,并构建ImageHolder对象
                for (int i = 0; i < IMAGEMAXCOUNT; i++) {
                    CommonsMultipartFile productDetailImgFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg" + i);
                    if (productDetailImgFile != null) {
                        ImageHolder productDetailImg = new ImageHolder(productDetailImgFile.getInputStream(),productDetailImgFile.getOriginalFilename());
                        productDetailImgList.add(productDetailImg);
                    }else{
                        // 如果从请求中获取的到file为空，终止循环
                        break;
                    }
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传图片不能为空");
                return modelMap;
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        // Step4 调用Service层
        if (product != null && thumbnailFile != null && productDetailImgList.size() > 0) {
            try {
                // 从session中获取shop信息，不依赖前端的传递更加安全
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                // 调用addProduct
                ProductExecution pe = productService.addProduct(product, thumbnail, productDetailImgList);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (ProductOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;
    }

}
