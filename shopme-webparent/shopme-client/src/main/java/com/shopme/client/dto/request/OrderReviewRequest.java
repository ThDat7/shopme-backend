package com.shopme.client.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class OrderReviewRequest {
    private String headline;
    private String comment;
    private int rating;
}
