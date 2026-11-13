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
    private String name;
    private String newDescription;
    private String description;
    private BigDecimal newPrice;
    private BigDecimal price;
    private Integer newQuantity;
    private Integer quantity;

    public String getNewPublicId() {
        return newPublicId != null ? newPublicId : publicId;
    }

    public String getNewName() {
        return newName != null ? newName : name;
    }

    public String getNewDescription() {
        return newDescription != null ? newDescription : description;
    }

    public BigDecimal getNewPrice() {
        return newPrice != null ? newPrice : price;
    }

    public Integer getNewQuantity() {
        return newQuantity != null ? newQuantity : quantity;
    }
}
