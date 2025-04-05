package com.gameleaderboard.gameleaderboard.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameleaderboard.gameleaderboard.config.KafkaProperties;
import com.gameleaderboard.gameleaderboard.domain.outbox.OutboxReader;
import com.gameleaderboard.gameleaderboard.event.Event;
import com.gameleaderboard.gameleaderboard.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxReader outboxReader;
    private final EventHandler eventHandler;
    private final KafkaProperties properties;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 1000 * 10)
    public void run() {
        outboxReader.findByIsProcessed(false, PageRequest.of(0, 100))
                // todo 후에 병렬처리
                .forEach(outbox -> {
                    Object event;
                    try {
                        event = objectMapper.readValue(outbox.getPayload(), Class.forName(outbox.getEventFullName()));
                        eventHandler.sendEvent((Event) event, properties.topic(), outbox);
                    } catch (JsonProcessingException e) {
                        log.error("[run] JsonProcessingException please check entity: " + outbox, e);
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        log.error("[run] ClassNotFoundException please check entity: " + outbox, e);
                        throw new RuntimeException(e);
                    }
                });

    }

}
