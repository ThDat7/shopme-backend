package com.shopme.client.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarouselImageResponse {
    private Integer id;
    private String image;
    private String name;
    private String content;
}
