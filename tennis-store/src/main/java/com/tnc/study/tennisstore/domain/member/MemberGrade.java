package com.tnc.study.tennisstore.domain.member;

import com.tnc.study.tennisstore.framework.domain.CodeModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberGrade implements CodeModel {
    BRONZE("브론즈"),
    SILVER("실버"),
    GOLD("골드"),
    PLATINUM("플래티넘"),
    DIAMOND("다이아몬드");

    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }
}
