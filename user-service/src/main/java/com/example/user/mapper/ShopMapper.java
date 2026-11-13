package com.example.user.mapper;

import com.example.user.dto.ProductDto;
import com.example.user.dto.request.CreateShopRequest;
import com.example.user.dto.response.ProductResponse;
import com.example.user.dto.response.ShopInfoResponse;
import com.example.user.dto.response.ShopResponse;
import com.example.user.model.ShopEntity;
import com.example.user.model.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShopMapper {

    public ShopEntity createToEntity(CreateShopRequest request, UserEntity owner) {
        return ShopEntity.builder()
                .name(request.getName())
                .shopAddress(request.getShopAddress())
                .owner(owner)
                .build();
    }

    public ShopResponse entityToResponse(ShopEntity shop) {
        return ShopResponse.builder()
                .ownerPublicId(shop.getOwner().getPublicId())
                .shopAddress(shop.getShopAddress())
                .name(shop.getName())
                .build();
    }

    public ShopInfoResponse entityToInfoResponse(ShopEntity shop, List<ProductDto> productList) {
        return ShopInfoResponse.builder()
                .ownerPublicId(shop.getOwner().getPublicId())
                .shopAddress(shop.getShopAddress())
                .name(shop.getName())
                .productList(productList)
                .build();
    }

    public ProductDto productResponseToDto(ProductResponse response) {
        return new ProductDto(
                response.getName(),
                response.getDescription(),
                response.getPrice(),
                response.getQuantity()
        );
    }
}
