package com.gameleaderboard.gameleaderboard.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity(name = "daily_user_leaderboard")
@AllArgsConstructor
@NoArgsConstructor
public class DailyUserLeaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant date;
    private String userId;
    private Long point;
    private Long createdAt;
    private Long updatedAt;

}
