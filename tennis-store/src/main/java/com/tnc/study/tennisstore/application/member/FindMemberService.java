package com.tnc.study.tennisstore.application.member;

import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.member.MemberRepository;
import com.tnc.study.tennisstore.domain.member.query.FindMemberCondition;
import com.tnc.study.tennisstore.domain.member.query.MemberQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindMemberService {

    private final MemberRepository memberRepository;
    private final MemberQuerydslRepository memberQuerydslRepository;

    public List<FindMemberResponse> findMembers() {
        return memberRepository.findAll().stream()
                .map(member -> new FindMemberResponse(
                        member.getId(),
                        member.getEmail().getAddress(),
                        member.getName(),
                        member.getAddress().getAddress1(),
                        member.getAddress().getAddress2(),
                        member.getAddress().getZipcode(),
                        member.getGrade()
                )).toList();
    }

    public FindMemberResponse findMember(Long id) {
        Member member = MemberServiceHelper.findExistingMember(memberRepository, id);
        return new FindMemberResponse(
                member.getId(),
                member.getEmail().getAddress(),
                member.getName(),
                member.getAddress().getAddress1(),
                member.getAddress().getAddress2(),
                member.getAddress().getZipcode(),
                member.getGrade()
        );
    }

    public List<FindMemberResponse> findMembers(FindMemberCondition condition) {
        return memberQuerydslRepository.findMembersByCondition(condition)
                .stream()
                .map(member -> new FindMemberResponse(
                        member.getId(),
                        member.getEmail().getAddress(),
                        member.getName(),
                        member.getAddress().getAddress1(),
                        member.getAddress().getAddress2(),
                        member.getAddress().getZipcode(),
                        member.getGrade()
                )).toList();
    }
}
