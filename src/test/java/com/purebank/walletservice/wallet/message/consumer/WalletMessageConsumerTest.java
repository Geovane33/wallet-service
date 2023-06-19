package com.purebank.walletservice.wallet.message.consumer;


import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import com.purebank.walletservice.wallet.service.WalletActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class WalletMessageConsumerTest {

    @Mock
    private WalletActivityService walletActivityService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private WalletMessageConsumer walletMessageConsumer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Teste do consumer wallet-activity")
    void walletActivityTest() {
        WalletActivityResource walletActivityResource = new WalletActivityResource();
        walletMessageConsumer.walletActivity(walletActivityResource);
        Mockito.verify(walletActivityService, Mockito.times(1)).createOrUpdate(walletActivityResource);
    }
}


