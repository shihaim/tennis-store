package com.tnc.study.tennisstore.domain.product.query;

public record FindProductCondition(
        String name,
        String brand,
        Long priceGoe,
        Long priceLoe
) {
}
