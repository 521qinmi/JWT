package com.gse.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class OttService {

    private final StringRedisTemplate redis;

    public OttService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public String generateOtt(String payload) {
        String ott = UUID.randomUUID().toString();
        redis.opsForValue().set("OTT:" + ott, payload, Duration.ofSeconds(30));
        return ott;
    }

    public String consume(String ott) {
        String key = "OTT:" + ott;
        String val = redis.opsForValue().get(key);
        if (val == null) return null;
        redis.delete(key);
        return val;
    }
}