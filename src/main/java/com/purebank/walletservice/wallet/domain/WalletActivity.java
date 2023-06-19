package com.purebank.walletservice.wallet.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "wallet_activities")
public class WalletActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movement_identifier")
    private Long movementIdentifier;
    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "status")
    private String status;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "activity_date")
    private LocalDateTime activityDate;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
