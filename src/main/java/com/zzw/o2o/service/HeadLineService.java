package com.zzw.o2o.service;

import com.zzw.o2o.entity.HeadLine;

import java.io.IOException;
import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/31 20:13
 */


public interface HeadLineService {
    public static final String  HEADLINEKEY = "arealist";

    /**
     * 根据传入的田间返回指定的头条列表
     * @param headLineCondition
     * @return
     */
    List<HeadLine> queryHeadLineList(HeadLine headLineCondition) throws IOException;
}
