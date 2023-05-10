package com.tnc.study.tennisstore.domain.member.query;

import com.tnc.study.tennisstore.domain.member.Member;

import java.util.List;

public interface MemberCustomRepository {

    List<Member> findMembersByCondition(FindMemberCondition condition);
}
