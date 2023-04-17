package com.tnc.study.tennisstore.domain.product.shoes;

import com.tnc.study.tennisstore.framework.domain.CodeModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GroundType implements CodeModel {
    ALL("모든 코트"),
    HARD("하드 코트"),
    GRASS("잔디 코트"),
    CLAY("클레이 코트");

    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }
}
