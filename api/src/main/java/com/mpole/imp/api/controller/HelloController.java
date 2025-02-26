package com.mpole.imp.api.controller;


import com.mpole.imp.framework.redis.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
public class HelloController {

    @Autowired
    RedisRepository redisRepository;

    @GetMapping("/hello"  )
    public Mono<String> hello() {
        return Mono.just("Hello, MPole!");
    }

    @GetMapping("/redis/set"  )
    public Mono<String> setRedisValue() {
        return redisRepository.set("key", "value")
                .filter(success -> success)
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to set value in Redis.")))
                .then(Mono.just("Value set successfully in Redis."));
    }

    @GetMapping("/redis/get"  )
    public Mono<String> getRedisValue() {
        return redisRepository.get("key");
    }
}
