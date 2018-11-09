package com.zzw.o2o.entity;

import java.util.Date;

/**
 * author: zzw5005
 * date: 2018/10/24 15:44
 */

/*
* 商品图片
* */
public class ProductImg {
    private Long productImgId;
    private String imgAddr; //图片的地址
    private String imgDesc; //图片的描述
    private Integer priority; //图片的权重，越大越在前面显示
    private Date createTime;
    //这里只需要一个product中的productId属性，不需要其他属性，
    //所以这里直接使用Long类型的productId，而没有使用Product
    private Long productId;

    public Long getProductImgId() {
        return productImgId;
    }

    public void setProductImgId(Long productImgId) {
        this.productImgId = productImgId;
    }

    public String getImgAddr() {
        return imgAddr;
    }

    public void setImgAddr(String imgAddr) {
        this.imgAddr = imgAddr;
    }

    public String getImgDesc() {
        return imgDesc;
    }

    public void setImgDesc(String imgDesc) {
        this.imgDesc = imgDesc;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
