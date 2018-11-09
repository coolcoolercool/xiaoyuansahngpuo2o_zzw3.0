package com.zzw.o2o.enums;

/**
 * author: zzw5005
 * date: 2018/10/26 8:56
 */

/*
* 使用枚举表述常量数据字典
* */
public enum ShopStateEnum {
    CHECK(0,"审核中"),
    OFFLINE(-1,"非法店铺"),
    SUCCESS(1,"操作成功"),
    PASS(2,"审核通过"),
    INNER_ERROR(-1001,"系统内部错误"),
    NULL_SHOPID(-1002,"ShopId为空"),
    NULL_SHOP(-1003,"Shop信息为空");

    private int state;
    private String stateInfo;

    private ShopStateEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /*
    * 这里仅仅设置了get方法，禁用set方法
    * */
    public int getState(){
        return state;
    }

    public String getStateInfo(){
        return stateInfo;
    }

    /**
     * 定义换成public 暴露给外部，通过state获取shopStateEnum
     * values()获取全部的enum常量
     * @param index
     * @return
     */
    public static ShopStateEnum stateOf(int index){
        for(ShopStateEnum state : values()){
            if(state.getState() == index){
                return state;
            }
        }
        return null;
    }
}
