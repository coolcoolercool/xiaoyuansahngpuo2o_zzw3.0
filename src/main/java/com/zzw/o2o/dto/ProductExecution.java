package com.zzw.o2o.dto;

import com.zzw.o2o.entity.Product;
import com.zzw.o2o.enums.ProductStateEnum;

import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/30 8:11
 */

/*
* 操作Product返回的DTO
* */
public class ProductExecution {
    //操作返回的状态信息
    private int state;
    //操作返回的状态信息描述
    private String stateInfo;
    //操作成功的总数
    private int count;
    //批量操作返回的Product集合
    private List<Product> productList;
    //增删改的操作返回的商品信息
    private Product product;

    /**
     * 默认的空参构造器
     */
    public ProductExecution() {
    }

    /**
     * 批量操作成功时候返回的ProductExecution
     * @param stateEnum
     * @param productList
     */
    public ProductExecution(ProductStateEnum stateEnum, List<Product> productList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.productList = productList;
    }

    /**
     * 批量操作成功时候返回的ProductExecution
     * @param stateEnum
     * @param productList
     */
    public ProductExecution(ProductStateEnum stateEnum, List<Product> productList, int count) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.productList = productList;
        this.count = count;
    }

    /**
     * 单个操作成功时候返回的ProductExecution
     * @param stateEnum
     * @param product
     */
    public ProductExecution(ProductStateEnum stateEnum, Product product) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.product = product;
    }

    /**
     * 操作失败的时候返回的ProductExecution，仅返回状态信息即可。
     * @param stateEnum
     */
    public ProductExecution(ProductStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "ProductExecution{" +
                "state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", count=" + count +
                ", product=" + product +
                '}';
    }
}
