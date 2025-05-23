package com.gameleaderboard.gameleaderboard.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "matched_user_point")
@ToString
public class MatchedUserPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String matchedId;
    private Long point;
    private Long pointedAt;
    private Long matchedAt;
    private Long createdAt;

}
