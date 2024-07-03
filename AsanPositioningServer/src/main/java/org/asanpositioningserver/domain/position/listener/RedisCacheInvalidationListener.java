package org.asanpositioningserver.domain.position.listener;

import org.asanpositioningserver.domain.position.service.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheInvalidationListener implements MessageListener, ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private WatchService watchService;

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        watchService.evictAllCaches();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        redisMessageListenerContainer.addMessageListener(this, new PatternTopic("__keyevent@*__:set"));
        redisMessageListenerContainer.addMessageListener(this, new PatternTopic("__keyevent@*__:del"));
    }
}