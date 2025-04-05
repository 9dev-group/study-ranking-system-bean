package com.gameleaderboard.gameleaderboard.domain.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OutboxReader {

    private final OutboxRepository outboxRepository;

    public List<Outbox> findByIsProcessed(Boolean isProcessed, Pageable page) {
        return outboxRepository.findByIsProcessed(isProcessed, page);
    }

}
