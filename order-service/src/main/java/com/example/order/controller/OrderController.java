package com.example.order.controller;

import com.example.order.dto.request.PlaceOrderRequest;
import com.example.order.sevice.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody PlaceOrderRequest request) {
        orderService.placeOrder(request);
        return "order placed successfully";
    }

}
