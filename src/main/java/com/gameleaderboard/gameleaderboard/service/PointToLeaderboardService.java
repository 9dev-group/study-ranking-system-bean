package com.gameleaderboard.gameleaderboard.service;

import com.gameleaderboard.gameleaderboard.domain.DailyUserLeaderboardWriter;
import com.gameleaderboard.gameleaderboard.domain.MatchedUserLeaderboardWriter;
import com.gameleaderboard.gameleaderboard.dto.DailyUserLeaderboardDto;
import com.gameleaderboard.gameleaderboard.dto.MatchedUserLeaderboardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PointToLeaderboardService {

    private final MatchedUserLeaderboardWriter matchedUserLeaderboardWriter;
    private final DailyUserLeaderboardWriter dailyUserLeaderboardWriter;

    @Transactional
    public void updateAllLeaderboard(List<MatchedUserLeaderboardDto> matchedUserLeaderboardDtoList, List<DailyUserLeaderboardDto> dailyUserLeaderboardDtoList) {
        matchedUserLeaderboardWriter.upsertAll(matchedUserLeaderboardDtoList);
        dailyUserLeaderboardWriter.upsertAll(dailyUserLeaderboardDtoList);
    }

}
