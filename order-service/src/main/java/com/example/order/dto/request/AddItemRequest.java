package com.example.order.dto.request;

import com.example.order.dto.response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddItemRequest {
    private String shopAddress;
    private String publicId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private Integer productAmount;
}
