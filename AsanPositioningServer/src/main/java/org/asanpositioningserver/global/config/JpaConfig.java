package org.asanpositioningserver.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "org.asanpositioningserver.domain.*.repository")
@Configuration
public class JpaConfig {
}