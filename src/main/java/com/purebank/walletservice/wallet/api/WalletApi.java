package com.purebank.walletservice.wallet.api;

import com.purebank.walletservice.wallet.resource.WalletResource;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
public interface WalletApi {
    @PostMapping
    ResponseEntity<WalletResource> createWallet(@RequestBody WalletResource walletResource);

    @GetMapping("/{walletId}")
    ResponseEntity<WalletResource> getWalletById(@PathVariable Long walletId);

//    @PostMapping("/{walletId}/")
//    ResponseEntity<List<WalletResource>> getWalletsById(@RequestBody @Size(max = 2) List<Long> walletId);

    @GetMapping("/{walletId}/balance")
    ResponseEntity<BigDecimal> getBalance(@PathVariable Long walletId);

    @PutMapping
    ResponseEntity<WalletResource> updateWallet(@RequestBody @Validated WalletResource walletResource);

    @PatchMapping("/{walletId}/deposit")
    ResponseEntity<Void> deposit(@PathVariable Long walletId, @RequestParam @Positive BigDecimal amount);

    @PatchMapping("/{walletId}/withdraw")
    ResponseEntity<Void> withdraw(@PathVariable Long walletId, @RequestParam @Positive BigDecimal amount);
}
