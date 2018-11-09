package com.zzw.o2o.dto;

import com.zzw.o2o.entity.ProductCategory;
import com.zzw.o2o.enums.ProductCategoryStateEnum;

import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/29 15:30
 */

/*
* 封装操作ProductCategory的返回结果，包括操作状态和ProductCategory信息
* */
public class ProductCategoryExecution {
    private int state; //结果状态
    private String stateInfo; //状态标识
    //批量操作，所以使用list
    private List<ProductCategory> productCategoryList;

    private int count;

    /**
     * 空参默认构造器
     */
    public ProductCategoryExecution() {
    }

    /**
     * 操作成功的时候使用的构造函数，返回操作状态和ProductCategory的集合
     * @param stateEnum
     * @param productCategoryList
     */
    public ProductCategoryExecution(ProductCategoryStateEnum stateEnum,
                                    List<ProductCategory> productCategoryList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.productCategoryList = productCategoryList;
    }

    /**
     * 操作失败的时候返回的信息，仅包含状态和状态描述即可
     * @param stateEnum
     */
    public ProductCategoryExecution(ProductCategoryStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<ProductCategory> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
