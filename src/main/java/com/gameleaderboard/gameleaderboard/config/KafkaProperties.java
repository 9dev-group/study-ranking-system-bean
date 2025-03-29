package com.gameleaderboard.gameleaderboard.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
public record KafkaProperties(
        String host,
        String topic
) {
}
