package com.example.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoveItemRequest {
    private String cartItemPublicId;
    private String publicId;

    public String getEffectiveCartItemPublicId() {
        if (cartItemPublicId != null && !cartItemPublicId.isBlank()) {
            return cartItemPublicId;
        }
        return publicId;
    }
}
