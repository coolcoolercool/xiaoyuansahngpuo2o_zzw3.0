package com.zzw.o2o.util;

import javax.servlet.http.HttpServletRequest;

/**
 * author: zzw5005
 * date: 2018/10/27 8:57
 */


public class HttPServletRequestUtil {
    public static int getInt(HttpServletRequest request, String key){
        try {
            return Integer.decode(request.getParameter(key));
        } catch (Exception e) {
            return -1; //表示失败，出现异常
        }
    }

    public static long getLong(HttpServletRequest request, String key){
        try {
            return Long.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1L; //表示失败，出现异常
        }
    }

    public static Double getDouble(HttpServletRequest request, String key){
        try {
            return Double.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1d; //表示失败，出现异常
        }
    }

    public static boolean getBoolean(HttpServletRequest request, String key){
        try {
            return Boolean.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return false; //表示失败，出现异常
        }
    }

    public static String getString(HttpServletRequest request, String key){

        try {
            String result = request.getParameter(key);
            if(result != null){
                result = result.trim(); //去除result两边的空格
            }
            if(result.equals("")){
                result = null;
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
