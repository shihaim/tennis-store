package com.tnc.study.tennisstore.application.order;

import com.tnc.study.tennisstore.domain.order.OrderState;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FindOrderResponse2 {

    private Long orderId;
    private Long memberId;
    private String memberName;
    private LocalDateTime orderDate;
    private OrderState orderState;
    private String orderStateMessage;
    private List<FindOrderLineResponse> orderLines;

    public FindOrderResponse2(Long orderId, Long memberId, String memberName,
                              LocalDateTime orderDate, OrderState orderState) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.orderDate = orderDate;
        this.orderState = orderState;
        this.orderStateMessage = orderState.getMessage();
    }
}
