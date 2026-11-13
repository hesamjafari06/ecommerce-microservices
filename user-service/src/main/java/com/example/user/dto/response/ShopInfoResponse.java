package com.example.user.dto.response;

import com.example.user.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopInfoResponse {
    private String name;
    private String shopAddress;
    private String ownerPublicId;
    private List<ProductDto> productList;
}
