package com.purebank.walletservice.wallet.repository;

import com.purebank.walletservice.wallet.domain.Wallet;
import com.purebank.walletservice.wallet.domain.WalletActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface WalletActivityRepository extends JpaRepository<WalletActivity, Long> {

    Optional<WalletActivity> findByUuidActivity(String uuidActivity);

}
