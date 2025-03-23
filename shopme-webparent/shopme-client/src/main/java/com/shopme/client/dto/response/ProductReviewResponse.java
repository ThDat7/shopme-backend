package com.shopme.client.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ProductReviewResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String headline;
    private String comment;
    private int rating;
    private Date reviewTime;
}
