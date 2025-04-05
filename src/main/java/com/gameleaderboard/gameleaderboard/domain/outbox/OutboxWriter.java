package com.gameleaderboard.gameleaderboard.domain.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Transactional
public class OutboxWriter {

    private final OutboxRepository outboxRepository;

    public Outbox insert(Long domainId, String eventFullName, String payload) {
        return outboxRepository.save(
                Outbox.builder()
                        .domainId(domainId)
                        .eventFullName(eventFullName)
                        .payload(payload)
                        .isProcessed(true)
                        .createdAt(Instant.now().toEpochMilli())
                        .build()
        );
    }

    public Outbox update(Outbox entity) {
        return outboxRepository.save(entity);
    }

    public void delete(Long id) {
        outboxRepository.deleteById(id);
    }
}
