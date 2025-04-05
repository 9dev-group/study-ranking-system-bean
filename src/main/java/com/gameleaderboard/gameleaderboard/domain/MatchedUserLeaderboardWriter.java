package com.gameleaderboard.gameleaderboard.domain;

import com.gameleaderboard.gameleaderboard.dto.MatchedUserLeaderboardDto;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
@Transactional
@RequiredArgsConstructor
public class MatchedUserLeaderboardWriter {

    private final NamedParameterJdbcTemplate template;
    private final String UPSERT_QUERY = "INSERT INTO matched_user_leaderboard " +
            "(matched_id, user_id, point, created_at, updated_at) " +
            "VALUES (:matchedId, :userId, :point, :createdAt, :updatedAt) " +
            "ON DUPLICATE KEY UPDATE " +
            "point = point + VALUES(point), updated_at = VALUES(updated_at)";

    public void upsertAll(List<MatchedUserLeaderboardDto> matchedUserLeaderboardDtoList) {
        List<List<MatchedUserLeaderboardDto>> partitions = Lists.partition(matchedUserLeaderboardDtoList, 500);

        partitions.forEach(partition -> {
            List<Map<String, Object>> params = partition.stream().map(dto -> {
                var now = Instant.now().toEpochMilli();
                Map<String, Object> paramMap = Maps.newHashMap();
                paramMap.put("matchedId", dto.key().matchedId());
                paramMap.put("userId", dto.key().userId());
                paramMap.put("point", dto.point());
                paramMap.put("createdAt", now);
                paramMap.put("updatedAt", now);
                return paramMap;
            }).toList();
            template.batchUpdate(UPSERT_QUERY, params.toArray(new Map[0]));
        });
    }

}
