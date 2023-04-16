package com.tnc.study.tennisstore.application.member;

import com.tnc.study.tennisstore.domain.member.MemberGrade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangeMemberInfoRequest(
        @NotBlank
        String name,
        @NotBlank
        String address1,
        @NotBlank
        String address2,
        @NotBlank
        String zipcode,
        @NotNull
        MemberGrade grade

) {
}
