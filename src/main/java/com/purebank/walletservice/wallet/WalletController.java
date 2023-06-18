package com.purebank.walletservice.wallet;

import com.purebank.walletservice.wallet.api.WalletApi;
import com.purebank.walletservice.wallet.api.resource.WalletResource;
import com.purebank.walletservice.wallet.exceptions.Exception;
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
    public ResponseEntity<Void> deposit(Long walletId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) >= 0 || new BigDecimal("1000000").compareTo(amount) <= 0) {
            throw new Exception.InvalidDepositAmount("O valor de depósito tem que ser maior que R$0,00 e menor que R$1.000.000,00");
        }
        walletService.deposit(walletId, amount);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> withdraw(Long walletId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) <= 0 || new BigDecimal("1000000").compareTo(amount) <= 0) {
            throw new Exception.InvalidDepositAmount("O valor de saque tem que ser maior que R$0,00 e menor que R$1.000.000,00");
        }
        walletService.withdraw(walletId, amount);
        return ResponseEntity.ok().build();
    }
}
