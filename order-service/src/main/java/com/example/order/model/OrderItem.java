package com.example.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private String cartItemPublicId;
    private String productPublicId;
    private String productName;
    private BigDecimal productPrice;
    private int productAmount;
    private String shopAddress;
}
