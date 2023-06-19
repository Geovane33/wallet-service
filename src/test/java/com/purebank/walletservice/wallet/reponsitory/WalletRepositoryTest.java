package com.purebank.walletservice.wallet.reponsitory;

import com.purebank.walletservice.wallet.domain.Wallet;
import com.purebank.walletservice.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
@Transactional
public class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;

    @Test
    @Rollback
    @DisplayName("Testando método findWalletById do WalletRepository")
    void testFindWalletById() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setName("Teste");
        wallet.setCreationDate(LocalDateTime.now());
        wallet.setActive(Boolean.TRUE);
        wallet.setLastUpdate(LocalDateTime.now());
        wallet.setBalance(BigDecimal.valueOf(100.0));
        walletRepository.save(wallet);

        Optional<Wallet> result = walletRepository.findWalletById(1L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(wallet.getId(), result.get().getId());
        Assertions.assertEquals(wallet.getBalance(), result.get().getBalance());
    }

    @Test
    @Rollback
    @DisplayName("Testando método getBalanceByWalletId do WalletRepository")
    void testGetBalanceByWalletId() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setName("Teste");
        wallet.setCreationDate(LocalDateTime.now());
        wallet.setActive(Boolean.TRUE);
        wallet.setLastUpdate(LocalDateTime.now());
        wallet.setBalance(new BigDecimal("100.00"));
        walletRepository.save(wallet);

        Optional<BigDecimal> result = walletRepository.getBalanceByWalletId(1L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(wallet.getBalance(), result.get());
    }
}

