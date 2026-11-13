package com.example.product.listener;

import com.example.product.config.RabbitMQConfig;
import com.example.product.dto.event.DeleteShopProductsEvent;
import com.example.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShopEventListener {

    private final ProductService productService;

    @RabbitListener(queues = RabbitMQConfig.SHOP_DELETE_QUEUE)
    public void handleDeleteShopProducts(DeleteShopProductsEvent event) {
        log.info("Received DeleteShopProductsEvent for shopAddress: {}, userPublicId: {}",
                event.getShopAddress(), event.getUserPublicId());
        productService.deleteShopProducts(event.getUserPublicId(), event.getShopAddress());
    }
}
