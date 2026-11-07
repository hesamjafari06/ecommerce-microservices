package com.example.user.dto.response;

import com.example.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopResponse {
    private String name;
    private String shopAddress;
    private String ownerPublicId;
}
