package com.tnc.study.tennisstore.domain.order;

import com.tnc.study.tennisstore.framework.domain.CodeModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryState implements CodeModel {

    PREPARING("배송 준비 중"),
    RELEASE("출고 처리"),
    DELIVERING("배송 중"),
    DELIVERY_COMPLETE("배송 완료");

    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }
}
