package com.purebank.walletservice.wallet.api;

import com.purebank.walletservice.wallet.api.resource.WalletResource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
    ResponseEntity<WalletResource> updateWallet(@RequestBody WalletResource walletResource);

    @PatchMapping("/{walletId}/deposit")
    ResponseEntity<WalletResource> deposit(@PathVariable String accountId, @RequestParam BigDecimal amount);

    @PatchMapping("/{walletId}/withdraw")
    ResponseEntity<WalletResource> withdraw(@PathVariable String accountId, @RequestParam BigDecimal amount);
}
