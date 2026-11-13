package com.example.user.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteShopProductsEvent {
    private String userPublicId;
    private String shopAddress;
}
