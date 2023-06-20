package com.purebank.walletservice.wallet.repository;

import com.purebank.walletservice.wallet.domain.WalletActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletActivityRepository extends JpaRepository<WalletActivity, Long> {

    Optional<WalletActivity> findByUuidActivity(String uuidActivity);

    Optional<List<WalletActivity>> findByWalletIdOrderByCreationDateDesc(Long walletId);

}
