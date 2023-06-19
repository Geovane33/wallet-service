package com.purebank.walletservice.wallet;

import com.purebank.walletservice.wallet.api.WalletApi;
import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import com.purebank.walletservice.wallet.resource.WalletResource;
import com.purebank.walletservice.wallet.exceptions.Exception;
import com.purebank.walletservice.wallet.service.WalletActivityService;
import com.purebank.walletservice.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static com.purebank.walletservice.wallet.utils.constants.Constants.*;


@RestController
public class WalletController implements WalletApi {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletActivityService walletActivityService;

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
        if (BigDecimal.ZERO.compareTo(amount) >= 0 || new BigDecimal("1000000.00").compareTo(amount) <= 0) {
            throw new Exception.InvalidAmount(DEPOSIT_AMOUNT_MUST_BE_GREATER_THAN_XXXX_AND_LESS_THAN_XXXX);
        }
        walletService.deposit(walletId, amount);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> withdraw(Long walletId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) >= 0 || new BigDecimal("1000000.00").compareTo(amount) <= 0) {
            throw new Exception.InvalidAmount(WITHDRAWAL_AMOUNT_MUST_BE_GREATER_THAN_XXXX_AND_LESS_THAN_XXXX);
        }
        walletService.withdraw(walletId, amount);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<WalletActivityResource>> activities(Long walletId) {
        return ResponseEntity.ok(walletActivityService.activities(walletId));
    }
}
