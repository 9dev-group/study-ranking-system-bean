package com.gameleaderboard.gameleaderboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameleaderboard.gameleaderboard.domain.Outbox;
import com.gameleaderboard.gameleaderboard.domain.OutboxWriter;
import com.gameleaderboard.gameleaderboard.event.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class EventHandler {

    private final OutboxWriter outboxWriter;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public void sendEvent(Event event, String topic) {
        Outbox outbox = insertOutbox(event);
        sendEvent(event, topic, outbox);
    }

    public void sendEvent(Event event, String topic, Outbox outbox) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                var future = kafkaTemplate.send(topic, event.getEventId(), event);
                future.whenComplete((result, ex) -> {
                    if (ex == null) {
                        outboxWriter.delete(outbox.getId());
                    } else {
                        outbox.updateIsProcessed(false);
                        outboxWriter.update(outbox);
                    }
                });
            }
        });
    }

    private Outbox insertOutbox(Event event) {
        try {
            var eventJson = objectMapper.writeValueAsString(event);
            return outboxWriter.insert(event.getDomainId(), event.getClass().getName(), eventJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
