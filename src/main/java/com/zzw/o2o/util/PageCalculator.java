package com.zzw.o2o.util;

/**
 * author: zzw5005
 * date: 2018/10/28 21:06
 */


public class PageCalculator {
    /**
     * 通过pageIndex和pageSize计算RowIndex
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static int calculateRowIndex(int pageIndex, int pageSize){
        return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
    }
}
