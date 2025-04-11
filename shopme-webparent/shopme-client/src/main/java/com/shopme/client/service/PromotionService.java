package com.shopme.client.service;


import com.shopme.client.dto.response.*;
import com.shopme.common.entity.PromotionProduct;
import com.shopme.common.entity.PromotionType;

import java.util.List;
import java.util.Map;

public interface PromotionService {
    PromotionDetailResponse getCurrentPromotionActiveById(Integer id);
    List<PromotionIdWithTypeResponse> getPromotionTypeActive();

    ListResponse<ProductListResponse> getProductsByPromotionId(Integer id, Map<String, String> params);
}
