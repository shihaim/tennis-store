package com.tnc.study.tennisstore.application.order;

import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.order.OrderState;
import com.tnc.study.tennisstore.domain.order.Receiver;

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
        BigDecimal orderTotalPrice,
        List<OrderLineResponse> orderLines,
        DeliveryResponse delivery
) {

    public record OrderLineResponse(
            Long orderLineId,
            Long productId,
            BigDecimal orderLinePrice,
            Integer orderQuantity
    ) {

    }

    public record DeliveryResponse(
        Long deliveryId,
        Address address,
        Receiver receiver,
        String deliveryMessage,
        BigDecimal deliveryFee,
        String trackingNumber
    ) {

    }
}
