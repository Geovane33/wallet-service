package com.purebank.walletservice.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = RabbitMQConfig.class)
public class RabbitMQConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    AmqpTemplate amqpTemplate;

    @Test
    @DisplayName("Verifica se a configuração da fila 'wallet-activity' está correta")
    void testQueueWalletActivity() {
        Queue queueWalletActivity = applicationContext.getBean("queueWalletActivity", Queue.class);

        Assertions.assertNotNull(queueWalletActivity);
        Assertions.assertEquals("wallet-activity", queueWalletActivity.getName());
        Assertions.assertTrue(queueWalletActivity.isDurable());
    }

    @Test
    @DisplayName("Verifica se a configuração da exchange 'direct-exchange-default' está correta")
    void testDirectExchange() {
        Exchange directExchange = applicationContext.getBean("directExchange", Exchange.class);

        Assertions.assertNotNull(directExchange);
        Assertions.assertEquals("direct-exchange-default", directExchange.getName());
        Assertions.assertEquals(ExchangeTypes.DIRECT, directExchange.getType());
    }

    @Test
    @DisplayName("Verifica se a configuração do binding da fila 'wallet-activity' está correta")
    void testBindingWalletActivity() {
        Binding bindingWalletActivity = applicationContext.getBean("bindingWalletActivity", Binding.class);

        Assertions.assertNotNull(bindingWalletActivity);
        Assertions.assertEquals("queue-wallet-activity-key", bindingWalletActivity.getRoutingKey());
        Assertions.assertTrue(bindingWalletActivity.isDestinationQueue());
    }
}

