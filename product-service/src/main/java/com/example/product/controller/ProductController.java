package com.example.product.controller;

import com.example.product.dto.request.CreateProductRequest;
import com.example.product.dto.request.UpdateProductRequest;
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

    @PutMapping("/{publicId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProduct(
            @PathVariable String publicId,
            @RequestBody UpdateProductRequest request,
            @RequestHeader("User-Public-Id") String ownerPublicId
    ) {
        return productService.updateProduct(request, ownerPublicId, publicId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProduct(
            @RequestBody UpdateProductRequest request,
            @RequestHeader("User-Public-Id") String ownerPublicId
    ) {
        return productService.updateProduct(request, ownerPublicId);
    }

    @DeleteMapping("/{publicId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(
            @PathVariable String publicId,
            @RequestHeader("User-Public-Id") String ownerPublicId
    ) {
        productService.deleteProduct(ownerPublicId, publicId);
    }

}
