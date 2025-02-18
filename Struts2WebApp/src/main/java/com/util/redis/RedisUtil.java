package com.util.redis;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import java.lang.reflect.Type;
import java.util.Map;

public class RedisUtil {

    private static final Gson gson = new Gson();
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    private RedisUtil() {}

    public static <T> void set(String key, String field, T value) {
        try {
            Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);
            String json = gson.toJson(value);
            jedis.hset(key, field, json);
        } catch (Exception e) {
            System.out.println("Exception while setting data in Redis: " + e.getMessage());
        }
    }

    public static <T> T get(String key, String field, Type type) {
        try {
            Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);
            String json = jedis.hget(key, field);
            if (json != null) {
                return gson.fromJson(json, type);
            }
        } catch (Exception e) {
            System.out.println("Exception while getting data from Redis: " + e.getMessage());
        }
        return null;
    }

    public static <T> Map<String, T> getAll(String key, Type type) {
        try {
            Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);
            Map<String, String> jsonMap = jedis.hgetAll(key);
            if (jsonMap != null && !jsonMap.isEmpty()) {
                return gson.fromJson(gson.toJson(jsonMap), type);
            }
        } catch (Exception e) {
            System.out.println("Exception while getting all data from Redis: " + e.getMessage());
        }
        return null;
    }

    public static void delete(String key) {
        try {
            Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);
            jedis.del(key);
        } catch (Exception e) {
            System.out.println("Exception while deleting data from Redis: " + e.getMessage());
        }
    }

    public static void expire(String key, int seconds) {
        try {
            Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);
            jedis.expire(key, seconds);
        } catch (Exception e) {
            System.out.println("Exception while setting expiration in Redis: " + e.getMessage());
        }
    }

    public static boolean exists(String key) {
        try {
            Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);
            return jedis.exists(key);
        } catch (Exception e) {
            System.out.println("Exception while checking key existence in Redis: " + e.getMessage());
            return false;
        }
    }
}