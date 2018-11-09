package com.zzw.o2o.service.impl;

import com.zzw.o2o.cache.JedisUtil;
import com.zzw.o2o.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * author: zzw5005
 * date: 2018/11/7 10:01
 */

@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Override
    public void removeFromCache(String keyPrefix) {
        Set<String> keySet = jedisKeys.keys(keyPrefix + "*");
        for(String key : keySet){
            jedisKeys.del(key);
        }
    }
}
