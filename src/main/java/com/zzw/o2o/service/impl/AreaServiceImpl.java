package com.zzw.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzw.o2o.cache.JedisUtil;
import com.zzw.o2o.dao.AreaDao;
import com.zzw.o2o.entity.Area;
import com.zzw.o2o.exception.AreaOperationException;
import com.zzw.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * author: zzw5005
 * date: 2018/10/25 9:58
 */

@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaDao areaDao;

    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Autowired
    private JedisUtil.Strings jedisStrings;

    // 区域信息的key,保存所有区域信息
    private static String AREALISTKEY = "arealist";

    private static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);

    @Override
    @Transactional
    public List<Area> getAreaList()  {
        //定义redis中的key
        String key = AREALISTKEY;
        //定义接收对象
        List<Area> areaList = null;
        //定义jackson数据转换操作类
        ObjectMapper mapper = new ObjectMapper();
            //如果reids中不存在key的信息,就从数据库中查询,如果redis中存在,则从redis中取出key的信息
            if (!jedisKeys.exists(key)) {
                try {
                    areaList = areaDao.queryArea();
                    String jsonString = mapper.writeValueAsString(areaList);
                    jedisStrings.set(key, jsonString);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    throw new AreaOperationException(e.getMessage());
                }
            } else {
                try {
                    String jsonString = jedisStrings.get(key);
                    JavaType javaType = mapper.getTypeFactory()
                            .constructParametricType(ArrayList.class, Area.class);
                    areaList = mapper.readValue(jsonString, javaType);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new AreaOperationException(e.getMessage());
                }
            }
        return areaList;
    }



}
