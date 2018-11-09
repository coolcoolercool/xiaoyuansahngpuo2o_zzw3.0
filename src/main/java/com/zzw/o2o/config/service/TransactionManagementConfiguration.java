package com.zzw.o2o.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * author: zzw5005
 * date: 2018/11/9 9:48
 */

/**
 * 对表spring-service里面的transactionManager
 * 继承TransactionManagementConfigurer是因为开启annotation-driver
 */

@Configuration
//首先使用注解 @EnableTransactionManagement开启事务支持后
//在service方法上添加注解 @Transaction即可
public class TransactionManagementConfiguration implements TransactionManagementConfigurer {
    @Autowired
    //注入DataSourceConfiguration里面的dataSource,通过createDataSource()获取数据连接信息
    private DataSource dataSource;

    /**
     * 关于事务管理,需要返回PlatformTransactionManager的实现
     * @return PlatformTransactionManager
     */
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
