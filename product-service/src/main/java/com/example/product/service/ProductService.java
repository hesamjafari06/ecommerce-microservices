package com.example.product.service;

import com.example.product.dto.request.CreateProductRequest;
import com.example.product.dto.request.UpdateProductRequest;
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

import java.util.List;

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

    @Transactional
    public ProductResponse updateProduct(UpdateProductRequest request, String ownerPublicId) {
        return updateProduct(request, ownerPublicId, request.getPublicId());
    }

    @Transactional
    public ProductResponse updateProduct(UpdateProductRequest request, String ownerPublicId, String publicId) {
        String targetPublicId = (publicId != null && !publicId.isBlank()) ? publicId : request.getPublicId();
        if (targetPublicId == null || targetPublicId.isBlank()) {
            throw new IllegalArgumentException("product publicId is required");
        }

        Product product = productRepository.findByPublicId(targetPublicId)
                .orElseThrow(() -> new RuntimeException("product not found"));

        UserShopCheckRequest checkRequest = new UserShopCheckRequest(ownerPublicId, request.getShopAddress());

        boolean isOwned = userServiceRestClient
                .post()
                .uri("/api/shop/check")
                .contentType(MediaType.APPLICATION_JSON)
                .body(checkRequest)
                .retrieve()
                .body(Boolean.class);
        if (!isOwned) {
            throw new IllegalArgumentException("this shop is now owned by you");
        }

        String newPublicId = request.getNewPublicId();

        if (productRepository.existsByPublicId(newPublicId)) {
            throw new IllegalArgumentException("this publicId is occupied");
        }
        product.setPublicId(newPublicId);


        if (request.getNewName() != null && !request.getNewName().isBlank()) {
            product.setName(request.getNewName());
        }

        if (request.getNewDescription() != null) {
            product.setDescription(request.getNewDescription());
        }

        if (request.getNewPrice() != null) {
            product.setPrice(request.getNewPrice());
        }

        if (request.getNewQuantity() != null) {
            product.setQuantity(request.getNewQuantity());
        }

        productRepository.save(product);
        return productMapper.ModelToResponse(product);
    }

    @Transactional
    public void deleteProduct(String ownerPublicId, String publicId) {
        Product product = productRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("product not found"));

        UserShopCheckRequest checkRequest = new UserShopCheckRequest(ownerPublicId, product.getShopAddress());

        boolean isOwned = userServiceRestClient
                .post()
                .uri("/api/shop/check")
                .contentType(MediaType.APPLICATION_JSON)
                .body(checkRequest)
                .retrieve()
                .body(Boolean.class);

        if (!isOwned) {
            throw new IllegalArgumentException("this shop is now owned by you");
        }

        productRepository.delete(product);
    }

    public List<ProductResponse> getShopProducts(String shopAddress) {
        return productRepository.findByShopAddress(shopAddress)
                .stream()
                .map(productMapper::ModelToResponse)
                .toList();
    }
}
