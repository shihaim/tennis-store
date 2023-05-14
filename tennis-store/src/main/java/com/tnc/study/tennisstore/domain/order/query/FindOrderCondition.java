package com.tnc.study.tennisstore.domain.order.query;

import com.tnc.study.tennisstore.domain.order.OrderState;

public record FindOrderCondition(
        String memberName,
        OrderState orderState
) {
}
