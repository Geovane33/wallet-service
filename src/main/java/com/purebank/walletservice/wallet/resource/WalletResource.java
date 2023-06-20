package com.purebank.walletservice.wallet.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representa uma carteira digital")
public class WalletResource implements Serializable{

    @Schema(description = "id da carteira digital")
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @Schema(description = "Nome do dono da carteira digital")
    @NotBlank(message = "Informe o nome do dono da carteira")
    private String name;

    @Schema(description = "Saldo da carteira")
    @NotNull(message = "O saldo da carteira não poder ser nulo")
    private BigDecimal balance;

}
