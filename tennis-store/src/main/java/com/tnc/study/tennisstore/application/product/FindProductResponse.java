package com.tnc.study.tennisstore.application.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindProductResponse {
    private Long productId;
    private String productType;
    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
    private int stockQuantity;
}
