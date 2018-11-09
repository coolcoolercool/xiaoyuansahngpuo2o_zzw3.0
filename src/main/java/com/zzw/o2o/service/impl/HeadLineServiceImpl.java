package com.zzw.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzw.o2o.cache.JedisUtil;
import com.zzw.o2o.dao.HeadLineDao;
import com.zzw.o2o.entity.HeadLine;
import com.zzw.o2o.service.HeadLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/31 20:16
 */

@Service
public class HeadLineServiceImpl implements HeadLineService {

    private static final Logger logger = LoggerFactory.getLogger(HeadLineServiceImpl.class);

    @Autowired
    private HeadLineDao headLineDao;

    @Autowired
    private JedisUtil.Strings jedisStrings;
    @Autowired
    private JedisUtil.Keys jedisKeys;


    @Override
    public List<HeadLine> queryHeadLineList(HeadLine headLineCondition) throws IOException {
        List<HeadLine> headLineList = new ArrayList<>();

        //定义key
        String key = "headline";
        //定义jackson数据转换操作类
        ObjectMapper mapper = new ObjectMapper();

        //根据mapper中查询田间 拼接key
        //根据不同的条件的key值,这里有两种缓存 headline_0 headline_1  方便管理员权限操作
        if(headLineCondition != null && headLineCondition.getEnableStatus() != null){
            key = key + "_" + headLineCondition.getEnableStatus();
        }
        //如果不存在,从数据库中获取数据,然后写入redis
        if(!jedisKeys.exists(key)){
            try {
                //从DB中获取数据
                headLineList = headLineDao.selectHeadLineList(headLineCondition);
                //将相关的实体类集合转换成string,存入redis里面对应的key中
                String jsonString = mapper.writeValueAsString(headLineList);
                jedisStrings.set(key, jsonString);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error("实体类集合转换string存入redis异常",e.getMessage());
            }
        }else{
            //否则从直接从redis中获取
            try {
                //若存在,则直接从redis中取出相应的数据
                String jsonString = jedisStrings.get(key);
                //指定要将string转换成的集合类型
                JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
                //将相关key对应的value里的string转换成jva对象的实体类集合
                headLineList = mapper.readValue(jsonString, javaType);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("异常{}", e.getMessage());
            }
        }
        return headLineList;
    }
}
