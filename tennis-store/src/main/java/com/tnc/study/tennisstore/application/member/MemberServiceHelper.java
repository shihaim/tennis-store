package com.tnc.study.tennisstore.application.member;

import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.member.MemberRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberServiceHelper {

    public static Member findExistingMember(MemberRepository repository, Long memberId) {
        return repository.findById(memberId).orElseThrow(NoMemberException::new);
    }
}
