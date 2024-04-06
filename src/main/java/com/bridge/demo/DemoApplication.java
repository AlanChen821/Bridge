package com.bridge.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Slf4j
public class DemoApplication {

	private static final JedisPool pool = new JedisPool("localhost", 6379);

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

//		configureRedis();
	}

	private static void configureRedis() {
//		JedisPool pool = new JedisPool("localhost", 6379);

		try (Jedis jedis = pool.getResource()) {
//			jedis.set("foo", "bar");
			System.out.println(jedis.get("foo"));
			log.info("Configure Redis successfully.");

//			Map<String, String> hash = new HashMap<>();
//			hash.put("name", "John");
//			hash.put("surname", "Smith");
//			hash.put("age", "29");
//			jedis.hset("key", hash);
//			System.out.println(jedis.hgetAll("key"));
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
			log.warn("Insert into redis key : {} failed. Message : {}", key, ex.getMessage());
		}
	}
}
