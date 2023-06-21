package com.purebank.walletservice.wallet.service;

import com.purebank.walletservice.wallet.domain.Wallet;
import com.purebank.walletservice.wallet.enums.ActivityType;
import com.purebank.walletservice.wallet.enums.ProcessStatus;
import com.purebank.walletservice.wallet.exceptions.Exception;
import com.purebank.walletservice.wallet.message.producer.WalletMessageProducer;
import com.purebank.walletservice.wallet.repository.WalletRepository;
import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import com.purebank.walletservice.wallet.resource.WalletResource;
import com.purebank.walletservice.wallet.service.impl.WalletServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest(classes = {WalletService.class, WalletServiceImpl.class})
public class WalletServiceTest {
    @Autowired
    private WalletService walletService;

    @MockBean
    private WalletMessageProducer walletMessageProducer;

    @MockBean
    private WalletRepository walletRepository;

    @Test
    @DisplayName("Criar Carteira - Sucesso")
    void createWalletWithSucessTest() {
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
    @DisplayName("Criar Carteira - Falha")
    void createWalletWithFailedTest() {
        Mockito.when(walletRepository.save(Mockito.any(Wallet.class))).thenThrow(new RuntimeException("Mock RuntimeException") {
        });

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            walletService.createWallet(Mockito.mock(WalletResource.class));
        });
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        Assertions.assertEquals("Erro ao criar a carteira.", exception.getMessage());
    }

    @Test
    @DisplayName("Obter Carteira por ID")
    void getWalletByIdTest() {
        Wallet walletEntity = new Wallet();
        walletEntity.setBalance(BigDecimal.valueOf(10));
        walletEntity.setName("Test");
        walletEntity.setId(1L);
        Mockito.doReturn(Optional.of(walletEntity)).when(walletRepository).findWalletById(walletEntity.getId());

        WalletResource walletById = walletService.getWalletById(walletEntity.getId());

        WalletResource walletResource = new WalletResource();
        walletResource.setBalance(BigDecimal.valueOf(10));
        walletResource.setName("Test");
        walletResource.setId(1L);
        Assertions.assertEquals(walletResource, walletById);
    }

    @Test
    @DisplayName("Obter Carteira por ID - Carteira não encontrada")
    void getWalletByIdWalletNotFoundTest() {
        Mockito.when(walletRepository.findWalletById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(Exception.NotFound.class, () -> {
            walletService.getWalletById(123L);
        });
    }

    @Test
    @DisplayName("Obter Saldo")
    void getBalanceTest() {
        Wallet walletEntity = new Wallet();
        walletEntity.setBalance(BigDecimal.valueOf(10));
        walletEntity.setName("Test");
        walletEntity.setId(1L);
        Mockito.doReturn(Optional.of(walletEntity.getBalance())).when(walletRepository).getBalanceByWalletId(walletEntity.getId());

        BigDecimal balance = walletService.getBalance(walletEntity.getId());

        Assertions.assertEquals(walletEntity.getBalance(), balance);
    }

    @Test
    @DisplayName("Obter Saldo - Carteira não encontrada")
    void getBalanceWalletNotFoundTest() {
        Mockito.when(walletRepository.getBalanceByWalletId(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(Exception.NotFound.class, () -> {
            walletService.getBalance(123L);
        });
    }

    @Test
    @DisplayName("Atualizar Carteira")
    void updateWalletTest() {
        Wallet walletEntity = new Wallet();
        walletEntity.setBalance(BigDecimal.valueOf(10));
        walletEntity.setName("Test");
        walletEntity.setId(1L);
        Mockito.doReturn(Optional.of(walletEntity)).when(walletRepository).findWalletById(walletEntity.getId());

        WalletResource walletResource = new WalletResource();
        walletResource.setBalance(BigDecimal.valueOf(10));
        walletResource.setName("Test");
        walletResource.setId(1L);

        WalletResource WalletUpdated = walletService.updateWallet(walletResource);

        Assertions.assertEquals(walletResource, WalletUpdated);
    }

    @Test
    @DisplayName("Atualizar Carteira - Falha")
    void updateWalletFailedTest() {
        Mockito.when(walletRepository.save(Mockito.any(Wallet.class)))
                .thenThrow(new RuntimeException("Mock RuntimeException") {});
        WalletResource walletResource = new WalletResource();
        walletResource.setId(123L);
        Assertions.assertThrows(Exception.class, () -> walletService.updateWallet(walletResource));
    }


    @Test
    @DisplayName("Depositar")
    void depositTest() {
        Wallet walletEntity = new Wallet();
        walletEntity.setBalance(BigDecimal.valueOf(10));
        walletEntity.setName("Test");
        walletEntity.setId(1L);

        Mockito.doReturn(Optional.of(walletEntity)).when(walletRepository).findWalletById(walletEntity.getId());

        Boolean deposit = walletService.deposit(walletEntity.getId(), new BigDecimal("10.00"));

        Assertions.assertEquals(Boolean.TRUE, deposit);
    }

    @Test
    @SneakyThrows
    @DisplayName("Depositar - Falha")
    public void testFailedDepositThrowsException() {
        Wallet walletEntity = new Wallet();
        walletEntity.setBalance(BigDecimal.valueOf(10));
        walletEntity.setName("Test");
        walletEntity.setId(123L);

        Mockito.doReturn(Optional.of(walletEntity)).when(walletRepository).findWalletById(walletEntity.getId());

        Mockito.doThrow(new RuntimeException("Mock RuntimeException")).when(walletRepository).save(Mockito.any(Wallet.class));

        Assertions.assertThrows(RuntimeException.class, () -> {
            walletService.deposit(walletEntity.getId(), BigDecimal.valueOf(50.0));
        });
    }

    @Test
    @DisplayName("Sacar - Sucesso")
    void withdrawSucessTest() {
        Wallet walletEntity = new Wallet();
        walletEntity.setBalance(BigDecimal.valueOf(10));
        walletEntity.setName("Test");
        walletEntity.setId(1L);

        Mockito.doReturn(Optional.of(walletEntity)).when(walletRepository).findWalletById(walletEntity.getId());

        Boolean withdraw = walletService.withdraw(walletEntity.getId(), new BigDecimal("10.00"));

        Assertions.assertEquals(Boolean.TRUE, withdraw, "Erro ao validar saque");
    }

    @Test
    @DisplayName("Sacar - Valor Maior que o Saldo")
    void withdrawAmountGreaterThanBalanceThrowsExceptionTest() {
        Wallet walletEntity = new Wallet();
        walletEntity.setBalance(BigDecimal.valueOf(0));
        walletEntity.setName("Test");
        walletEntity.setId(1L);

        Mockito.doReturn(Optional.of(walletEntity)).when(walletRepository).findWalletById(walletEntity.getId());
        BigDecimal withdrawalGreaterThanBalance = new BigDecimal("10.00");
        Assertions.assertThrows(Exception.InvalidAmount.class, () -> walletService.withdraw(walletEntity.getId(), withdrawalGreaterThanBalance));
    }

    @Test
    @SneakyThrows
    @DisplayName("Registrar atividades da Carteira")
    public void testSendWalletActivities() {
        Long walletId = 1L;
        BigDecimal amount = BigDecimal.TEN;
        ActivityType activityType = ActivityType.DEPOSIT;
        ProcessStatus status = ProcessStatus.COMPLETED;
        String description = "Depósito realizado com sucesso";

        Method sendWalletActivitiesMethod = WalletServiceImpl.class
                .getDeclaredMethod("sendWalletActivities", Long.class, BigDecimal.class, ActivityType.class, ProcessStatus.class, String.class);
        sendWalletActivitiesMethod.setAccessible(true);

        sendWalletActivitiesMethod.invoke(walletService, walletId, amount, activityType, status, description);

        Mockito.verify(walletMessageProducer, Mockito.times(1)).sendWalletActivity(Mockito.any(WalletActivityResource.class));

        // Capturar o argumento passado para o método sendWalletActivities
        ArgumentCaptor<WalletActivityResource> captor = ArgumentCaptor.forClass(WalletActivityResource.class);
        Mockito.verify(walletMessageProducer).sendWalletActivity(captor.capture());

        WalletActivityResource capturedWalletActivityResource = captor.getValue();
        Assertions.assertEquals(walletId, capturedWalletActivityResource.getWalletId());
        Assertions.assertEquals(LocalDateTime.now().getDayOfMonth(), capturedWalletActivityResource.getActivityDate().getDayOfMonth());
        Assertions.assertEquals(activityType, capturedWalletActivityResource.getActivityType());
        Assertions.assertEquals(amount, capturedWalletActivityResource.getAmount());
        Assertions.assertEquals(status, capturedWalletActivityResource.getStatus());
        Assertions.assertEquals(description, capturedWalletActivityResource.getDescription());
        Assertions.assertEquals(LocalDateTime.now().getDayOfMonth(), capturedWalletActivityResource.getCreationDate().getDayOfMonth());
    }
}
