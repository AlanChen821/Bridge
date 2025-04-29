package com.bridge.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class RedisUtils {
//    private static final JedisPool pool = new JedisPool("localhost", 6379);
//    private static final JedisPool pool = new JedisPool("oregon-redis.render.com", 6379, true, "V1kb7SD09hrmlUNExd8kVA80Bwn3WobX");
//    private static final JedisPool pool = new JedisPool("oregon-redis.render.com", 6379, true);
//    private static final JedisPool pool = new JedisPool("oregon-redis.render.com", 6379, "", "V1kb7SD09hrmlUNExd8kVA80Bwn3WobX");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

//    rediss://red-cs14e723esus739ah3ag:V1kb7SD09hrmlUNExd8kVA80Bwn3WobX@oregon-redis.render.com:6379
//    private static final JedisPool pool = new JedisPool(jedisPoolConfig, "oregon-redis.render.com", 6379, Protocol.DEFAULT_TIMEOUT, "V1kb7SD09hrmlUNExd8kVA80Bwn3WobX", true, null, null, null);
    //  for local
//    private static final JedisPool pool = new JedisPool(jedisPoolConfig, "oregon-redis.render.com", 6379, Protocol.DEFAULT_TIMEOUT, "red-cs14e723esus739ah3ag", "V1kb7SD09hrmlUNExd8kVA80Bwn3WobX", true);
    //  on Render
    private static final JedisPool pool = new JedisPool(jedisPoolConfig, "red-cs14e723esus739ah3ag", 6379);

//    public RedisUtils() {
//        log.debug("Construct RedisUtils.");
//    }

    public static Boolean checkKey(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(key);
        }
    }

    public static Boolean checkKeyAndField(String key, Long field) {
        return checkKeyAndField(key, String.valueOf(field));
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

    public static void insertRedis(String key, Long field, Object object) {
        insertRedis(key, String.valueOf(field), object);
    }

    public static void insertRedis(String key, String field, Object object) {
        try (Jedis jedis = pool.getResource()) {
            String jsonVal = JsonUtils.serialize(object);
            jedis.hset(key, field, jsonVal);
        }
    }

    public static String getFromRedis(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
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

    public static <T> T getFromRedis(String key, Long field, Type clazz) {
        return getFromRedis(key, String.valueOf(field), clazz);
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
