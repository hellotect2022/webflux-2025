package com.mpole.imp.framework.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class RedisConfig {
    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(Environment env) {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        String redisHost = env.getProperty("spring.redis.host", "localhost");
        int port = env.getProperty("spring.redis.port", Integer.class, 6379);
        String password = env.getProperty("spring.redis.password", "");

        redisConfig.setHostName(redisHost);
        redisConfig.setPort(port);
        redisConfig.setPassword(password);

        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(@Qualifier("reactiveRedisConnectionFactory") ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext<String, Object> serializationContext =
                RedisSerializationContext.<String, Object>newSerializationContext(new StringRedisSerializer())
                        .hashKey(new StringRedisSerializer())
                        .hashValue(new StringRedisSerializer())
                        .key(new StringRedisSerializer())
                        .build();

        return new ReactiveRedisTemplate<>(factory, serializationContext);
    }
}
