package com.example.user.controller;

import com.example.user.dto.request.CreateShopRequest;
import com.example.user.dto.request.UpdateShopRequest;
import com.example.user.dto.response.ShopResponse;
import com.example.user.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShopResponse createShop(
            @RequestBody CreateShopRequest request,
            @RequestHeader("User-Public-Id") String publicId
    ) {
        return shopService.createShop(request, publicId);
    }


    @PutMapping("/{address}")
    @ResponseStatus(HttpStatus.OK)
    public ShopResponse updateShop(
            @PathVariable String address,
            @RequestBody UpdateShopRequest request,
            @RequestHeader("User-Public-Id") String publicId
    ) {
        return shopService.updateShop(request, publicId, address);
    }

    @DeleteMapping("/{address}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteShop(
            @PathVariable String address,
            @RequestHeader("User-Public-Id") String publicId
    ) {
        shopService.deleteShop(publicId, address);
    }

    @GetMapping("/{address}")
    @ResponseStatus(HttpStatus.OK)
    public ShopResponse showInfoShop(@PathVariable String address) {
        return shopService.showShopInfo(address);
    }
}
