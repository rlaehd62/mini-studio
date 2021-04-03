package com.github.rlaehd62.service;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;


@Service
public class RedisService
{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String getData(String key)
    {
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setData(String key, String value)
    {
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key,value);
    }
    
    public List<String> getList(String key)
    {
    	ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
    	return listOperations.range(key, 0, listOperations.size(key));
    }
    
    public void addToList(String key, String value)
    {
    	ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
    	listOperations.leftPush(key, value);
    }

    public void setDataExpire(String key,String value,long duration)
    {
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key,value,expireDuration);
    }

    public void deleteData(String key)
    {
        stringRedisTemplate.delete(key);
    }
}
