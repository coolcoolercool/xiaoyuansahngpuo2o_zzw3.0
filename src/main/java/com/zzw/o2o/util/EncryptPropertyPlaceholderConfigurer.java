package com.zzw.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * author: zzw5005
 * date: 2018/11/6 9:47
 */

/*
*
* */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    //需要加密的字段数组,遇到这两个字段就进行解密
    private String[] encryptPropNames = {"jdbc.username","jdbc.password"};

    /**
     * 对关键的属性进行转换
     * @param propertyName
     * @param propertyValue
     * @return
     */
    protected String convertProperty(String propertyName, String propertyValue){
        if(isEncryptProp(propertyName)){
            //对已经加密的字段进行解密工作
            String decryptValue = DESUtils.getDecryptString(propertyValue);
            return decryptValue;
        }else{
            return propertyValue;
        }
    }

    /**
     * 该属性是否已经加密
     * @param propertyName
     * @return
     */
    private boolean isEncryptProp(String propertyName){
        //若等于需要加密的field,则进行加密
        for(String encryptPropertyName : encryptPropNames){
            if(encryptPropertyName.equals(propertyName)){
                return true;
            }
        }
        return false;
    }
}
