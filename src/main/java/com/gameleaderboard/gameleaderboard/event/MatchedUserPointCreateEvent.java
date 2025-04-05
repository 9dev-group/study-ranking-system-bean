package com.gameleaderboard.gameleaderboard.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchedUserPointCreateEvent implements Event {
    private Long domainId;
    private String matchedId;
    private String userId;
    private Long point;
    private Long matchedAt;
    private String eventId;
    private String partitionId;
}
