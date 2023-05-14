package com.tnc.study.tennisstore.domain.order;

import com.tnc.study.tennisstore.framework.domain.CodeModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderState implements CodeModel {
    ORDER_RECEIVED("주문 접수"),
    PURCHASE_CONFIRMATION("구매 확정"),
    CANCELED("주문 취소");

    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }
}
