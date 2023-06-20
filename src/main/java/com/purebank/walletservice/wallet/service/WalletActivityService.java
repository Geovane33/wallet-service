package com.purebank.walletservice.wallet.service;

import com.purebank.walletservice.wallet.resource.WalletActivityResource;

import java.util.List;

public interface WalletActivityService {
    void createOrUpdate(WalletActivityResource walletActivityResource);

    List<WalletActivityResource> activity(Long walletId);
}
