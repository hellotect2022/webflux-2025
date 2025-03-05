package com.mpole.imp.framework.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class RedisSubscriber {
    @Autowired
    private ReactiveRedisMessageListenerContainer reactiveContainer;

    public Flux<String> subscribeToChannel(String channel) {
        return reactiveContainer.receive(ChannelTopic.of(channel)).map(ReactiveSubscription.Message::getMessage);
    }
}
