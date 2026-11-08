package com.example.product.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

@Document(collection = "product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Product {
    @Id
    private String id;
    private String publicId;
    private String shopAddress;
    private String ownerPublicId;
    private String name;
    private String description;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal price;
    private int quantity;
}
