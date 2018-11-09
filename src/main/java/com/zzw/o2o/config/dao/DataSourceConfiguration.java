package com.zzw.o2o.config.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zzw.o2o.util.DESUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;

/**
 * author: zzw5005
 * date: 2018/11/8 17:31
 */


/**
 * 配置dataSource到IoC容器中
 */
@Configuration //表明这个配置文件会写入SpringIoC容器中
//配置mybatis mapper的扫描路径
@MapperScan("com.zzw.o2o.dao")
public class DataSourceConfiguration {

    @Value("${jdbc.driver}") //从属性文件中读取变量
    private String jdbcDriver;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;

    /**
     * 生成与Spring-dao.xml对应的bean dataSource
     * @return
     */
    @Bean(name="dataSource") //表明这个类会生成名为dataSource的bean
    public ComboPooledDataSource createDataSource() {
        //生成dataSource实例
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        //跟配置文件一样设置以下信息
        //驱动
        try {
            dataSource.setDriverClass(jdbcDriver);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        //数据库连接URL
        dataSource.setJdbcUrl(jdbcUrl);
        //设置用户名,注意这里对属性文件中的用户名进行了解密,并不是直接取的暗文
        dataSource.setUser(DESUtils.getDecryptString(jdbcUsername));
        //设置用户密码
        dataSource.setPassword(DESUtils.getDecryptString(jdbcPassword));

        //设置c3p0连接池的私有属性
        //连接池最大线程数
        dataSource.setMaxPoolSize(30);
        //连接池最小线程数
        dataSource.setMinPoolSize(10);
        //关闭连接后不自动commit
        dataSource.setAutoCommitOnClose(false);
        //连接超时时间
        dataSource.setCheckoutTimeout(10000);
        //连接失败重试次数
        dataSource.setAcquireRetryAttempts(2);

        return dataSource;
    }
}
