package com.example.user.mapper;

import com.example.user.dto.request.CreateShopRequest;
import com.example.user.dto.response.ShopResponse;
import com.example.user.model.ShopEntity;
import com.example.user.model.UserEntity;
import org.springframework.stereotype.Component;

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
}
