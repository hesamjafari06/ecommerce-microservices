package com.example.order.controller;

import com.example.order.dto.request.AddItemRequest;
import com.example.order.dto.response.CartItemResponse;
import com.example.order.dto.response.CartResponse;
import com.example.order.model.Order;
import com.example.order.service.CartService;
import com.example.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;

    @PostMapping("/cart")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse createCart(
            @RequestHeader(value = "User-Public-Id") String ownerPublicId
    ) {
        return cartService.createCart(ownerPublicId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartItemResponse addItem(
            @RequestHeader("User-Public-Id") String ownerPublicId,
            @RequestBody AddItemRequest request
    ) {
        return cartService.addItem(ownerPublicId, request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void removeItem(
            @RequestHeader("User-Public-Id") String ownerPublicId,
            @RequestBody String itemPublicId
    ) {
        cartService.removeItem(ownerPublicId, itemPublicId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CartItemResponse> getCartItems(
            @RequestHeader("User-Public-Id") String ownerPublicId
    ) {
        return cartService.getCartItems(ownerPublicId);
    }

    @GetMapping("/cart")
    @ResponseStatus(HttpStatus.OK)
    public CartResponse getCart(
            @RequestHeader("User-Public-Id") String ownerPublicId
    ) {
        return cartService.getCart(ownerPublicId);
    }

    @PostMapping({ "/place-order"})
    @ResponseStatus(HttpStatus.CREATED)
    public Order placeOrder(
            @RequestHeader("User-Public-Id") String ownerPublicId
    ) {
        return orderService.placeOrder(ownerPublicId);
    }

    @GetMapping("/place-order")
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getUserOrders(
            @RequestHeader("User-Public-Id") String ownerPublicId
    ) {
        return orderService.getUserOrders(ownerPublicId);
    }
}
