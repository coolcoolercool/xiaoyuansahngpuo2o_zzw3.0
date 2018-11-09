package com.zzw.o2o.config.redis;

import com.zzw.o2o.cache.JedisPoolWriper;
import com.zzw.o2o.cache.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

/**
 * author: zzw5005
 * date: 2018/11/9 10:01
 */

/**
 * spring-redis.xml的配置
 */
@Configuration
public class RedisConfiguration {
    @Value("${redis.hostname}")
    private String hostname;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.pool.maxActive}")
    private int maxTotal;

    @Value("${redis.pool.maxIdle}")
    private int maxIdle;

    @Value("${redis.pool.maxWait}")
    private long maxWaitMills;

    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;
    @Autowired
    private JedisPoolWriper jedisWriperPool;
    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 创建redis连接池的设置
     * @return
     */
    @Bean(name="jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //控制一个pool可分配多少个jedis实例
        jedisPoolConfig.setMaxTotal(maxTotal);
        //连接池中最多可空闲maxIdle个连接,这里取值为20
        //表示即使没有数据库连接时依然可以保持20个空闲的连接
        //而不清除,随时处于待命状态
        jedisPoolConfig.setMaxIdle(maxIdle);
        //最大等待时间,当没有可用连接时候,连接池等待连接被归还的最大时间(单位毫秒)超过时间则抛出异常
        jedisPoolConfig.setMaxWaitMillis(maxWaitMills);
        //在获取连接的到时候检查有效性
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        return jedisPoolConfig;
    }

    /**
     * 创建Redis连接池,并做相关配置
     * @return
     */
    @Bean(name="jedisWriperPool")
    public JedisPoolWriper createJedisPoolWriper(){
        JedisPoolWriper jedisPoolWriper = new JedisPoolWriper(jedisPoolConfig, hostname, port);
        return jedisPoolWriper;
    }

    /**
     * 创建Redis工具类,封装好Redis的连接以进行相关的操作
     * @return
     */
    @Bean(name="jedisUtil")
    public JedisUtil createJedisUtil(){
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(jedisWriperPool);
        return jedisUtil;
    }

    /**
     * Redis的key操作
     * @return
     */
    @Bean(name="jedisKeys")
    public JedisUtil.Keys createJedisKeys(){
        //内部类的new 的方式
        JedisUtil.Keys jedisKeys = jedisUtil.new Keys();
        return jedisKeys;
    }


    /**
     * Redis的String操作
     * todo 这里只是做了Strings类型的操作,其他的比如List,Sets没有做,如果除了错误,
     * 记得要将RedisUtil的其他类型删除
     * @return
     */
    @Bean(name="jedisStrings")
    public JedisUtil.Strings createJedisStrings(){
        JedisUtil.Strings jedisStrings = jedisUtil.new Strings();
        return jedisStrings;
    }

}






























