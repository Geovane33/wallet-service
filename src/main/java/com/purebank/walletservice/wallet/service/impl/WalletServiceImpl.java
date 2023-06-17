package com.purebank.walletservice.wallet.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.purebank.walletservice.wallet.api.resource.WalletResource;
import com.purebank.walletservice.wallet.domain.Wallet;
import com.purebank.walletservice.wallet.exceptions.Exception;
import com.purebank.walletservice.wallet.repository.WalletRepository;
import com.purebank.walletservice.wallet.service.WalletService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
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

    public WalletResource getWalletById(Long walletId) {
        Wallet wallet = walletRepository.findWalletById(walletId);
        WalletResource walletResource = new WalletResource();
        if (wallet == null) {
            throw new Exception("Carteira não encontrada, informe o id correto", HttpStatus.NOT_FOUND);
        }
        walletResource.setId(wallet.getId());
        walletResource.setName(wallet.getName());
        walletResource.setBalance(wallet.getBalance());
        return walletResource;
    }

    public BigDecimal getBalance(Long walletId) {
        return walletRepository.getBalanceByWalletId(walletId);
    }

    public WalletResource updateWallet(WalletResource walletResource) {
        Wallet wallet = new Wallet();
        wallet.setId(walletResource.getId());
        wallet.setBalance(walletResource.getBalance());
        wallet.setName(walletResource.getName());
        walletRepository.save(wallet);
        return walletResource;
    }

    @Override
    public Boolean deposit(Long accountId, BigDecimal amount) {
        Wallet wallet = walletRepository.findWalletById(accountId);
        //todo - Validar valor negativo ou elevado - tratar exceções
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
        return true;
    }

    @Override
    public Boolean withdraw(Long accountId, BigDecimal amount) {
        Wallet wallet = walletRepository.findWalletById(accountId);
        if (amount == null) {
            throw new Exception.NullPointerException("Não foi possivel realizar o saque");
        }
        if (ObjectUtils.anyNotNull(wallet, wallet.getBalance()) && wallet.getBalance().compareTo(amount) >= 0) {
            wallet.setBalance(wallet.getBalance().subtract(amount));
            walletRepository.save(wallet);
            return true;
        } else {
            throw new Exception.NullPointerException("Não foi possivel realizar o saque");
        }
        //todo - Validar saldo e valor - tratar exceções
    }

    @RabbitListener(queues = "transfer")
    public void transfer(String payload) throws JsonProcessingException {
        TransferResource transferResource = objectMapper.readValue(payload, TransferResource.class);
        Wallet walletOrigin = walletRepository.findWalletById(transferResource.getWalletOrigin());
        Wallet walletDestiny = walletRepository.findWalletById(transferResource.getWalletDestiny());

        if (walletOrigin == null) {
            transferResource.setStatusDescription("Falha ao processar a transferência: Conta de origem não existe");
            rabbitTemplate.convertAndSend("direct-exchange-default", "queue-transfer-key", transferResource);
            return;
        }

        if (walletDestiny == null) {
            transferResource.setStatusDescription("Falha ao processar a transferência: Conta de destino não existe");
            rabbitTemplate.convertAndSend("direct-exchange-default", "queue-transfer-key", transferResource);
            return;
        }

        if (walletOrigin.getBalance().compareTo(transferResource.getAmount()) < 0) {
            transferResource.setStatusDescription("Falha ao processar a transferência: saldo insuficiente");
            rabbitTemplate.convertAndSend("direct-exchange-default", "queue-transfer-key", transferResource);
            return;
        }

        BigDecimal updatedBalance = walletDestiny.getBalance().add(transferResource.getAmount());
        walletDestiny.setBalance(updatedBalance);

        rabbitTemplate.convertAndSend("direct-exchange-default", "queue-transfer-key", transferResource);
    }
}
