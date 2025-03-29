package com.gameleaderboard.gameleaderboard.service;

import com.gameleaderboard.gameleaderboard.config.KafkaProperties;
import com.gameleaderboard.gameleaderboard.domain.MatchedUserPoint;
import com.gameleaderboard.gameleaderboard.domain.MatchedUserPointWriter;
import com.gameleaderboard.gameleaderboard.event.MatchedUserPointCreateEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MatchedUserPointService {

    private final MatchedUserPointWriter matchedUserPointWriter;
    private final EventHandler eventHandler;
    private final KafkaProperties properties;

    @Transactional
    public void add(String userId, String matchedId, Long point) {
        var now = Instant.now();
        MatchedUserPoint entity = matchedUserPointWriter.insert(userId, matchedId, point, now);
        var event = new MatchedUserPointCreateEvent(entity.getId(), matchedId, userId, point, now.toEpochMilli(), userId);
        eventHandler.sendEvent(event, properties.topic());
    }

}
