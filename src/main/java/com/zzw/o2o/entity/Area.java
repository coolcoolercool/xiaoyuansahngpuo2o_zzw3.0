package com.zzw.o2o.entity;

import java.util.Date;

/**
 * author: zzw5005
 * date: 2018/10/24 10:39
 */

/*
* 区域信息
* */
public class Area {
    //为什么这里的成员变量的类型都是引用类型而不是基础类型，因为当成员变量是空值的时候，
    //系统会默认赋值，基础类型的默认值是0，会干扰判断，不知道是系统的默认值，还是本来就是0
    //引用类型的默认值是null，就不会有这样的问题。
    //id
    private Long areaId;
    //名字
    private String areaName;
    //地区描述
    private String areaDesc;
    //权重
    private Integer priority;
    //创建时间
    private Date createTime;
    //更新时间
    private Date lastEditTime;

    public String getAreaDesc() {
        return areaDesc;
    }

    public void setAreaDesc(String areaDesc) {
        this.areaDesc = areaDesc;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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

    public void setLastEditTime(Date lastEndTime) {
        this.lastEditTime = lastEndTime;
    }


    @Override
    public String toString() {
        return "Area{" +
                "areaId=" + areaId +
                ", areaName='" + areaName + '\'' +
                ", areaDesc='" + areaDesc + '\'' +
                ", priority=" + priority +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                '}';
    }
}
