package com.tnc.study.tennisstore.domain.product.query;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tnc.study.tennisstore.application.product.FindProductResponse;
import com.tnc.study.tennisstore.application.product.QFindProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.tnc.study.tennisstore.domain.product.QProduct.*;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<FindProductResponse> findProductByCondition(FindProductCondition condition, Pageable pageable) {
        // 검색 조건을 적용한 Query
        JPAQuery<FindProductResponse> query = queryFactory
                .select(new QFindProductResponse(
                        product.id,
                        product.productType,
                        product.name,
                        product.description,
                        product.brand,
                        product.price.amount,
                        product.stockQuantity
                ))
                .from(product)
                .where(
                        nameContains(condition.name()),
                        brandEq(condition.brand()),
                        priceGoe(condition.priceGoe()),
                        priceLoe(condition.priceLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // Sort 적용 (Orer by)
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(product.getType(), product.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }

        // query에서 List 조회
        List<FindProductResponse> content = query.fetch();

        // 카운트 쿼리
//        Long total = queryFactory
//                .select(product.count())
//                .from(product)
//                .where(
//                        nameContains(condition.name()),
//                        brandEq(condition.brand()),
//                        priceGoe(condition.priceGoe()),
//                        priceLoe(condition.priceLoe())
//                )
//                .fetchOne();

        JPAQuery<Long> countQuery = queryFactory
                .select(product.count())
                .from(product)
                .where(
                        nameContains(condition.name()),
                        brandEq(condition.brand()),
                        priceGoe(condition.priceGoe()),
                        priceLoe(condition.priceLoe())
                );

//        return new PageImpl<>(content, pageable, total);
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression nameContains(String name) {
        return StringUtils.hasText(name) ? product.name.contains(name) : null;
    }

    private BooleanExpression brandEq(String brand) {
        return StringUtils.hasText(brand) ? product.brand.eq(brand) : null;
    }

    private BooleanExpression priceGoe(Long priceGoe) {
        return priceGoe != null ? product.price.amount.goe(priceGoe) : null;
    }

    private BooleanExpression priceLoe(Long priceLoe) {
        return priceLoe != null ? product.price.amount.loe(priceLoe) : null;
    }

}
