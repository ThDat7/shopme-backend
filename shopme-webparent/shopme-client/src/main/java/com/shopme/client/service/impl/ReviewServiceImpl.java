package com.shopme.client.service.impl;

import com.shopme.client.dto.response.ProductReviewResponse;
import com.shopme.client.mapper.ReviewMapper;
import com.shopme.client.repository.ReviewRepository;
import com.shopme.client.service.ReviewService;
import com.shopme.common.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private static final String DEFAULT_SORT_FIELD = "reviewTime";
    private static final String DEFAULT_SORT_DIRECTION = "desc";
    private static final int DEFAULT_PRODUCTS_PER_PAGE = 4;

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    private Pageable getPageableFromParams(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", String.valueOf(DEFAULT_PRODUCTS_PER_PAGE)));
        String sortField = params.getOrDefault("sortField", DEFAULT_SORT_FIELD);
        String sortDirection = params.getOrDefault("sortDirection", DEFAULT_SORT_DIRECTION);

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return PageRequest.of(page, size, sort);
    }

    @Override
    public ListResponse<ProductReviewResponse> listPageByProduct(Map<String, String> params, Integer productId) {
        Pageable pageable = getPageableFromParams(params);
        Integer rating = params.containsKey("rating") ? Integer.parseInt(params.get("rating")) : null;

        Page<Review> reviewPage = reviewRepository.findAllByProductIdAndRating(productId, rating, pageable);
        List<ProductReviewResponse> productReviews = reviewPage.getContent().stream()
                .map(reviewMapper::toProductReviewResponse)
                .toList();

        return ListResponse.<ProductReviewResponse>builder()
                .content(productReviews)
                .totalPages(reviewPage.getTotalPages())
                .build();
    }
}
