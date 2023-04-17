package com.tnc.study.tennisstore.domain.product.ball;

import com.tnc.study.tennisstore.framework.domain.CodeModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BallType implements CodeModel {
    PRACTICE("연습구"),
    COMPETITION("시합구");

    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }
}
