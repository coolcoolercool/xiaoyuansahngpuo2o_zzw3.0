package com.zzw.o2o.entity;

import java.util.Date;
import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/24 15:27
 */

/*
* 店铺实体类
* */
public class Shop {
    private Long shopId;
    private Long shopCategoryId;
    private Long ownerId;

    private String shopName;
    private String shopDesc;
    private String shopAddr;
    private String phone;
    //店铺图片的地址路径
    private String shopImg;

    //这里两个可以无视，一般也不会用到
    private Double longitude;
    private Double latitude;

    private Integer priority;
    private Date createTime;
    private Date lastEditTime;

    private Integer enableStatus; //-1不可用 0审核中 1可用
    private String advice;  //超级管理员给店家的建议
    private PersonInfo owner;

    private List<ShopAuthMap> staffList;
    private Area area;
    private ShopCategory shopCategory;
    private ShopCategory parentCategory;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public PersonInfo getOwner() {
        return owner;
    }

    public void setOwner(PersonInfo owner) {
        this.owner = owner;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(Long shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    public String getShopAddr() {
        return shopAddr;
    }

    public void setShopAddr(String shopAddr) {
        this.shopAddr = shopAddr;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
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

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }


    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public List<ShopAuthMap> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<ShopAuthMap> staffList) {
        this.staffList = staffList;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public ShopCategory getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(ShopCategory shopCategory) {
        this.shopCategory = shopCategory;
    }

    public ShopCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(ShopCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "shopId=" + shopId +
                ", shopCategoryId=" + shopCategoryId +
                ", ownerId=" + ownerId +
                ", shopName='" + shopName + '\'' +
                ", shopDesc='" + shopDesc + '\'' +
                ", shopAddr='" + shopAddr + '\'' +
                ", phone='" + phone + '\'' +
                ", shopImg='" + shopImg + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", priority=" + priority +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                ", enableStatus=" + enableStatus +
                ", advice='" + advice + '\'' +
                ", owner=" + owner +
                ", staffList=" + staffList +
                ", area=" + area +
                ", shopCategory=" + shopCategory +
                ", parentCategory=" + parentCategory +
                '}';
    }
}
