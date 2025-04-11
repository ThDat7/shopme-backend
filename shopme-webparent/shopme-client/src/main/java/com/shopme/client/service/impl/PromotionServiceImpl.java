package com.shopme.client.service.impl;

import com.shopme.client.dto.response.*;
import com.shopme.client.exception.type.PromotionNotFoundException;
import com.shopme.client.mapper.PromotionMapper;
import com.shopme.client.repository.PromotionProductRepository;
import com.shopme.client.repository.PromotionRepository;
import com.shopme.client.service.ProductService;
import com.shopme.client.service.PromotionService;
import com.shopme.common.entity.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final ProductService productService;
    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;
    private final PromotionProductRepository promotionProductRepository;

    @Override
    public List<PromotionIdWithTypeResponse> getPromotionTypeActive() {
        List<Promotion> promotions = promotionRepository.findActivePromotions();
        return promotions.stream().map(promotionMapper::toPromotionIdWithTypeResponse).toList();
    }

    @Override
    public PromotionDetailResponse getCurrentPromotionActiveById(Integer id) {
        Promotion promotion = promotionRepository.findActivePromotionById(id)
                .orElseThrow(PromotionNotFoundException::new);

        return promotionMapper.toPromotionDetailResponse(promotion);
    }

    @Override
    public ListResponse<ProductListResponse> getProductsByPromotionId(Integer id, Map<String, String> params) {
        params.put("promotionId", id.toString());

        return productService.listByPage(params);
    }
}
