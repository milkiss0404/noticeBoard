package com.example.noticeboard.common.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> void saveData(String key, T data) {
        try {
            String value = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error("Redis 저장 중 오류 발생", e);
        }
    }

    public <T> Optional<T> getData(String key, Class<T> classType) {
        try {
            String value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                return Optional.empty();
            }
            T data = objectMapper.readValue(value, classType);
            return Optional.of(data);
        } catch (Exception e) {
            log.error("Redis 조회 중 오류 발생", e);
            return Optional.empty();
        }
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
    public String getBlackList(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setBlackList(String key, String value, Long minutes) {
        redisTemplate.opsForValue().set(key, value, minutes, TimeUnit.MINUTES);
    }
}

