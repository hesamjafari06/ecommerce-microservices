package com.example.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private String id;
    private String publicId;
    private String ownerPublicId;
    @Builder.Default
    private List<OrderItem> orderItemList = new ArrayList<>();
    @Builder.Default
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private LocalDateTime timestamp;
}
