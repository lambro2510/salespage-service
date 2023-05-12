package com.salespage.salespageservice.domains.utils;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class RemoteCacheManager {


  public static final int HOUR = 3600; //1h

  public static final int DAY = 3600 * 24; //1h
  private static final int TOKEN_EXPIRE = 604800;


  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  public void set(String key, String value, int expireTime) {
    redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
  }

  public void set(String key, Object value, int expireTime) {
    redisTemplate.opsForValue().set(key, JsonParser.toJson(value), expireTime, TimeUnit.SECONDS);
  }

  public void set(String key, String value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public void setList(String key, Object o, Integer expireTime) {
    if (expireTime != null) {
      redisTemplate.opsForValue().set(key, JsonParser.toJson(o), expireTime);
    } else {
      redisTemplate.opsForValue().set(key, JsonParser.toJson(o));
    }
  }

  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public <T> T get(String key, Class<T> tClass) throws Exception {
    String value = redisTemplate.opsForValue().get(key);
    return JsonParser.entity(value, tClass);
  }

  public <T> ArrayList<T> getList(String key, Class<T> tClass) {
    try {

      String value = redisTemplate.opsForValue().get(key);
      return JsonParser.arrayList(value, tClass);

    } catch (Exception e) {

      return null;
    }
  }

  public Boolean exists(String key) {
    return redisTemplate.hasKey(key);
  }

  public void del(String key) {
    redisTemplate.delete(key);
  }


  public void saveConfig(String key, String value) {
    String configKey = CacheKey.getConfigKey(key);
    set(configKey, value, Integer.MAX_VALUE);
  }

  public void deleteConfig(String key) {
    String configKey = CacheKey.getConfigKey(key);
    del(configKey);
  }


  public void deleteTokenValue(String token) {
    redisTemplate.delete(CacheKey.genSessionKey(token));
  }
}
