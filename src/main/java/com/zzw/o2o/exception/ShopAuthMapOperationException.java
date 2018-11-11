package com.zzw.o2o.exception;

/**
 * author: zzw5005
 * date: 2018/10/26 10:22
 */

/*
* 继承自RuntimeException ，这样在标注了@Transactional事务的方法中，出现了异常，才回回滚数据。
* 默认情况下，如果在事务中抛出了未检查异常（继承自 RuntimeException 的异常）或者Error，则Spring
* 将回滚事务；除此之外，pring不会回滚事务。
* */
public class ShopAuthMapOperationException extends RuntimeException {
    private static final long serialVersionUID = 11231331231241L;

    public ShopAuthMapOperationException(String message){
        super(message);
    }
}
