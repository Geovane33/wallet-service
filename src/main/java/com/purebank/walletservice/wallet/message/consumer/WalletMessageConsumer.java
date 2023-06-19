package com.purebank.walletservice.wallet.message.consumer;

import com.purebank.walletservice.wallet.resource.TransferResource;
import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import com.purebank.walletservice.wallet.service.WalletActivityService;
import com.purebank.walletservice.wallet.service.impl.WalletServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WalletMessageConsumer {

    @Autowired
    private WalletServiceImpl walletServiceImpl;

    @Autowired
    WalletActivityService walletActivityService;

    @RabbitListener(queues = "update-accounts-balance")
    public void updateAccountsBalance(TransferResource transferResource) {
        walletServiceImpl.updateAccountsBalance(transferResource);
    }

    @RabbitListener(queues = "wallet-activity")
    public void walletActivity(WalletActivityResource walletActivityResource) {
        walletActivityService.createOrUpdate(walletActivityResource);
    }
}
