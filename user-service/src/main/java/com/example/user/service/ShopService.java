package com.example.user.service;

import com.example.user.dto.ProductDto;
import com.example.user.dto.request.CreateShopRequest;
import com.example.user.dto.request.UpdateShopRequest;
import com.example.user.dto.request.UserShopCheckRequest;
import com.example.user.dto.response.ProductResponse;
import com.example.user.dto.response.ShopInfoResponse;
import com.example.user.dto.response.ShopResponse;
import com.example.user.mapper.ShopMapper;
import com.example.user.model.ShopEntity;
import com.example.user.model.UserEntity;
import com.example.user.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.example.user.config.RabbitMQConfig;
import com.example.user.dto.event.DeleteShopProductsEvent;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final ShopMapper shopMapper;
    private final UserService userService;
    private final RestClient productServiceRestClient;
    private final RabbitTemplate rabbitTemplate;

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


    public void deleteShop(String publicId, String shopAddress) {
        UserEntity owner = userService.findUser(null, null, publicId);

        ShopEntity shop = shopRepository.findByShopAddress(shopAddress)
                .orElseThrow(() -> new RuntimeException("shop not found"));

        if (!shop.getOwner().equals(owner)) {
            throw new IllegalArgumentException("this shop is now owned by you");
        }

        shopRepository.delete(shop);

        DeleteShopProductsEvent event = new DeleteShopProductsEvent(publicId, shopAddress);
        rabbitTemplate.convertAndSend(RabbitMQConfig.SHOP_EXCHANGE, RabbitMQConfig.SHOP_DELETE_ROUTING_KEY, event);
    }

    public ShopInfoResponse showShopInfo(String address) {
        ShopEntity shop = shopRepository.findByShopAddress(address)
                .orElseThrow(() -> new RuntimeException("shop not found"));

        List<ProductResponse> productResponses = productServiceRestClient
                .get()
                .uri("/api/product/shop/{shopAddress}", address)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        List<ProductDto> productList = productResponses == null ? List.of() : productResponses.stream()
                .map(shopMapper::productResponseToDto)
                .toList();

        return shopMapper.entityToInfoResponse(shop, productList);
    }

    public boolean checkOwner(UserShopCheckRequest request) {
        return shopRepository.findByShopAddress(request.shopAddress())
                .orElseThrow(() -> new RuntimeException("shop not found"))
                .getOwner().getPublicId().equals(request.ownerPublicId());
    }
}
