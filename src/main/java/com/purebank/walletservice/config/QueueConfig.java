package com.purebank.walletservice.config;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class QueueConfig {

    @Bean
    @Primary
    public Queue queueUpdateStatusTransfer() {
        return QueueBuilder
                .durable("update-status-transfer")
                .build();
    }

//    @Bean
//    public Queue queueWalletActivity() {
//        return QueueBuilder
//                .durable("wallet-activity")
//                .build();
//    }

    @Bean
    public Queue fila1() {
        return new Queue("fila1");
    }



    @Bean
    public Queue fila2() {
        return new Queue("fila2");
    }
}