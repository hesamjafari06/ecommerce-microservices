package com.example.order.service;

import com.example.order.model.Cart;
import com.example.order.model.Order;
import com.example.order.model.OrderItem;
import com.example.order.repository.CartRepository;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Order placeOrder(String ownerPublicId) {
        if (ownerPublicId == null || ownerPublicId.isBlank()) {
            throw new IllegalArgumentException("User-Public-Id header is required");
        }

        Cart cart = cartRepository.findByOwnerPublicId(ownerPublicId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + ownerPublicId));

        if (cart.getCartItemList() == null || cart.getCartItemList().isEmpty()) {
            throw new IllegalStateException("Cannot place order with an empty cart");
        }

        List<OrderItem> orderItems = cart.getCartItemList().stream()
                .map(item -> OrderItem.builder()
                        .cartItemPublicId(item.getCartItemPublicId())
                        .productPublicId(item.getProductPublicId())
                        .productName(item.getProductName())
                        .productPrice(item.getProductPrice())
                        .productAmount(item.getProductAmount())
                        .shopAddress(item.getShopAddress())
                        .build())
                .toList();

        Order order = Order.builder()
                .publicId(UUID.randomUUID().toString())
                .ownerPublicId(ownerPublicId)
                .orderItemList(orderItems)
                .totalPrice(cart.getTotalPrice())
                .timestamp(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        cart.getCartItemList().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        return savedOrder;
    }

    public List<Order> getUserOrders(String ownerPublicId) {
        if (ownerPublicId == null || ownerPublicId.isBlank()) {
            throw new IllegalArgumentException("User-Public-Id header is required");
        }
        return orderRepository.findByOwnerPublicId(ownerPublicId);
    }
}
