package com.purebank.walletservice.wallet.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallet")
@Data
public class Wallet {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private BigDecimal balance;
        private boolean active;
        @Column(name = "creation_date", nullable = false, updatable = false)
        private LocalDateTime creationDate;
        @Column(name = "last_update")
        private LocalDateTime lastUpdate;
}
