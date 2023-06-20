package com.purebank.walletservice.wallet.api;

import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import com.purebank.walletservice.wallet.resource.WalletResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@RestController
@RequestMapping("/api/wallet")
@Tag(name = "Wallet API")
public interface WalletApi {

    @Operation(summary = "Criar uma nova carteira",
            description = "Cria uma nova carteira com as informações fornecidas.")
    @PostMapping
    ResponseEntity<WalletResource> createWallet(
            @Valid @RequestBody WalletResource walletResource);

    @Operation(summary = "Obter detalhes de uma carteira",
            description = "Obtém os detalhes de uma carteira com base no ID fornecido.")
    @ApiResponse(responseCode = "200", description = "Carteira encontrada",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = WalletResource.class)))
    @ApiResponse(responseCode = "404", description = "Carteira não encontrada")
    @GetMapping(value = "/{walletId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<WalletResource> getWalletById(
            @Parameter(description = "ID da carteira", required = true)
            @PathVariable Long walletId);

    @Operation(summary = "Obter o saldo de uma carteira",
            description = "Obtém o saldo de uma carteira com base no ID fornecido.")
    @GetMapping(value = "/{walletId}/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BigDecimal> getBalance(
            @Parameter(description = "ID da carteira", required = true)
            @PathVariable Long walletId);

    @Operation(summary = "Atualizar uma carteira",
            description = "Atualiza os detalhes de uma carteira com base nas informações fornecidas.")
    @PutMapping
    ResponseEntity<WalletResource> updateWallet(
            @RequestBody @Validated WalletResource walletResource);

    @Operation(summary = "Realizar um depósito em uma carteira",
            description = "Realiza um depósito na carteira com o ID fornecido.")
    @PatchMapping("/{walletId}/deposit")
    ResponseEntity<Void> deposit(
            @Parameter(description = "ID da carteira", required = true)
            @PathVariable Long walletId,
            @Parameter(description = "Valor do depósito", required = true)
            @RequestParam @Positive BigDecimal amount);

    @Operation(summary = "Realizar um saque de uma carteira",
            description = "Realiza um saque na carteira com o ID fornecido.")
    @PatchMapping("/{walletId}/withdraw")
    ResponseEntity<Void> withdraw(
            @Parameter(description = "ID da carteira", required = true)
            @PathVariable Long walletId,
            @Parameter(description = "Valor do saque", required = true)
            @RequestParam @Positive BigDecimal amount);

    @Operation(summary = "Listar todas as atividades de uma carteira",
            description = "Lista todas as atividades de uma carteira com base no ID fornecido.")
    @GetMapping("/{walletId}/activities")
    ResponseEntity<List<WalletActivityResource>> activities(
            @Parameter(description = "ID da carteira", required = true)
            @PathVariable Long walletId);
}
