package com.example.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;
    @Column(nullable = false, unique = true)
    private String cartItemPublicId;
    @Column(nullable = false)
    private String productPublicId;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private BigDecimal productPrice;
    @Column(nullable = false)
    private int productAmount;
    private String shopAddress;
}
