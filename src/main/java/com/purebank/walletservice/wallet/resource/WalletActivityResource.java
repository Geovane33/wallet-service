package com.purebank.walletservice.wallet.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletActivityResource implements Serializable {

    private Long walletId;

    private Long movementIdentifier;

    private String activityType;

    private String status;

    private BigDecimal amount;

    private String description;
    private LocalDateTime activityDate;

    private LocalDateTime creationDate;

}
