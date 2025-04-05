package com.gameleaderboard.gameleaderboard.domain;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@Transactional
@RequiredArgsConstructor
public class MatchedUserPointWriter {

    private final MatchedUserPointRepository matchedUserPointRepository;

    public MatchedUserPoint insert(String userId, String matchedId, Long point, Instant pointedAt, Long matchedAt) {
        return matchedUserPointRepository.save(
                MatchedUserPoint
                        .builder()
                        .userId(userId)
                        .matchedId(matchedId)
                        .point(point)
                        .pointedAt(pointedAt.toEpochMilli())
                        .matchedAt(matchedAt)
                        .createdAt(Instant.now().toEpochMilli())
                        .build()
        );
    }

}
