package com.purebank.walletservice.wallet.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.purebank.walletservice.wallet.api.resource.WalletResource;
import com.purebank.walletservice.wallet.domain.Wallet;
import com.purebank.walletservice.wallet.domain.WalletActivity;
import com.purebank.walletservice.wallet.exceptions.Exception;
import com.purebank.walletservice.wallet.repository.WalletRepository;
import com.purebank.walletservice.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public WalletResource createWallet(WalletResource walletResource) {
        try {
            Wallet wallet = new Wallet();
            wallet.setName(walletResource.getName());
            wallet.setCreationDate(LocalDateTime.now());
            walletRepository.save(wallet);
            walletResource.setId(wallet.getId()); // Atualiza o ID da carteira no objeto de recurso
            return walletResource;
        } catch (DataAccessException e) {
            // Tratamento da exceção de persistência
            // Pode ser lançada uma exceção personalizada, log do erro, retorno de uma mensagem de erro específica, etc.
            throw new Exception("Erro ao criar a carteira.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public WalletResource getWalletById(Long walletId) {
        Wallet wallet = findWalletById(walletId);
        WalletResource walletResource = new WalletResource();
        walletResource.setId(wallet.getId());
        walletResource.setName(wallet.getName());
        walletResource.setBalance(wallet.getBalance());
        return walletResource;
    }

    @Override
    public BigDecimal getBalance(Long walletId) {
        return walletRepository.getBalanceByWalletId(walletId);
    }

    @Override
    public WalletResource updateWallet(WalletResource walletResource) {
        Wallet wallet = new Wallet();
        wallet.setId(walletResource.getId());
        wallet.setBalance(walletResource.getBalance());
        wallet.setName(walletResource.getName());
        walletRepository.save(wallet);
        return walletResource;
    }

    @Override
    public Boolean deposit(Long walletId, BigDecimal amount) {
        Wallet wallet = findWalletById(walletId);

        try {
            wallet.setBalance(wallet.getBalance().add(amount));
            walletRepository.save(wallet);
            saveWalletActivities(amount, "Deposit");
        } catch (Exception e) {
            log.error("Falha ao efetivar deposito: {}", e.getMessage());
            throw new Exception.FailedToDeposit("Falha ao efetivar deposito.");
        }
        return true;
    }

    @Override
    public Boolean withdraw(Long walletId, BigDecimal amount) {
        Wallet wallet = findWalletById(walletId);
        try {
            wallet.setBalance(wallet.getBalance().subtract(amount));
            walletRepository.save(wallet);
        } catch (Exception e) {
            log.error("Falha ao efetivar saque: {}", e.getMessage());
            throw new Exception.FailedToDeposit("Falha ao efetivar deposito.");
        }
        return true;
        //todo - Validar saldo e valor - tratar exceções
    }

    private Wallet findWalletById(Long walletId) {
        Optional<Wallet> walletOptional = walletRepository.findWalletById(walletId);
        return walletOptional.orElseThrow(() -> new Exception.NotFound("Carteira não encontrada com o ID: " + walletId));
    }

    private void saveWalletActivities(BigDecimal amount, String activityType) {
        WalletActivity walletActivity = new WalletActivity();
        walletActivity.setAmount(amount);
        walletActivity.setActivityType(activityType);
        walletActivity.setActivityDate(LocalDateTime.now());
        walletActivity.setCreationDate(LocalDateTime.now());
        walletActivity.setLastUpdate(LocalDateTime.now());
        rabbitTemplate.convertAndSend("direct-exchange-default", "queue-wallet-activity-key", walletActivity);
    }

    @RabbitListener(queues = "update-accounts-balance")
    public void updateAccountsBalance(TransferResource transferResource) {
        Optional<Wallet> walletOriginOptional = walletRepository.findWalletById(transferResource.getWalletOrigin());
        Optional<Wallet> walletDestinyOptional = walletRepository.findWalletById(transferResource.getWalletDestiny());

        String errorMessage = null;
        transferResource.setStatus(TransferStatus.FAILED);
        if (walletOriginOptional.isEmpty()) {
            errorMessage = "Falha ao processar a transferência: Conta de origem não existe";
        } else if (walletDestinyOptional.isEmpty()) {
            errorMessage = "Falha ao processar a transferência: Conta de destino não existe";
        } else if (walletOriginOptional.get().getBalance().compareTo(transferResource.getAmount()) < 0) {
            errorMessage = "Falha ao processar a transferência: saldo insuficiente";
        }
        if (errorMessage == null) {
            Wallet walletOrigin = walletOriginOptional.get();
            Wallet walletDestiny = walletDestinyOptional.get();

            walletOrigin.setBalance(walletOrigin.getBalance().subtract(transferResource.getAmount()));
            walletRepository.save(walletOrigin);

            walletDestiny.setBalance(walletDestiny.getBalance().add(transferResource.getAmount()));
            walletRepository.save(walletDestiny);
            transferResource.setStatus(TransferStatus.COMPLETED);
        }
        updateStatusTransfer(transferResource, StringUtils.defaultString(errorMessage));
    }

    private void updateStatusTransfer(TransferResource transferResource, String statusDescription) {
        transferResource.setStatusDescription(statusDescription);
        rabbitTemplate.convertAndSend("direct-exchange-default", "queue-update-status-transfer-key", transferResource);
    }
}
