package com.tnc.study.tennisstore.domain.order.query;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tnc.study.tennisstore.domain.order.Order;
import com.tnc.study.tennisstore.domain.order.OrderState;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.tnc.study.tennisstore.domain.member.QMember.*;
import static com.tnc.study.tennisstore.domain.order.QDelivery.*;
import static com.tnc.study.tennisstore.domain.order.QOrder.*;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Order> findOrdersByCondition(FindOrderCondition condition, Pageable pageable) {
        JPAQuery<Order> query = queryFactory
                .selectFrom(order)
                .join(order.member, member).fetchJoin()
                .join(order.delivery, delivery).fetchJoin()
                .where(
                        memberNameContains(condition.memberName()),
                        orderStateEq(condition.orderState())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // Sort 적용 (Orer by)
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(order.getType(), order.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC, pathBuilder.get(o.getProperty())));
        }

        List<Order> content = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(
                        memberNameContains(condition.memberName()),
                        orderStateEq(condition.orderState())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression memberNameContains(String memberName) {
        return StringUtils.hasText(memberName) ? order.member.name.contains(memberName) : null;
    }

    private BooleanExpression orderStateEq(OrderState orderState) {
        return orderState != null ? order.state.eq(orderState) : null;
    }

    public Slice<Order> findOrdersByMemberIdNoOffset(Long memberId, Long lastOrderId, Pageable pageable) {
        List<Order> orders = queryFactory
                .selectFrom(order)
                .join(order.member, member).fetchJoin()
                .join(order.delivery, delivery).fetchJoin()
                .where(
                        order.member.id.eq(memberId),
                        orderIdLt(lastOrderId)
                )
                .orderBy(order.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, orders);
    }

    private BooleanExpression orderIdLt(Long lastOrderId) {
        return lastOrderId != null ? order.id.lt(lastOrderId) : null;
    }

    // 무한 스크롤 방식 처리하는 메서드
    private Slice<Order> checkLastPage(Pageable pageable, List<Order> results) {
        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 데이터가 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            // 요청한 갯수보다 1개 더 조회했으니 마지막 하나는 제거한다.
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
