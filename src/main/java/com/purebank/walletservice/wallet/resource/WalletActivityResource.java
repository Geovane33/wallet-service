package com.purebank.walletservice.wallet.resource;

import com.purebank.walletservice.wallet.enums.ActivityType;
import com.purebank.walletservice.wallet.enums.ProcessStatus;
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

    private ActivityType activityType;

    private ProcessStatus status;

    private BigDecimal amount;

    private String description;

    private LocalDateTime activityDate;

    private LocalDateTime creationDate;

    private String uuidActivity;
}
