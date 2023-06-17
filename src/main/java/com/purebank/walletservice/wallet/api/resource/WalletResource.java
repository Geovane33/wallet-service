package com.purebank.walletservice.wallet.api.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletResource {

    @Schema(description = "id da carteira digital")
    private Long id;
    @Schema(description = "Nome do dono da carteira digital")
    private String name;

    @Schema(description = "Saldo da carteira")
    private BigDecimal balance;

}
