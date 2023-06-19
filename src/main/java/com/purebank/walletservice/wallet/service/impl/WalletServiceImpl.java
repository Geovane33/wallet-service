package com.purebank.walletservice.wallet.service.impl;

import com.purebank.walletservice.wallet.domain.Wallet;
import com.purebank.walletservice.wallet.exceptions.Exception;
import com.purebank.walletservice.wallet.message.producer.WalletMessageProducer;
import com.purebank.walletservice.wallet.repository.WalletRepository;
import com.purebank.walletservice.wallet.resource.TransferResource;
import com.purebank.walletservice.wallet.resource.MovimentStatus;
import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import com.purebank.walletservice.wallet.resource.WalletResource;
import com.purebank.walletservice.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
            sendWalletActivities(walletId, amount, "deposit", "COMPLETED", "Depósito realizado com sucesso");
        } catch (Exception e) {
            log.error("Falha ao efetivar depósito: {}", e.getMessage());
            sendWalletActivities(walletId, amount, "deposit", "FAILED", "Não foi possível processar o depósito");
            throw new Exception.FailedToDeposit("Falha ao efetivar deposito.");
        }
        return true;
    }

    @Override
    public Boolean withdraw(Long walletId, BigDecimal amount) {
        Wallet wallet = findWalletById(walletId);
        if (amount.compareTo(wallet.getBalance()) > 0) {
            sendWalletActivities(walletId, amount, "withdraw", "FAILED",
                    String.format("Saldo insuficiente ao realizar o saque no valor de RS$ %f", amount));
            throw new Exception.InvalidAmount("Falha ao efetivar saque: Saldo insuficiente");
        }

        try {
            wallet.setBalance(wallet.getBalance().subtract(amount));
            walletRepository.save(wallet);
            sendWalletActivities(walletId, amount, "withdraw", "COMPLETED", "Saque realizado com sucesso");
        } catch (Exception e) {
            sendWalletActivities(walletId, amount, "withdraw", "FAILED", "Não foi possível processar o saque");
            log.error("Falha ao efetivar saque: {}", e.getMessage());
            throw new Exception.FailedToWithdraw("Falha ao efetivar saque.");
        }
        return true;
    }

    private Wallet findWalletById(Long walletId) {
        Optional<Wallet> walletOptional = walletRepository.findWalletById(walletId);
        return walletOptional.orElseThrow(() -> new Exception.NotFound("Carteira não encontrada com o ID: " + walletId));
    }

    public void sendWalletActivities(Long walletId, BigDecimal amount, String activityType, String status, String description) {
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

    public void updateAccountsBalance(TransferResource transferResource) {
        Optional<Wallet> walletOriginOptional = walletRepository.findWalletById(transferResource.getWalletOrigin());
        Optional<Wallet> walletDestinyOptional = walletRepository.findWalletById(transferResource.getWalletDestiny());

        String errorMessage = StringUtils.EMPTY;
        transferResource.setStatus(MovimentStatus.FAILED);
        if (walletOriginOptional.isEmpty()) {
            errorMessage = "Falha ao processar a transferência: Conta de origem não existe";
        } else if (walletDestinyOptional.isEmpty()) {
            errorMessage = "Falha ao processar a transferência: Conta de destino não existe";
        } else if (walletOriginOptional.get().getBalance().compareTo(transferResource.getAmount()) < 0) {
            errorMessage = "Falha ao processar a transferência: saldo insuficiente";
        }
        if (StringUtils.isBlank(errorMessage)) {
            Wallet walletOrigin = walletOriginOptional.get();
            Wallet walletDestiny = walletDestinyOptional.get();

            walletOrigin.setBalance(walletOrigin.getBalance().subtract(transferResource.getAmount()));
            walletRepository.save(walletOrigin);

            walletDestiny.setBalance(walletDestiny.getBalance().add(transferResource.getAmount()));
            walletRepository.save(walletDestiny);
            transferResource.setStatus(MovimentStatus.COMPLETED);
        }
        this.walletMessageProducer.updateStatusTransfer(transferResource, errorMessage);
    }

}
