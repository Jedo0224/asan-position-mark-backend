package org.asanpositioningserver.domain.position.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;



@Getter
@Setter
@RedisHash(value = "watchL")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchLive {
    @Id
    private Long id;
    private String watchName;
    @Indexed
    private boolean live;

    public boolean isLive() {
        return "1".equals(this.live);
    }
}
