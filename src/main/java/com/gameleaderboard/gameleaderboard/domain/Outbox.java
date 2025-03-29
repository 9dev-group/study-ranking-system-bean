package com.gameleaderboard.gameleaderboard.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity(name = "outbox")
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;
    private String eventFullName;
    private Long domainId;
    @Column(name = "payload", columnDefinition = "json")
    private String payload;
    private Boolean isProcessed;
    private Long createdAt;

    public void updateIsProcessed(boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

}
