package com.purebank.walletservice.wallet.message.consumer;

import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import com.purebank.walletservice.wallet.service.WalletActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WalletMessageConsumer {
    public static final String WALLET_ACTIVITY = "wallet-activity";
    @Autowired
    WalletActivityService walletActivityService;

    @RabbitListener(queues = WALLET_ACTIVITY)
    public void walletActivities(WalletActivityResource walletActivityResource) {
        log.info("Consumindo da fila walletActivityResource");
        walletActivityService.createOrUpdate(walletActivityResource);
    }
}
