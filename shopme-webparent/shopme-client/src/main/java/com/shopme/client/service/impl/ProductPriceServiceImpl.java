package com.shopme.client.service.impl;

import com.shopme.client.dto.response.ProductPriceResponse;
import com.shopme.client.exception.type.ProductNotFoundException;
import com.shopme.client.mapper.ProductMapper;
import com.shopme.client.repository.ProductRepository;
import com.shopme.client.repository.PromotionProductRepository;
import com.shopme.client.service.ProductPriceService;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.PromotionProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductPriceServiceImpl implements ProductPriceService {
    private final ProductRepository productRepository;
    private final PromotionProductRepository promotionProductRepository;
    private final ProductMapper productMapper;


    @Override
    public int calculateFinalPrice(Product product) {
        return Math.round(
                product.getPrice()
                * (100 - calculateDiscountPercent(product)) / 100);
    }

    private float calculateDiscountPercent(Product product) {
        float discountPercentFromPromotion = getDiscountPercentFromPromotion(product);
        float discountPercentFromBaseProduct = getDiscountPercentFromBaseProduct(product);

        return 100 -
                (100 - discountPercentFromBaseProduct) * (100 - discountPercentFromPromotion) / 100;
    }

    @Override
    @Cacheable(value = "productPriceCache", key = "#productId")
    public ProductPriceResponse calculateProductPriceResponse(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        ProductNotFoundException::new
                );
        int price = product.getPrice();
        float discountPercent = calculateDiscountPercent(product);
        int discountPrice = calculateFinalPrice(product);
        return productMapper.toProductPriceResponse(price, discountPercent, discountPrice);
    }

    private float getDiscountPercentFromPromotion(Product product) {
        PromotionProduct promotionProduct = getPromotionProduct(product.getId());
        if (promotionProduct == null)
            return 0;

        return promotionProduct.getDiscountPercent();
    }

    private float getDiscountPercentFromBaseProduct(Product product) {
        return product.getDiscountPercent();
    }


    public PromotionProduct getPromotionProduct(Integer productId) {
        List<PromotionProduct> promotionProducts = promotionProductRepository.findAllByProductIdAndPromotionActive(productId);
        return getHighestDiscountPromotionProduct(promotionProducts);
    }

    private PromotionProduct getHighestDiscountPromotionProduct(List<PromotionProduct> promotionProducts) {
        return promotionProducts.stream()
                .max((p1, p2) -> Float.compare(p1.getDiscountPercent(), p2.getDiscountPercent()))
                .orElse(null);
    }
}
