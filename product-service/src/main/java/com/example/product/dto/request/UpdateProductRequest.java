package com.example.product.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {
    private String shopAddress;
    private String newPublicId;
    private String publicId;
    private String newName;
    private String newDescription;
    private BigDecimal newPrice;
    private Integer newQuantity;
}
