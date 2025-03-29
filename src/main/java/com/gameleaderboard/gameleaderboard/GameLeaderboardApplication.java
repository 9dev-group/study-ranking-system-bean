package com.gameleaderboard.gameleaderboard;

import com.gameleaderboard.gameleaderboard.config.KafkaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaRepositories
@EnableConfigurationProperties(KafkaProperties.class)
@SpringBootApplication
public class GameLeaderboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameLeaderboardApplication.class, args);
    }

}
