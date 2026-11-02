package com.example.order.dto.request;

import com.example.order.dto.OrderLineItemDto;
import com.example.order.model.OrderLineItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderRequest {
    private List<OrderLineItemDto> orderLineItemDtoList;
}
