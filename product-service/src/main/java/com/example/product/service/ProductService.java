package com.example.product.service;

import com.example.product.dto.request.CreateProductRequest;
import com.example.product.dto.request.UserShopCheckRequest;
import com.example.product.dto.response.ProductResponse;
import com.example.product.mapper.ProductMapper;
import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final RestClient userServiceRestClient;

    @Transactional
    public ProductResponse addProduct(CreateProductRequest request, String ownerPublicId) {

        UserShopCheckRequest checkRequest = new UserShopCheckRequest(ownerPublicId, request.getShopAddress());

        boolean isOwned = userServiceRestClient
                .post()
                .uri("/api/shop/check")
                .contentType(MediaType.APPLICATION_JSON)
                .body(checkRequest)
                .retrieve()
                .body(Boolean.class);
        if (isOwned) {
            Product newProduct = productMapper.createToModel(request, ownerPublicId);
            productRepository.save(newProduct);
            return productMapper.ModelToResponse(newProduct);
        }
        throw new IllegalArgumentException("this shop is now owned by you");
    }
}
