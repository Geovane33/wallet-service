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
import java.util.stream.Collectors;

@Slf4j
@Service
public class WalletActivityServiceImpl implements WalletActivityService {

    @Autowired
    WalletActivityRepository walletActivityRepository;

    public void createOrUpdate(WalletActivityResource walletActivityResource) {
        try {
            WalletActivity walletActivity = new WalletActivity();
            if (StringUtils.isNotBlank(walletActivityResource.getUuidActivity())) {
                walletActivity = walletActivityRepository
                        .findByUuidActivity(walletActivityResource.getUuidActivity())
                        .orElse(new WalletActivity());
            }

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
            walletActivity.setDescription(walletActivityResource.getDescription());
            walletActivityRepository.save(walletActivity);

        } catch (RuntimeException ex) {
            log.error("Erro ao salvar movimentação da conta: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<WalletActivityResource> activity(Long walletId) {
        List<WalletActivity> walletActivity = walletActivityRepository
                .findByWalletIdOrderByCreationDateDesc(walletId)
                .orElse(Collections.emptyList());

        return walletActivity.stream()
                .map(a -> {
                    WalletActivityResource resource = new WalletActivityResource();
                    resource.setUuidActivity(a.getUuidActivity());
                    resource.setWalletId(a.getWalletId());
                    resource.setActivityType(a.getActivityType());
                    resource.setStatus(a.getStatus());
                    resource.setAmount(a.getAmount());
                    resource.setActivityDate(a.getActivityDate());
                    resource.setDescription(a.getDescription());
                    return resource;
                }).collect(Collectors.toList());
    }

}
