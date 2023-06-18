package com.purebank.walletservice.wallet.service.impl;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferResource {

    @Schema(description = "ID da carteira digital de origem")
    private Long id;
    @Schema(description = "ID da carteira digital de origem")
    private Long walletOrigin;

    @Schema(description = "ID da carteira digital de destino")
    private Long walletDestiny;

    @Schema(description = "Valor da transferência")
    private BigDecimal amount;
    private String statusDescription;
    @Schema(description = "Status da transferência")
    private TransferStatus status;
    @Schema(description = "Conta externa (true se for uma conta externa, false se for interna)")
    private Boolean externalAccount;

}
