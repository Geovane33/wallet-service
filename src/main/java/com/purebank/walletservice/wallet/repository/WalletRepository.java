package com.purebank.walletservice.wallet.repository;

import com.purebank.walletservice.wallet.domain.Wallet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
@Transactional
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findWalletById(@Param("id") Long walletId);

    @Query("SELECT w.balance FROM Wallet w WHERE w.id = :id")
    Optional<BigDecimal> getBalanceByWalletId(@Param("id") Long walletId);
}
