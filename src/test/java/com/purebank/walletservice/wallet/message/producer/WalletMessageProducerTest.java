package com.purebank.walletservice.wallet.message.producer;

import com.purebank.walletservice.wallet.resource.WalletActivityResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;

public class WalletMessageProducerTest {
    @Mock
    private RabbitTemplate rabbitTemplate;

    @Captor
    private ArgumentCaptor<Object> messageCaptor;

    @InjectMocks
    private WalletMessageProducer walletMessageProducer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Teste do producer wallet-activity")
    void sendWalletActivityTest() {
        WalletActivityResource walletActivityResource = new WalletActivityResource();
        walletActivityResource.setWalletId(1L);
        walletActivityResource.setAmount(BigDecimal.valueOf(50));

        walletMessageProducer.sendWalletActivity(walletActivityResource);

        Mockito.verify(rabbitTemplate).convertAndSend(Mockito.eq("direct-exchange-default"),
                Mockito.eq("queue-wallet-activity-key"), messageCaptor.capture());

        Object capturedMessage = messageCaptor.getValue();
        Assertions.assertEquals(walletActivityResource, capturedMessage);
    }
}
