package com.tnc.study.tennisstore.domain.member.query;

import com.tnc.study.tennisstore.domain.member.MemberGrade;

public record FindMemberCondition(
        String email,
        String name,
        Boolean withdrawal,
        MemberGrade grade
) {
}
