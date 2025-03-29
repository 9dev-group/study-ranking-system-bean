package com.gameleaderboard.gameleaderboard.consumer;

import com.gameleaderboard.gameleaderboard.config.KafkaProperties;
import com.gameleaderboard.gameleaderboard.event.MatchedUserPointCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointToLeaderboardConsumer {

    private final KafkaProperties kafkaProperties;

    @KafkaListener(topics = "leaderboard.point", groupId = "point-leaderboard-group", containerFactory = "kafkaMatchedUserPointListenerContainerFactory")
    public void consume(@Payload List<MatchedUserPointCreateEvent> events, @Headers MessageHeaders headers) {
        log.info("[consume] event size: " + events.size());
    }
}
