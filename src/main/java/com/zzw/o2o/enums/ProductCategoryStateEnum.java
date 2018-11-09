package com.zzw.o2o.enums;

/**
 * author: zzw5005
 * date: 2018/10/29 10:08
 */

/*
* 将对ProductCategoryState的状态信息封装到ProductCategoryStateEnum中
* */
public enum ProductCategoryStateEnum {
    SUCCESS(1,"操作成功"),
    INNER_ERROR(-1001, "操作失败"),
    NULL_SHOP(-1002, "Shop信息为空"),
    EMPTY_LIST(-1003,"请输入商品目录信息");

    private int state;
    private String stateInfo;

    ProductCategoryStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 通过state获取productCategoryStateEnum,从而可以调用ProductCategoryStateEnum
     * #getStateInfo()获取stateInfo
     * @param index
     * @return
     */
    public static ProductCategoryStateEnum stateOf(int index){
        for(ProductCategoryStateEnum productCategoryStateEnum : values()){
            if(productCategoryStateEnum.getState() == index){
                return productCategoryStateEnum;
            }
        }
        return null;
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
}
