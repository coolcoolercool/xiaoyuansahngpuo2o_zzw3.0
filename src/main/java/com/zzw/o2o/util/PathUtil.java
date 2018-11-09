package com.zzw.o2o.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * author: zzw5005
 * date: 2018/10/25 22:15
 */

@Configuration
public class PathUtil {
    private static String separator = System.getProperty("file.separator");

    private static String winPath;

    private static String linuxPath;

    private static String shopPath;

    @Value("${win.base.path}")
    public void setWinPath(String winPath) {
        PathUtil.winPath = winPath;
    }
    @Value("${linux.base.path}")
    public void setLinuxPath(String linuxPath) {
        PathUtil.linuxPath = linuxPath;
    }
    @Value("${shop.relevant.path}")
    public void setShopPath(String shopPath) {
        PathUtil.shopPath = shopPath;
    }

    /**
     * 根据不同的操作系统，获取图片的存放路径
     * @return 图片储存堆的根路径
     */
    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basePath = "";
        if(os.toLowerCase().startsWith("win")){
            //basePath = "F:\\javaCode\\OfferProduct\\xioayuanshangpu\\Image\\win";
            basePath = winPath;
        }else{
            basePath="F:\\javaCode\\OfferProduct\\xioayuanshangpu\\Image\\others\\";
        }
        basePath = basePath.replace("/", separator);
        return basePath;
    }

    /**
     * 获取指定商铺的图片的路径，根据shopId来区分
     * @param shopId 商铺的ID
     * @return
     */
    public static String getShopImagePath(Long shopId){
        StringBuilder shopImgPathBuilder = new StringBuilder();
        shopImgPathBuilder.append("/upload/image/item/shop/");
        shopImgPathBuilder.append(shopId);
        shopImgPathBuilder.append("/");
        String shopImgPath = shopImgPathBuilder.toString().replace("/", separator);
        return shopImgPath;   //  /upload/image/item/shop/32/
    }
}
