package com.example.order.mapper;

import com.example.order.dto.response.CartItemResponse;
import com.example.order.dto.response.CartResponse;
import com.example.order.model.Cart;
import com.example.order.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CartMapper {

    public CartItemResponse cartItemToResponse(CartItem item) {
        return CartItemResponse.builder()
                .cartItemPublicId(item.getCartItemPublicId())
                .productPublicId(item.getProductPublicId())
                .productName(item.getProductName())
                .productPrice(item.getProductPrice())
                .productAmount(item.getProductAmount())
                .shopAddress(item.getShopAddress())
                .build();
    }

    public CartResponse cartToResponse(Cart cart) {
        List<CartItemResponse> items =
                cart.getCartItemList().stream().map(this::cartItemToResponse).toList();

        return CartResponse.builder()
                .publicId(cart.getPublicId())
                .ownerPublicId(cart.getOwnerPublicId())
                .cartItemList(items)
                .totalPrice(cart.getTotalPrice())
                .build();
    }
}
