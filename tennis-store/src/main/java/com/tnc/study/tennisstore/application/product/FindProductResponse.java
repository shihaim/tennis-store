package com.tnc.study.tennisstore.application.product;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.math.BigDecimal;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindProductResponse {
    private Long productId;
    private String productType;
    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
    private int stockQuantity;

    @QueryProjection
    public FindProductResponse(Long productId, String productType, String name, String description, String brand, BigDecimal price, int stockQuantity) {
        this.productId = productId;
        this.productType = productType;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
