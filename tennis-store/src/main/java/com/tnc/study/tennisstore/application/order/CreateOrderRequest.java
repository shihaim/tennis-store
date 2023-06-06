package com.tnc.study.tennisstore.application.order;

import com.tnc.study.tennisstore.domain.order.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record CreateOrderRequest(
        @NotNull
        Long memberId,
        @NotBlank
        String address1,
        @NotBlank
        String address2,
        @NotBlank
        String zipcode,
        @NotBlank
        String receiverName,
        @NotBlank
        String receiverPhone,
        String deliveryMessage,
        @NotEmpty
        List<OrderProduct> orderProducts
//        Map<Long, OrderProduct> orderProducts
) {
    public record OrderProduct(
            Long productId,
            int orderCount
    ) {
    }
}
