package com.gameleaderboard.gameleaderboard.consumer;

import com.gameleaderboard.gameleaderboard.dto.DailyUserKey;
import com.gameleaderboard.gameleaderboard.dto.DailyUserLeaderboardDto;
import com.gameleaderboard.gameleaderboard.dto.MatchedUserKey;
import com.gameleaderboard.gameleaderboard.dto.MatchedUserLeaderboardDto;
import com.gameleaderboard.gameleaderboard.event.MatchedUserPointCreateEvent;
import com.gameleaderboard.gameleaderboard.service.PointToLeaderboardService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointToLeaderboardConsumer {

    private final PointToLeaderboardService pointToLeaderboardService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final String MATCHED_LEADERBOARD_KEY = "matchedLeaderboard:matchedId:";
    private final String DAILY_LEADERBOARD_KEY = "dailyLeaderboard:date:";

    @KafkaListener(topics = "leaderboard.point",
            groupId = "point-leaderboard-group",
            containerFactory = "kafkaMatchedUserPointListenerContainerFactory")
    public void consume(
            @Payload List<MatchedUserPointCreateEvent> events,
            Acknowledgment acknowledgment
    ) {
        List<MatchedUserPointCreateEvent> filteredEvents = Lists.newArrayList();
        try {
            filteredEvents = events.stream()
                    .filter(event -> redisTemplate.opsForValue().setIfAbsent(event.getEventId(), event.getEventId(), Duration.ofHours(1)))
                    .toList();

            // matched user leaderboard 집계
            List<MatchedUserLeaderboardDto> matchedUserLeaderboardDtoList = filteredEvents
                    .stream()
                    .collect(Collectors.groupingBy(
                            p -> new MatchedUserKey(p.getMatchedId(), p.getUserId()),
                            Collectors.summingLong(MatchedUserPointCreateEvent::getPoint)
                    )).entrySet()
                    .stream()
                    .map(entry -> new MatchedUserLeaderboardDto(entry.getKey(), entry.getValue()))
                    .toList();

            // daily user leaderboard 집계
            List<DailyUserLeaderboardDto> dailyUserLeaderboardDtoList = filteredEvents
                    .stream()
                    .collect(Collectors.groupingBy(
                            p -> new DailyUserKey(Instant.ofEpochMilli(p.getMatchedAt()).truncatedTo(DAYS), p.getUserId()),
                            Collectors.summingLong(MatchedUserPointCreateEvent::getPoint)
                    )).entrySet()
                    .stream()
                    .map(entry -> new DailyUserLeaderboardDto(entry.getKey(), entry.getValue()))
                    .toList();

            pointToLeaderboardService.updateAllLeaderboard(
                    matchedUserLeaderboardDtoList,
                    dailyUserLeaderboardDtoList
            );

            matchedUserLeaderboardDtoList.forEach(dto -> {
                redisTemplate.opsForZSet()
                        .incrementScore(MATCHED_LEADERBOARD_KEY + dto.key().matchedId(), dto.key().userId(), dto.point());
            });
            dailyUserLeaderboardDtoList.forEach(dto -> {
                redisTemplate.opsForZSet()
                        .incrementScore(DAILY_LEADERBOARD_KEY + dto.key().date().toString(), dto.key().userId(), dto.point());
            });

            log.info("[consume] event size: " + events.size());
            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.error("[consume] consume failed", ex);
            filteredEvents.forEach(event -> redisTemplate.delete(event.getEventId()));
            log.info("[consume] delete failed event keys: " + filteredEvents);
            throw ex;
        }
    }
}
