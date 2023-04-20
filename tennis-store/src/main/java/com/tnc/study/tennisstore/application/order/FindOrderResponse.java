package com.tnc.study.tennisstore.application.order;

import com.tnc.study.tennisstore.domain.order.OrderState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record FindOrderResponse(
        Long orderId,
        Long memberId,
        String memberName,
        LocalDateTime orderDate,
        OrderState orderState,
        String orderStateMessage,
        List<OrderLineResponse> orderLines
) {

    public record OrderLineResponse(
            Long orderLineId,
            Long productId,
            BigDecimal orderLinePrice,
            Integer orderQuantity
    ) {

    }
}
