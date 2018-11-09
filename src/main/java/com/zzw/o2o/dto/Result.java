package com.zzw.o2o.dto;

/**
 * author: zzw5005
 * date: 2018/10/29 10:04
 */

/*
* 封装json对象，所有返回结构都用它
* 前面的代码中，我们都是使用的Map<String, Object>作为返回值，然后通过@ResponseBody转换为JSON对象
* （也可以继续使用Map<String, Object>，一样的效果）。
* 这里我们换另外一种方式，将操作结果以及数据存到到List中，然后使用Result泛型类，返回Result给前台。
---------------------

* */
public class Result<T> {
    //是否成功的标识
    private boolean success;
    //成功时，返回的数据
    private T data;
    //错误码
    private int errorCode;
    //错误信息
    private String errMsg;

    /**
     * 空参构造器
     */
    public Result() {
    }

    /**
     * 数据获取成功的时候使用的构造器
     * @param success
     * @param data
     */
    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    /**
     * 数据获取失败时候使用的构造器
     * @param success
     * @param errorCode
     * @param errMsg
     */
    public Result(boolean success, int errorCode, String errMsg) {
        this.success = success;
        this.errorCode = errorCode;
        this.errMsg = errMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
