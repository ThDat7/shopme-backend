package com.shopme.client.service;


import com.shopme.client.dto.response.PromotionDetailResponse;
import com.shopme.client.dto.response.PromotionIdWithTypeResponse;

import java.util.List;

public interface PromotionService {
    PromotionDetailResponse getCurrentPromotionActiveById(Integer id);
    List<PromotionIdWithTypeResponse> getPromotionTypeActive();
}
