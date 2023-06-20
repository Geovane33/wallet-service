package com.purebank.walletservice.wallet.message.producer;

import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WalletMessageProducer {

    public static final String QUEUE_WALLET_ACTIVITY_KEY = "queue-wallet-activity-key";
    private static final String DIRECT_EXCHANGE_DEFAULT = "direct-exchange-default";
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendWalletActivity(WalletActivityResource walletActivityResource){
        rabbitTemplate.convertAndSend(DIRECT_EXCHANGE_DEFAULT, QUEUE_WALLET_ACTIVITY_KEY, walletActivityResource);
    }
}
