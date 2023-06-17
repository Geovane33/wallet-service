package com.purebank.accountservice.wallet.service;

import com.purebank.walletservice.wallet.api.resource.WalletResource;
import com.purebank.walletservice.wallet.domain.Wallet;
import com.purebank.walletservice.wallet.repository.WalletRepository;
import com.purebank.walletservice.wallet.service.WalletService;
import com.purebank.walletservice.wallet.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(properties = {"spring.flyway.enabled=false"})
//@ExtendWith(SpringExtension.class)
//@ExtendWith(MockitoExtension.class)
//@AutoConfigureMockMvc
@SpringBootTest(classes = {WalletService.class, WalletServiceImpl.class})
public class WalletServiceTest {
    @Autowired
    private WalletService walletService;

    @MockBean
    private WalletRepository walletRepository;

    @BeforeEach
    void setup() {
        // Configuração inicial, se necessário
    }

    @Test
    void createWalletTest() {
        Wallet walletEntity = new Wallet();
        walletEntity.setBalance(BigDecimal.valueOf(10));
        walletEntity.setName("Test");
        walletEntity.setId(1L);
        Mockito.doReturn(walletEntity).when(walletRepository).save(Mockito.any());

        WalletResource walletResource = new WalletResource();
        walletResource.setBalance(BigDecimal.valueOf(10));
        walletResource.setName("Test");
        WalletResource walletById = walletService.createWallet(walletResource);

        walletResource.setId(1L);
        Assertions.assertEquals(walletResource, walletById);
    }

    @Test
    void getWalletByIdTest() {
        Wallet walletEntity = new Wallet();
        walletEntity.setBalance(BigDecimal.valueOf(10));
        walletEntity.setName("Test");
        walletEntity.setId(1L);
        Mockito.doReturn(walletEntity).when(walletRepository).findWalletById(walletEntity.getId());

        WalletResource walletById = walletService.getWalletById(walletEntity.getId());

        WalletResource walletResource = new WalletResource();
        walletResource.setBalance(BigDecimal.valueOf(10));
        walletResource.setName("Test");
        walletResource.setId(1L);
        Assertions.assertEquals(walletResource, walletById);
    }

    @Test
    void getBalanceTest() {
        Wallet walletEntity = new Wallet();
        walletEntity.setBalance(BigDecimal.valueOf(10));
        walletEntity.setName("Test");
        walletEntity.setId(1L);
        Mockito.doReturn(walletEntity.getBalance()).when(walletRepository).getBalanceByWalletId(walletEntity.getId());

        BigDecimal balance = walletService.getBalance(walletEntity.getId());

        Assertions.assertEquals(walletEntity.getBalance(), balance);
    }

    @Test
    void updateWalletTest() {
        Wallet walletEntity = new Wallet();
        walletEntity.setBalance(BigDecimal.valueOf(10));
        walletEntity.setName("Test");
        walletEntity.setId(1L);
        Mockito.doReturn(walletEntity).when(walletRepository).save(walletEntity);

        WalletResource walletResource = new WalletResource();
        walletResource.setBalance(BigDecimal.valueOf(10));
        walletResource.setName("Test");
        walletResource.setId(1L);

        WalletResource WalletUpdated = walletService.updateWallet(walletResource);

        Assertions.assertEquals(walletResource, WalletUpdated);
    }

    @Test
    void depositTest() {
        Wallet walletEntity = new Wallet();
        walletEntity.setBalance(BigDecimal.valueOf(10));
        walletEntity.setName("Test");
        walletEntity.setId(1L);

        Mockito.doReturn(walletEntity).when(walletRepository).findWalletById(walletEntity.getId());

        Boolean deposit = walletService.deposit(walletEntity.getId(), new BigDecimal("10.00"));

        Assertions.assertEquals(Boolean.TRUE, deposit);
    }

    @Test
    void withdrawSucessTest() {
        Wallet walletEntity = new Wallet();
        walletEntity.setBalance(BigDecimal.valueOf(10));
        walletEntity.setName("Test");
        walletEntity.setId(1L);

        Mockito.doReturn(walletEntity).when(walletRepository).findWalletById(walletEntity.getId());

        Boolean withdraw = walletService.withdraw(walletEntity.getId(), new BigDecimal("10.00"));

        Assertions.assertEquals(Boolean.TRUE, withdraw, "Erro ao validar saque");
    }

    @Test
    void withdrawFailedTest() {
        Wallet walletEntity = new Wallet();
        walletEntity.setBalance(BigDecimal.valueOf(0));
        walletEntity.setName("Test");
        walletEntity.setId(1L);

        Mockito.doReturn(walletEntity).when(walletRepository).findWalletById(walletEntity.getId());

        Boolean withdraw = walletService.withdraw(walletEntity.getId(), new BigDecimal("10.00"));

        Assertions.assertEquals(Boolean.FALSE, withdraw, "Erro ao validar saque");
    }

    @Test
    void withdrawErrorTest() {
        Wallet walletEntity = new Wallet();
        walletEntity.setBalance(BigDecimal.valueOf(10));
        walletEntity.setName("Test");
        walletEntity.setId(1L);

        Mockito.doReturn(walletEntity).when(walletRepository).findWalletById(walletEntity.getId());

        Boolean withdraw = walletService.withdraw(walletEntity.getId(), null);

        Assertions.assertEquals(Boolean.FALSE, withdraw, "Erro ao validar saque");
    }
}
