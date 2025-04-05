package com.gameleaderboard.gameleaderboard.domain.outbox;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    public List<Outbox> findByIsProcessed(Boolean isProcessed, Pageable page);

}
