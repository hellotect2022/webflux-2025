package com.mpole.imp.framework.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RedisPublisher { // redis Publisher class
    @Autowired
    private ReactiveRedisTemplate reactiveRedisTemplate;

    public Mono<Void> publishMessage(String channel, String message) {
        return reactiveRedisTemplate.convertAndSend(channel, message).then();
    }
}
