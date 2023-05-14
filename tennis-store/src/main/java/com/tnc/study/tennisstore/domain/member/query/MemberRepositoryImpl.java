package com.tnc.study.tennisstore.domain.member.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.member.MemberGrade;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.tnc.study.tennisstore.domain.member.QMember.*;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> findMembersByCondition(FindMemberCondition condition) {
        return queryFactory
                .select(member)
                .from(member)
                .where(
                        emailContains(condition.email()),
                        nameContains(condition.name()),
                        withdrawalEq(condition.withdrawal()),
                        gradeEq(condition.grade())
                )
                .fetch();
    }

    private BooleanExpression emailContains(String email) {
        return StringUtils.hasText(email) ? member.email.address.contains(email) : null;
    }

    private BooleanExpression nameContains(String name) {
        return StringUtils.hasText(name) ? member.name.contains(name) : null;
    }

    private BooleanExpression withdrawalEq(Boolean withdrawal) {
        return withdrawal != null ? member.withdrawal.eq(withdrawal) : null;
    }

    private BooleanExpression gradeEq(MemberGrade grade) {
        return grade != null ? member.grade.eq(grade) : null;
    }
}
