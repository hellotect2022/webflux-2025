package com.mpole.imp.framework.redis;

import com.mpole.imp.framework.utils.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class RedisRepository {

    @Autowired
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    public Mono<Boolean> set(String key, Object value) {
        return reactiveRedisTemplate.opsForValue().set(key, JsonHelper.toJson(value));
    }

    public <T>Mono<T> get(String key, Class<T> clazz) {
        return reactiveRedisTemplate.opsForValue().get(key).map(obj -> JsonHelper.fromJson((String) obj, clazz));
    }

    public Mono<String> get(String key) {
        return get(key, String.class);
    }

    public Mono<Void> hset(String key, String hashkey, Object value) {
        return reactiveRedisTemplate.opsForHash().put(key, hashkey, JsonHelper.toJson(value)).then();
    }

    public <T>Mono<T> hget(String key, String hashkey, Class<T> clazz) {
        return reactiveRedisTemplate.opsForHash().get(key, hashkey).map(obj-> JsonHelper.fromJson((String) obj, clazz));
    }

    public Mono<String> hget(String key, String hashkey) {
        return hget(key, hashkey, String.class);
    }

    public Mono<Void> sadd(String key, Object value) {
        return reactiveRedisTemplate.opsForSet().add(key, JsonHelper.toJson(value)).then();
    }

    public <T> Flux<T> smembers(String key, Class<T> clazz) {
        return reactiveRedisTemplate.opsForSet().members(key).map(member -> JsonHelper.fromJson((String) member, clazz));
    }

    public Flux<String> smembers(String key) {
        return reactiveRedisTemplate.opsForSet().members(key).map(member -> JsonHelper.fromJson((String) member, String.class));
    }
}
