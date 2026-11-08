package com.example.product.dto.request;

public record UserShopCheckRequest(
        String ownerPublicId,
        String shopAddress
) {}
