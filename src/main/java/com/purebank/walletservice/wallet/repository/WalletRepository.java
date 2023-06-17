package com.purebank.walletservice.wallet.repository;

import com.purebank.walletservice.wallet.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Wallet findWalletById(@Param("id") Long walletId);

    @Query("SELECT w.balance FROM Wallet w WHERE w.id = :id")
    BigDecimal getBalanceByWalletId(@Param("id") Long walletId);
}
