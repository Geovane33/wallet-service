package com.purebank.walletservice.wallet.resource;

import com.purebank.walletservice.wallet.enums.ActivityType;
import com.purebank.walletservice.wallet.enums.ProcessStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representa uma atividade da carteira")
public class WalletActivityResource implements Serializable {
    @Schema(description = "ID da carteira")
    private Long walletId;

    @Schema(description = "O tipo de atividade da carteira")
    private ActivityType activityType;

    @Schema(description = "O status do processo da atividade da carteira")
    private ProcessStatus status;

    @Schema(description = "O valor da atividade da carteira")
    private BigDecimal amount;

    @Schema(description = "A descrição da atividade da carteira")
    private String description;

    @Schema(description = "A data da atividade da carteira")
    private LocalDateTime activityDate;

    @Schema(description = "A data de criação da atividade da carteira")
    private LocalDateTime creationDate;

    @Schema(description = "O UUID da atividade da carteira")
    private String uuidActivity;
}
