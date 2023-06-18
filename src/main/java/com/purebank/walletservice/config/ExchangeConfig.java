package com.purebank.walletservice.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeConfig {

    @Autowired
    private Queue queueTransfer;

    @Autowired
    private Queue fila1;

//    @Autowired
//    private Queue queueWalletActivity;


    @Bean
    public Queue queueFila1(){
        return new Queue("fila1");
    }


    @Bean
    public Exchange directExchange() {
        return ExchangeBuilder
                .directExchange("direct-exchange-default")
                .build();
    }

    @Bean
    public Binding bindingFila1() {
        return BindingBuilder
                .bind(queueFila1())
                .to(directExchange())
                .with("queue-fila1-key")
                .noargs();
    }

    @Bean
    public Binding bindingQueueTransfer() {
        return BindingBuilder
                .bind(queueTransfer)
                .to(directExchange())
                .with("queue-update-status-transfer-key")
                .noargs();
    }
}