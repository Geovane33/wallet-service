package com.purebank.walletservice.wallet.domain;

import com.purebank.walletservice.wallet.enums.ActivityType;
import com.purebank.walletservice.wallet.enums.ProcessStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "wallet_activity")
public class WalletActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wallet_id")
    private Long walletId;
    @Column(name = "uuid_activity")
    private String uuidActivity;
    @Column(name = "activity_type")
    private ActivityType activityType;

    @Column(name = "status")
    private ProcessStatus status;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "activity_date")
    private LocalDateTime activityDate;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
}
