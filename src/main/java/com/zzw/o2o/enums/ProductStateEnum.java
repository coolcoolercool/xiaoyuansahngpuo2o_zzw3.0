package com.zzw.o2o.enums;

/**
 * author: zzw5005
 * date: 2018/10/30 8:19
 */

/*
* 商品的枚举类
* */
public enum ProductStateEnum {
    SUCCESS(1,"操作成功"),
    INNER_ERROR(-1001,"操作失败"),
    NULL_PARAMETER(-1002,"缺少参数");

    private int state;
    private String stateInfo;

    /**
     * 私有构造器，禁止外部初始化改变定义的常量
     * @param state
     * @param stateInfo
     */
    private ProductStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 定义换成public 暴露给外部，通过state获取ShopStateEnum
     * values()获取全部的enum常量
     * @param state
     * @return
     */
    public static ProductStateEnum stateOf(int state){
        for(ProductStateEnum stateEnum : values()){
            if(stateEnum.getState() == state){
                return stateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
