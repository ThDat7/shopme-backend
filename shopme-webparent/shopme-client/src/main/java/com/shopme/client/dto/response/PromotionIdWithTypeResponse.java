package com.shopme.client.dto.response;

import com.shopme.common.entity.PromotionType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PromotionIdWithTypeResponse {
    private Integer id;
    private PromotionType type;
}
