package com.tnc.study.tennisstore.application.member;

import com.tnc.study.tennisstore.domain.member.MemberGrade;
import lombok.Getter;

//@Getter
public record FindMemberResponse(
        Long memberId,
        String email,
        String name,
        String address1,
        String address2,
        String zipcode,
        MemberGrade grade
) {
}
