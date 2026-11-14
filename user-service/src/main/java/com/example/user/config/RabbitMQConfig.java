package com.example.user.config;

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

    public static final String Shop_Exchange = "shop.exchange";
    public static final String Shop_Delete_Queue = "shop.delete.products.queue";
    public static final String Shop_Delete_Routing_Key = "shop.deleted";

    @Bean
    public DirectExchange shopExchange() {
        return new DirectExchange(Shop_Exchange);
    }

    @Bean
    public Queue shopDeleteQueue() {
        return new Queue(Shop_Delete_Queue, true);
    }

    @Bean
    public Binding shopDeleteBinding(Queue shopDeleteQueue, DirectExchange shopExchange) {
        return BindingBuilder.bind(shopDeleteQueue).to(shopExchange).with(Shop_Delete_Routing_Key);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter("*");
    }
}
