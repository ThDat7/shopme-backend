package com.shopme.common.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CustomerStatus {
    VERIFIED("Verified"),
    UNVERIFIED("Unverified"),
    NEED_INFO("Need Info");

    private final String description;
}
