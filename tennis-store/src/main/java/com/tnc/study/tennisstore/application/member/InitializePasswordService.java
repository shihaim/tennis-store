package com.tnc.study.tennisstore.application.member;

import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InitializePasswordService {
    private final MemberRepository memberRepository;

    public String initializePassword(Long memberId) {
        Member findMember = MemberServiceHelper.findExistingMember(memberRepository, memberId);
        return findMember.initializePassword();
    }
}

