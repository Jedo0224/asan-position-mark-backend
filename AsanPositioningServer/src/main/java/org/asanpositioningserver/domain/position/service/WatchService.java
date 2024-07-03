package org.asanpositioningserver.domain.position.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.asanpositioningserver.domain.position.entity.WatchLive;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class WatchService {
    private final RedisTemplate<String, String> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CacheManager cacheManager;


    public WatchLive getWatch(Long id) throws Exception {
        String key = "watch:" + id;
        Map<Object, Object> rawEntries = redisTemplate.opsForHash().entries(key);
        if (rawEntries.isEmpty()) {
            return null;
        }

        Map<String, Object> entries = new HashMap<>();
        for (Map.Entry<Object, Object> entry : rawEntries.entrySet()) {
            String field = entry.getKey().toString();
            String value = entry.getValue().toString();
            if (field.equals("watchName")) {
                entries.put(field, value);
            } else if (field.equals("live")) {
                entries.put(field, "1".equals(value));
            } else {
                entries.put(field, value);
            }
        }

        String jsonString = objectMapper.writeValueAsString(entries);
        WatchLive watchLive = objectMapper.readValue(jsonString, WatchLive.class);
        return watchLive;
    }

//    @Cacheable(value = "watches")
    public List<WatchLive> getAllWatches() throws Exception {
        List<WatchLive> watches = new ArrayList<>();
        ScanOptions scanOptions = ScanOptions.scanOptions().match("watch:*").build();
        stringRedisTemplate.execute((RedisCallback<Object>) (connection) -> {
            Cursor<byte[]> cursor = connection.scan(scanOptions);
            Pattern pattern = Pattern.compile("watch:(\\d+)");
            while (cursor.hasNext()) {
                String key = new String(cursor.next(), StandardCharsets.UTF_8);
                Matcher matcher = pattern.matcher(key);
                if (matcher.matches()) {
                    Long id = Long.parseLong(matcher.group(1));
                    try {
                        WatchLive watchLive = getWatch(id);
                        if (watchLive != null) {
                            watches.add(watchLive);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        });

        return watches;
    }


    public void evictAllCaches() {
        // This method will clear all caches
    }


    public WatchLive findById(Long id) throws Exception {
        return getWatch(id);
    }

}