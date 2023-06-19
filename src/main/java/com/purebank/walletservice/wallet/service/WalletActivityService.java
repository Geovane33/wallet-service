package com.purebank.walletservice.wallet.service;

import com.purebank.walletservice.wallet.domain.WalletActivity;
import com.purebank.walletservice.wallet.resource.WalletActivityResource;

import java.util.List;
import java.util.Optional;

public interface WalletActivityService {
    void createOrUpdate(WalletActivityResource walletActivityResource);

    List<WalletActivityResource> activities(Long walletId);
}
