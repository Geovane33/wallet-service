package com.purebank.walletservice.wallet.api.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletResource {

    @Schema(description = "id da carteira digital")
    private Long id;
    @Schema(description = "Nome do dono da carteira digital")
    @NotBlank(message = "Informe o nome do dono da carteira")
    private String name;

    @Schema(description = "Saldo da carteira")
    @NotNull(message = "O saldo da carteira não poder ser nulo")
    private BigDecimal balance;

}
