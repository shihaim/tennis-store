package com.tnc.study.tennisstore.application.member;

import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChangeMemberInfoService {

    private final MemberRepository memberRepository;

    public Long changeMemberInfo(Long memberId, ChangeMemberInfoRequest request) {

        Member findMember = MemberServiceHelper.findExistingMember(memberRepository, memberId);

        Address address = new Address(
                request.address1(),
                request.address2(),
                request.zipcode()
        );

        findMember.changeMemberInfo(
                request.name(),
                address,
                request.grade()
        );

        return findMember.getId();
    }

}
