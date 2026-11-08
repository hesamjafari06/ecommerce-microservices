package com.example.product.controller;

import com.example.product.dto.request.CreateProductRequest;
import com.example.product.dto.response.ProductResponse;
import com.example.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse addProduct(
            @RequestBody CreateProductRequest request,
            @RequestHeader("User-Public-Id") String ownerPublicId
    ) {
        return productService.addProduct(request, ownerPublicId);
    }

}
