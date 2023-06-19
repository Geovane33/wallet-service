package com.purebank.walletservice.wallet.message.producer;

import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WalletMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendWalletActivity(WalletActivityResource walletActivityResource){
        rabbitTemplate.convertAndSend("direct-exchange-default", "queue-wallet-activity-key", walletActivityResource);
    }
}
