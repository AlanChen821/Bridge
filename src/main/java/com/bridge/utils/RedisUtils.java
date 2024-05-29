package com.bridge.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class RedisUtils {
    private static final JedisPool pool = new JedisPool("localhost", 6379);

    private static final ObjectMapper objectMapper = new ObjectMapper();

//    public RedisUtils() {
//        log.debug("Construct RedisUtils.");
//    }

    public static Boolean checkKey(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(key);
        }
    }

    public static Boolean checkKeyAndField(String key, String field) {
        try (Jedis jedis = pool.getResource()) {
            Map<String, String> map = jedis.hgetAll(key);
            if (null != map) {
                return map.containsKey(field);
            }
            return false;
        }
    }

    public static void insertRedis(String key, Object target) {
        try (Jedis jedis = pool.getResource()) {
            Map<String, String> map = new HashMap<>();
            log.info("Target info : {}", target);
            String gameString = objectMapper.writeValueAsString(target);
            map.put("0", gameString);
            jedis.hset(key, map);
        } catch (Exception ex) {
            log.warn("Insert into redis key : {} failed. Message : {}", key, ex.getMessage(), ex);
        }
    }

    public static void insertRedis(String key, String field, Object object) {
        try (Jedis jedis = pool.getResource()) {
            String jsonVal = JsonUtils.serialize(object);
            jedis.hset(key, field, jsonVal);
        }
    }

    public static <T> Map<String, T> getFromRedis(String key, Type clazz) {
        try (Jedis jedis = pool.getResource()) {
            Map<String, String> map = jedis.hgetAll(key);
            Map<String, T> result = new HashMap<>();
            map.forEach((key1, val) -> {
                T target = JsonUtils.deserialize(val, clazz);
                result.put(key1, target);
            });
            return result;
//            return (Map<String, T>) map;
        } catch (Exception ex) {
            log.warn("Get key {} to class {} from Redis failed.", key, clazz);
            return null;
        }
    }

    public static <T> T getFromRedis(String key, String field, Type clazz) {
        try (Jedis jedis = pool.getResource()) {
            String jsonVal = jedis.hget(key, field);
            T object = JsonUtils.deserialize(jsonVal, clazz);
            return object;
        } catch (Exception ex) {
            log.warn("Get key {} & field {} to class {} from Redis failed.", key, field, clazz);
            return null;
        }
    }

}
