package com.gameleaderboard.gameleaderboard.consumer;

import com.gameleaderboard.gameleaderboard.dto.DailyUserKey;
import com.gameleaderboard.gameleaderboard.dto.MatchedUserKey;
import com.gameleaderboard.gameleaderboard.event.MatchedUserPointCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointToLeaderboardConsumer {

    @KafkaListener(topics = "leaderboard.point", groupId = "point-leaderboard-group", containerFactory = "kafkaMatchedUserPointListenerContainerFactory")
    public void consume(
            @Payload List<MatchedUserPointCreateEvent> events,
            Acknowledgment acknowledgment) {
        // todo redis에 key를 적제해서 consumer에서 멱등성을 유지한다.
        // todo fail 나면 event id를 다시 제거한다.
        try {
            Map<MatchedUserKey, Long> matchedLeaderboardMap = events
                    .stream()
                    .collect(Collectors.groupingBy(
                            p -> new MatchedUserKey(p.getMatchedId(), p.getUserId()),
                            Collectors.summingLong(MatchedUserPointCreateEvent::getPoint)
                    ));

            Map<DailyUserKey, Long> dailyUserKeyLongMap = events
                    .stream()
                    .collect(Collectors.groupingBy(
                            p -> new DailyUserKey(Instant.ofEpochMilli(p.getPointedAt()).truncatedTo(DAYS), p.getUserId()),
                            Collectors.summingLong(MatchedUserPointCreateEvent::getPoint)
                    ));
            log.info("[consume] event size: " + events.size());
            acknowledgment.acknowledge();
        } catch (Exception ex) {

        }
    }
}
