package com.purebank.walletservice.wallet.service;

import com.purebank.walletservice.wallet.resource.WalletResource;

import java.math.BigDecimal;

public interface WalletService {

    WalletResource createWallet(WalletResource walletResource);

    WalletResource getWalletById(Long walletId);

    BigDecimal getBalance(Long walletId);

    WalletResource updateWallet(WalletResource walletResource);

    Boolean deposit(Long accountId, BigDecimal amount);

    Boolean withdraw(Long accountId, BigDecimal amount);
}
