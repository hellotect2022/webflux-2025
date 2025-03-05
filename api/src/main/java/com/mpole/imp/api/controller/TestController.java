package com.mpole.imp.api.controller;

import com.mpole.imp.api.dto.Response;
import com.mpole.imp.api.dto.type.ErrorCode;
import com.mpole.imp.api.exception.CustomException;
import com.mpole.imp.api.exception.StatusOkException;
import com.mpole.imp.framework.redis.RedisPublisher;
import com.mpole.imp.framework.redis.RedisRepository;
import com.mpole.imp.framework.redis.RedisSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private RedisPublisher redisPublisher;

    @Autowired
    private RedisSubscriber redisSubscriber;


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

    @GetMapping("/redis/publish"  )
    public Mono<Void> redisPublish() {
        return redisPublisher.publishMessage("test", "Hello, Redis!").then();
    }

    @GetMapping("/redis/subscribe"  )
    public Flux<String> redisSubscribe() {
        return redisSubscriber.subscribeToChannel("test");
    }

    @GetMapping("/memory")
    public Mono<Response<Map<String, Long>>> getMemoryInfo() {
        return Mono.defer(()->{
            Runtime runtime = Runtime.getRuntime();

            long maxMemory = runtime.maxMemory(); // JVM이 사용할 수 있는 최대 Heap 크기
            long totalMemory = runtime.totalMemory(); // 현재 JVM이 확보한 Heap 크기
            long freeMemory = runtime.freeMemory(); // 사용 가능한 Heap 크기
            long usedMemory = totalMemory - freeMemory; // 사용 중인 Heap 크기

            Map<String, Long> memoryInfo = new HashMap<>();
            memoryInfo.put("maxMemory", maxMemory / 1024 / 1024);
            memoryInfo.put("totalMemory", totalMemory / 1024 / 1024);
            memoryInfo.put("freeMemory", freeMemory / 1024 / 1024);
            memoryInfo.put("usedMemory", usedMemory / 1024 / 1024);

            return Mono.just(Response.success(memoryInfo));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/error")
    public Mono<Response> getError() {

        return Mono.error(new RuntimeException("An error occurred."));
    }
    @GetMapping("/error1")
    public Mono<Response> getError1() {

        return Mono.error(new RuntimeException("An error occurred."));
    }

    @GetMapping("/error2")
    public Mono<Response> getError2() {
        return Mono.error(new StatusOkException(ErrorCode.SOME_ERROR));
    }
}
