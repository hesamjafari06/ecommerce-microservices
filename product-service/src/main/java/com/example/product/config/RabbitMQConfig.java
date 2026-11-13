package com.example.product.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SHOP_EXCHANGE = "shop.exchange";
    public static final String SHOP_DELETE_QUEUE = "shop.delete.products.queue";
    public static final String SHOP_DELETE_ROUTING_KEY = "shop.deleted";

    @Bean
    public DirectExchange shopExchange() {
        return new DirectExchange(SHOP_EXCHANGE);
    }

    @Bean
    public Queue shopDeleteQueue() {
        return new Queue(SHOP_DELETE_QUEUE, true);
    }

    @Bean
    public Binding shopDeleteBinding(Queue shopDeleteQueue, DirectExchange shopExchange) {
        return BindingBuilder.bind(shopDeleteQueue).to(shopExchange).with(SHOP_DELETE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter("*");
    }
}
