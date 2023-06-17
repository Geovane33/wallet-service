package com.purebank.walletservice.wallet;

import com.purebank.walletservice.wallet.api.WalletApi;
import com.purebank.walletservice.wallet.api.resource.WalletResource;
import com.purebank.walletservice.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class WalletController implements WalletApi {

    @Autowired
    private WalletService walletService;
    @Override
    public ResponseEntity<WalletResource> createWallet(WalletResource walletResource) {
        WalletResource wallet = walletService.createWallet(walletResource);
        return ResponseEntity.ok(wallet);
    }

    @Override
    public ResponseEntity<WalletResource> getWalletById(Long walletId) {
        WalletResource WalletResource = walletService.getWalletById(walletId);
        return ResponseEntity.ok(WalletResource);
    }

    @Override
    public ResponseEntity<BigDecimal> getBalance(Long walletId) {
        BigDecimal balance = walletService.getBalance(walletId);
        return ResponseEntity.ok(balance);
    }

    @Override
    public ResponseEntity<WalletResource> updateWallet(WalletResource walletResource) {
        walletResource = walletService.updateWallet(walletResource);
        return ResponseEntity.ok(walletResource);
    }

    @Override
    public ResponseEntity<WalletResource> deposit(String accountId, BigDecimal amount) {
        return null;
    }

    @Override
    public ResponseEntity<WalletResource> withdraw(String accountId, BigDecimal amount) {
        return null;
    }
}
