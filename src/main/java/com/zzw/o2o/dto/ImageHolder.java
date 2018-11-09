package com.zzw.o2o.dto;

import java.io.InputStream;

/**
 * author: zzw5005
 * date: 2018/10/30 8:34
 */

/*
* 我们将 InputStream prodImgIns和 String prodImgName 封装到一个类中，取名为ImageHolder ,
* 提供构造函数用于初始化以及setter/getter方法。
* */
public class ImageHolder {
    private String fileName;
    private InputStream ins;

    public ImageHolder(InputStream ins, String fileName){
        this.ins = ins;
        this.fileName = fileName;
    }

    public InputStream getIns() {
        return ins;
    }

    public void setIns(InputStream ins) {
        this.ins = ins;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "ImageHolder{" +
                "ins=" + ins +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
