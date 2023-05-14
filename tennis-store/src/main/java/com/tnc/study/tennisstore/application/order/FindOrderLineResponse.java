package com.tnc.study.tennisstore.application.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FindOrderLineResponse {
    private Long orderId;
    private Long orderLineId;
    private Long productId;
    private BigDecimal orderLinePrice;
    private Integer orderQuantity;

    public FindOrderLineResponse(Long orderId, Long orderLineId,
                                 Long productId, BigDecimal orderLinePrice, Integer orderQuantity) {
        this.orderId = orderId;
        this.orderLineId = orderLineId;
        this.productId = productId;
        this.orderLinePrice = orderLinePrice;
        this.orderQuantity = orderQuantity;
    }
}
