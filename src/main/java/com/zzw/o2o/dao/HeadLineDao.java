package com.zzw.o2o.dao;

import com.zzw.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/31 19:55
 */


public interface HeadLineDao {
    /**
     * 根据enable_status查询符合条件的头条信息
     * @param headLineCondition
     * @return
     */
    List<HeadLine> selectHeadLineList(@Param("headLineCondition") HeadLine headLineCondition);

}
