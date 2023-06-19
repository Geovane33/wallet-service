package com.purebank.walletservice.wallet.service.impl;

import com.purebank.walletservice.wallet.domain.WalletActivity;
import com.purebank.walletservice.wallet.repository.WalletActivityRepository;
import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import com.purebank.walletservice.wallet.service.WalletActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class WalletActivityServiceImpl implements WalletActivityService {

    @Autowired
    WalletActivityRepository walletActivityRepository;

    public void createOrUpdate(WalletActivityResource walletActivityResource) {
        try {
            WalletActivity walletActivity = walletActivityRepository
                    .findByMovementIdentifier(walletActivityResource.getMovementIdentifier())
                    .orElse(new WalletActivity());

            walletActivity.setStatus(walletActivityResource.getStatus());
            walletActivity.setMovementIdentifier(walletActivityResource.getMovementIdentifier());
            walletActivity.setAmount(walletActivityResource.getAmount());
            walletActivity.setActivityType(walletActivityResource.getActivityType());
            walletActivity.setActivityDate(walletActivityResource.getActivityDate());
            walletActivity.setCreationDate(walletActivityResource.getCreationDate());
            walletActivity.setLastUpdate(LocalDateTime.now());
            walletActivityRepository.save(walletActivity);

        } catch (Exception ex) {
            log.error("Erro ao salvar movimentação da conta: {}", ex.getMessage());
            throw ex;
        }
    }

}
