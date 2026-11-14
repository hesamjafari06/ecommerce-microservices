package com.example.order.service;

import com.example.order.dto.request.AddItemRequest;
import com.example.order.dto.response.CartItemResponse;
import com.example.order.dto.response.CartResponse;
import com.example.order.mapper.CartMapper;
import com.example.order.model.Cart;
import com.example.order.model.CartItem;
import com.example.order.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Transactional
    public CartResponse createCart(String ownerPublicId) {
        if (ownerPublicId == null || ownerPublicId.isBlank()) {
            throw new IllegalArgumentException("ownerPublicId is required");
        }

        return cartRepository.findByOwnerPublicId(ownerPublicId)
                .map(cartMapper::cartToResponse)
                .orElseGet(() -> {
                    Cart cart = Cart.builder()
                            .publicId(UUID.randomUUID().toString())
                            .ownerPublicId(ownerPublicId)
                            .cartItemList(new ArrayList<>())
                            .totalPrice(BigDecimal.ZERO)
                            .build();
                    cartRepository.save(cart);
                    log.info("Created cart {} for user {}", cart.getPublicId(), ownerPublicId);
                    return cartMapper.cartToResponse(cart);
                });
    }

    @Transactional
    public CartItemResponse addItem(String ownerPublicId, AddItemRequest request) {
        if (ownerPublicId == null || ownerPublicId.isBlank()) {
            throw new IllegalArgumentException("User-Public-Id header is required");
        }

        String productPublicId = request.getPublicId();
        if (productPublicId == null || productPublicId.isBlank()) {
            throw new IllegalArgumentException("product publicId is required");
        }

        Cart cart = cartRepository.findByOwnerPublicId(ownerPublicId)
                .orElseThrow(() -> new IllegalArgumentException("User-Public-Id header is required"));

        if (cart.getCartItemList() == null) {
            cart.setCartItemList(new ArrayList<>());
        }

        int amountToAdd = request.getProductAmount();
        BigDecimal price = request.getPrice();

        CartItem existingItem = cart.getCartItemList().stream()
                .filter(item -> productPublicId.equals(item.getProductPublicId()))
                .findFirst()
                .orElse(null);

        CartItem targetItem;
        if (existingItem != null) {
            existingItem.setProductAmount(existingItem.getProductAmount() + amountToAdd);
            if (price.compareTo(BigDecimal.ZERO) > 0) {
                existingItem.setProductPrice(price);
            }
            if (request.getShopAddress() != null && !request.getShopAddress().isBlank()) {
                existingItem.setShopAddress(request.getShopAddress());
            }
            targetItem = existingItem;
        } else {
            targetItem = CartItem.builder()
                    .cart(cart)
                    .cartItemPublicId(UUID.randomUUID().toString())
                    .productPublicId(productPublicId)
                    .productName(request.getName())
                    .productPrice(price)
                    .productAmount(amountToAdd)
                    .shopAddress(request.getShopAddress())
                    .build();
            cart.getCartItemList().add(targetItem);
        }

        recalculateTotalPrice(cart);
        cartRepository.save(cart);

        return cartMapper.cartItemToResponse(targetItem);
    }

    @Transactional
    public void removeItem(String ownerPublicId, String cartItemPublicId) {
        if (ownerPublicId == null || ownerPublicId.isBlank()) {
            throw new IllegalArgumentException("User-Public-Id header is required");
        }

        if (cartItemPublicId == null || cartItemPublicId.isBlank()) {
            throw new IllegalArgumentException("cartItemPublicId is required");
        }

        Cart cart = cartRepository.findByOwnerPublicId(ownerPublicId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + ownerPublicId));

        if (cart.getCartItemList() == null || cart.getCartItemList().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        CartItem itemToRemove = cart.getCartItemList().stream()
                .filter(item -> cartItemPublicId.equals(item.getCartItemPublicId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found: " + cartItemPublicId));

        cart.getCartItemList().remove(itemToRemove);
        recalculateTotalPrice(cart);
        cartRepository.save(cart);
    }

    public List<CartItemResponse> getCartItems(String ownerPublicId) {
        if (ownerPublicId == null || ownerPublicId.isBlank()) {
            throw new IllegalArgumentException("User-Public-Id header is required");
        }

        Cart cart = cartRepository.findByOwnerPublicId(ownerPublicId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + ownerPublicId));

        if (cart.getCartItemList() == null) {
            return List.of();
        }

        return cart.getCartItemList().stream()
                .map(cartMapper::cartItemToResponse)
                .toList();
    }

    public CartResponse getCart(String ownerPublicId) {
        if (ownerPublicId == null || ownerPublicId.isBlank()) {
            throw new IllegalArgumentException("User-Public-Id header is required");
        }

        Cart cart = cartRepository.findByOwnerPublicId(ownerPublicId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + ownerPublicId));

        return cartMapper.cartToResponse(cart);
    }

    private void recalculateTotalPrice(Cart cart) {
        if (cart.getCartItemList() == null || cart.getCartItemList().isEmpty()) {
            cart.setTotalPrice(BigDecimal.ZERO);
            return;
        }

        BigDecimal total = cart.getCartItemList().stream()
                .map(item -> {
                    BigDecimal price = item.getProductPrice() != null ? item.getProductPrice() : BigDecimal.ZERO;
                    return price.multiply(BigDecimal.valueOf(item.getProductAmount()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(total);
    }
}
