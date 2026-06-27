package com.Ecommerce.demo.test;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("!production")
public class RedisTestController {

    private final StringRedisTemplate redisTemplate;

    public RedisTestController(
            StringRedisTemplate redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/redis-test")
    public String redisTest() {

        redisTemplate.opsForValue()
                .set("test", "Redis Connected");

        return redisTemplate.opsForValue()
                .get("test");
    }
}
