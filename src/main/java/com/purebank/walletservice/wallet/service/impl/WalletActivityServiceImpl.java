package com.purebank.walletservice.wallet.service.impl;

import com.purebank.walletservice.wallet.domain.WalletActivity;
import com.purebank.walletservice.wallet.enums.ProcessStatus;
import com.purebank.walletservice.wallet.repository.WalletActivityRepository;
import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import com.purebank.walletservice.wallet.service.WalletActivityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class WalletActivityServiceImpl implements WalletActivityService {

    @Autowired
    WalletActivityRepository walletActivityRepository;

    public void createOrUpdate(WalletActivityResource walletActivityResource) {
        try {
            WalletActivity walletActivity = walletActivityRepository
                    .findByUuidActivity(walletActivityResource.getUuidActivity())
                    .orElse(new WalletActivity());

            walletActivity.setWalletId(walletActivityResource.getWalletId());
            walletActivity.setStatus(walletActivityResource.getStatus());
            walletActivity.setUuidActivity(walletActivityResource.getUuidActivity());
            if (ProcessStatus.COMPLETED.equals(walletActivityResource.getStatus())) {
                walletActivity.setUuidActivity(StringUtils.EMPTY);
            }
            walletActivity.setAmount(walletActivityResource.getAmount());
            walletActivity.setActivityType(walletActivityResource.getActivityType());
            walletActivity.setActivityDate(walletActivityResource.getActivityDate());
            walletActivity.setCreationDate(walletActivityResource.getCreationDate());
            walletActivity.setLastUpdate(LocalDateTime.now());
            walletActivityRepository.save(walletActivity);

        } catch (RuntimeException ex) {
            log.error("Erro ao salvar movimentação da conta: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<WalletActivityResource> activities(Long walletId) {
        List<WalletActivity> walletActivities = walletActivityRepository
                .findByWalletIdOrderByCreationDateAsc(walletId)
                .orElse(Collections.emptyList());

        return walletActivities.stream()
                .map(a -> {
                    WalletActivityResource resource = new WalletActivityResource();
                    resource.setUuidActivity(a.getUuidActivity());
                    resource.setWalletId(a.getWalletId());
                    resource.setActivityType(a.getActivityType());
                    resource.setStatus(a.getStatus());
                    resource.setAmount(a.getAmount());
                    return resource;
                }).toList();
    }

}
