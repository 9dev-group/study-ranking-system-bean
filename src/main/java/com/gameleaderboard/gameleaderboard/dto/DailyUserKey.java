package com.gameleaderboard.gameleaderboard.dto;

import java.time.Instant;

public record DailyUserKey(
        Instant date,
        String userId
) {
}
