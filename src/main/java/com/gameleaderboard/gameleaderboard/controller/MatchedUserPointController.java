package com.gameleaderboard.gameleaderboard.controller;

import com.gameleaderboard.gameleaderboard.dto.MatchedUserPointRequest;
import com.gameleaderboard.gameleaderboard.service.MatchedUserPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/matches")
public class MatchedUserPointController {

    private final MatchedUserPointService matchedUserPointService;

    @PostMapping("/{matchedId}/users/{userId}/point")
    public void addPoint(
            @PathVariable String matchedId,
            @PathVariable String userId,
            @RequestBody MatchedUserPointRequest request
    ) {
        log.info("request point");
        matchedUserPointService.add(userId, matchedId, request.point());
    }
}
