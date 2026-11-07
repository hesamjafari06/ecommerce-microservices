package com.example.user.service;

import com.example.user.dto.request.CreateShopRequest;
import com.example.user.dto.request.UpdateShopRequest;
import com.example.user.dto.response.ShopResponse;
import com.example.user.mapper.ShopMapper;
import com.example.user.model.ShopEntity;
import com.example.user.model.UserEntity;
import com.example.user.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final ShopMapper shopMapper;
    private final UserService userService;

    @Transactional
    public ShopResponse createShop(CreateShopRequest request, String publicId) {

        UserEntity owner = userService.findUser(null, null, publicId);

        if (shopRepository.existsByShopAddress(request.getShopAddress())) {
            throw new IllegalArgumentException("this shopAddress is occupied");
        }

        ShopEntity newShop = shopMapper.createToEntity(request, owner);

        shopRepository.save(newShop);

        return shopMapper.entityToResponse(newShop);
    }

    @Transactional
    public ShopResponse updateShop(UpdateShopRequest request, String publicId, String shopAddress) {

        UserEntity owner = userService.findUser(null, null, publicId);

        ShopEntity shop = shopRepository.findByShopAddress(shopAddress)
                .orElseThrow(() -> new RuntimeException("shop not found"));

        if (!shop.getOwner().equals(owner)) {
            throw new IllegalArgumentException("this shop is now owned by you");
        }
        if (request.getShopAddress() != null) {
            if (shopRepository.existsByShopAddress(request.getShopAddress())) {
                throw new IllegalArgumentException("this shopAddress is occupied");
            } else {
                shop.setShopAddress(request.getShopAddress());
            }
        }
        if (request.getName() != null) {
            shop.setName(request.getName());
        }

        return shopMapper.entityToResponse(shop);
    }
}
