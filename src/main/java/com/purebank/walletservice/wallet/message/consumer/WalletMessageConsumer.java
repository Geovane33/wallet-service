package com.purebank.walletservice.wallet.message.consumer;

import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import com.purebank.walletservice.wallet.service.WalletActivityService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WalletMessageConsumer {
    public static final String WALLET_ACTIVITY = "wallet-activity";
    @Autowired
    WalletActivityService walletActivityService;

    @RabbitListener(queues = WALLET_ACTIVITY)
    public void walletTimeline(WalletActivityResource walletActivityResource) {
        walletActivityService.createOrUpdate(walletActivityResource);
    }
}
