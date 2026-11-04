package com.example.order.sevice;

import com.example.order.dto.OrderLineItemDto;
import com.example.order.dto.request.PlaceOrderRequest;
import com.example.order.dto.response.InventoryResponse;
import com.example.order.model.Order;
import com.example.order.model.OrderLineItem;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final RestClient restClient;
    private final OrderRepository orderRepository;

    public void placeOrder(PlaceOrderRequest request) {
        Order order =
                Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .build();

        List<OrderLineItem> orderLineItems = request.getOrderLineItemDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemList(orderLineItems);

        List<String> skuCodes = orderLineItems.stream().map(OrderLineItem::getSkuCode).toList();

        InventoryResponse[] result = restClient
                .get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                        .body(InventoryResponse[].class);

        for (InventoryResponse product : result) {
            if (product.isInStock()) {
                orderRepository.save(order);
            } else {
                throw new IllegalArgumentException("product is not in stock");
            }
        }
    }

    private OrderLineItem mapToDto(OrderLineItemDto dto) {
        return OrderLineItem.builder()
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .skuCode(dto.getSkuCode())
                .build();
    }
}
