package com.zzw.o2o.exception;

/**
 * author: zzw5005
 * date: 2018/10/30 8:24
 */

/*
* 继承自RuntimeException ，这样在标注了@Transactional事务的方法中，出现了异常，才会回滚数据。
* 默认情况下，如果在事务中抛出了未检查异常（继承自RuntimeException 的异常或者Error，则Spring
* 将回滚事务；除此之外，Spring 不会回滚事务。
* */
public class ProductOperationException extends RuntimeException {
    private static final long serialVersionUID =  -6981952073033881834L;

    public ProductOperationException(String message){
        super(message);
    }
}
