package com.bridge.demo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class RedisUtils {
    private static final JedisPool pool = new JedisPool("localhost", 6379);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public RedisUtils() {
        log.debug("Construct RedisUtils.,");
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
}
