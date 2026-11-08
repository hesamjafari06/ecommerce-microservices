package com.example.product.mapper;

import com.example.product.dto.request.CreateProductRequest;
import com.example.product.dto.response.ProductResponse;
import com.example.product.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product createToModel(CreateProductRequest request, String ownerPublicId) {
        return Product.builder()
                .publicId(request.getPublicId())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .ownerPublicId(ownerPublicId)
                .shopAddress(request.getShopAddress())
                .quantity(request.getQuantity())
                .build();
    }

    public ProductResponse ModelToResponse(Product product) {
        return ProductResponse.builder()
                .publicId(product.getPublicId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

}
