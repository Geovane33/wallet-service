package com.purebank.walletservice.wallet.service.impl;

import com.purebank.walletservice.wallet.domain.Wallet;
import com.purebank.walletservice.wallet.exceptions.Exception;
import com.purebank.walletservice.wallet.message.producer.WalletMessageProducer;
import com.purebank.walletservice.wallet.repository.WalletRepository;
import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import com.purebank.walletservice.wallet.resource.WalletResource;
import com.purebank.walletservice.wallet.service.WalletService;
import com.purebank.walletservice.wallet.enums.ProcessStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletMessageProducer walletMessageProducer;

    @Override
    public WalletResource createWallet(WalletResource walletResource) {
        try {
            Wallet wallet = new Wallet();
            wallet.setName(walletResource.getName());
            wallet.setCreationDate(LocalDateTime.now());
            walletRepository.save(wallet);
            walletResource.setId(wallet.getId()); // Atualiza o ID da carteira no objeto de recurso
            return walletResource;
        } catch (DataAccessException e) {
            log.error("Erro ao criar carteira: {}", e.getMessage());
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
            sendWalletActivities(walletId, amount, "deposit", ProcessStatus.COMPLETED, "Depósito realizado com sucesso");
        } catch (Exception e) {
            log.error("Falha ao efetivar depósito: {}", e.getMessage());
            sendWalletActivities(walletId, amount, "deposit", ProcessStatus.FAILED, "Não foi possível processar o depósito");
            throw new Exception.FailedToDeposit("Falha ao efetivar deposito.");
        }
        return true;
    }

    @Override
    public Boolean withdraw(Long walletId, BigDecimal amount) {
        Wallet wallet = findWalletById(walletId);
        if (amount.compareTo(wallet.getBalance()) > 0) {
            sendWalletActivities(walletId, amount, "withdraw", ProcessStatus.FAILED,
                    String.format("Saldo insuficiente ao realizar o saque no valor de RS$ %f", amount));
            throw new Exception.InvalidAmount("Falha ao efetivar saque: Saldo insuficiente");
        }

        try {
            wallet.setBalance(wallet.getBalance().subtract(amount));
            walletRepository.save(wallet);
            sendWalletActivities(walletId, amount, "withdraw", ProcessStatus.COMPLETED, "Saque realizado com sucesso");
        } catch (Exception e) {
            sendWalletActivities(walletId, amount, "withdraw", ProcessStatus.FAILED, "Não foi possível processar o saque");
            log.error("Falha ao efetivar saque: {}", e.getMessage());
            throw new Exception.FailedToWithdraw("Falha ao efetivar saque.");
        }
        return true;
    }

    @Override
    public List<WalletActivityResource> activities(Long walletId) {
        return null;
    }

    private Wallet findWalletById(Long walletId) {
        Optional<Wallet> walletOptional = walletRepository.findWalletById(walletId);
        return walletOptional.orElseThrow(() -> new Exception.NotFound("Carteira não encontrada com o ID: " + walletId));
    }

    private void sendWalletActivities(Long walletId, BigDecimal amount, String activityType, ProcessStatus status, String description) {
        WalletActivityResource walletActivityResource = new WalletActivityResource();
        walletActivityResource.setWalletId(walletId);
        walletActivityResource.setActivityDate(LocalDateTime.now());
        walletActivityResource.setActivityType(activityType);
        walletActivityResource.setAmount(amount);
        walletActivityResource.setStatus(status);
        walletActivityResource.setDescription(description);
        walletActivityResource.setCreationDate(LocalDateTime.now());
        walletMessageProducer.sendWalletActivity(walletActivityResource);
    }

}
