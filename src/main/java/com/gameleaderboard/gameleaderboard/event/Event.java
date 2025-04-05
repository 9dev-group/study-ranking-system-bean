package com.gameleaderboard.gameleaderboard.event;

public interface Event {
    Long getDomainId();
    String getPartitionId();
    String getEventId();
}
