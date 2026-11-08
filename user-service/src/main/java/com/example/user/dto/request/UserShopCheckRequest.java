package com.example.user.dto.request;

public record UserShopCheckRequest(
        String ownerPublicId,
        String shopAddress
) {}