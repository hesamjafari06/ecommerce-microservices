package com.example.product.repository;

import com.example.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByPublicId(String publicId);
    boolean existsByPublicId(String publicId);
    List<Product> findByShopAddress(String shopAddress);
    void deleteByPublicId(String publicId);
    void deleteByShopAddress(String shopAddress);
}
