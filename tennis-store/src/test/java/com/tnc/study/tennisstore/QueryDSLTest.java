package com.tnc.study.tennisstore;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.Email;
import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.Password;
import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.member.MemberGrade;
import com.tnc.study.tennisstore.domain.member.QMember;
import com.tnc.study.tennisstore.domain.order.*;
import com.tnc.study.tennisstore.domain.product.Product;
import com.tnc.study.tennisstore.domain.product.QProduct;
import com.tnc.study.tennisstore.domain.product.ball.Ball;
import com.tnc.study.tennisstore.domain.product.ball.BallType;
import com.tnc.study.tennisstore.domain.product.racquet.Racquet;
import com.tnc.study.tennisstore.domain.product.shoes.GroundType;
import com.tnc.study.tennisstore.domain.product.shoes.Shoes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tnc.study.tennisstore.domain.member.QMember.*;
import static com.tnc.study.tennisstore.domain.order.QOrder.*;
import static com.tnc.study.tennisstore.domain.order.QOrderLine.*;
import static com.tnc.study.tennisstore.domain.product.QProduct.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class QueryDSLTest {


    @PersistenceContext
    EntityManager em;

    @PersistenceUnit
    EntityManagerFactory emf;

    JPAQueryFactory queryFactory;

    @BeforeEach
    void setup() {
        queryFactory = new JPAQueryFactory(em);

        Member member = new Member(
                Email.of("kjpmj@tnctec.co.kr"),
                Password.of("1234"),
                "하승완",
                new Address("서울시 관악구 은천로 37길 21", "501호", "05123")
        );
        Member member2 = new Member(
                Email.of("kjpmj@naver.com"),
                Password.of("1234"),
                "하승완",
                new Address("서울시 관악구 은천로 37길 21", "501호", "05123")
        );
        Member member3 = new Member(
                Email.of("kjpmj77@kakao.com"),
                Password.of("1234"),
                "HSW",
                new Address("서울시 관악구 은천로 37길 21", "501호", "05123")
        );

        member2.changeMemberInfo(
                member2.getName(),
                member2.getAddress(),
                MemberGrade.SILVER
        );

        member3.changeMemberInfo(
                member3.getName(),
                member3.getAddress(),
                MemberGrade.GOLD
        );

        em.persist(member);
        em.persist(member2);
        em.persist(member3);

        for (int i = 1; i <= 100; i++) {
            Racquet racquet = new Racquet(
                    "라켓 " + i,
                    "라켓 설명 " + i,
                    "요넥스",
                    Money.of(350_000L + i),
                    10 + i,
                    305,
                    98,
                    315
            );

            Shoes shoes = new Shoes(
                    "신발 " + i,
                    "신발 설명 " + i,
                    "나이키",
                    Money.of(149_000L + i),
                    10 + i,
                    GroundType.HARD
            );

            Ball ball = new Ball(
                    "공 " + i,
                    "공 설명 " + i,
                    "윌슨",
                    Money.of(3000L + i),
                    10 + i,
                    BallType.COMPETITION
            );

            em.persist(racquet);
            em.persist(shoes);
            em.persist(ball);

            List<OrderLine> orderLines1 = Arrays.asList(
                    new OrderLine(racquet, racquet.getPrice(), i),
                    new OrderLine(shoes, shoes.getPrice(), i)
            );
            List<OrderLine> orderLines2 = List.of(
                    new OrderLine(ball, ball.getPrice(), i)
            );

            Receiver receiver1 = new Receiver(member.getName(), "010-4461-0579");
            Receiver receiver2 = new Receiver(member3.getName(), "010-4461-0579");

            Order order1 = new Order(member, orderLines1, member.getAddress(), receiver1, "빨리 빨리 와주세요" + i);
            Order order2 = new Order(member3, orderLines2, member3.getAddress(), receiver2, "안전하게 와주세요" + i);
            em.persist(order1);
            em.persist(order2);
        }
    }

    @Test
    @DisplayName("기본 검색 조건 1")
    void test1() {
        QMember member = new QMember("member");

        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.name.eq("하승완")
                                .and(member.grade.eq(MemberGrade.SILVER))
                )
                .fetchOne();

        assertThat(findMember).isNotNull();
        assertThat(findMember.getName()).isEqualTo("하승완");
        assertThat(findMember.getGrade()).isEqualTo(MemberGrade.SILVER);
    }

    @Test
    @DisplayName("기본 검색 조건 2")
    void test2() {
        List<Product> products = queryFactory
                .selectFrom(product)
                .where(
                        product.price.amount.gt(350_090L)
                )
                .fetch();

        assertThat(products.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("AND 조건을 파라미터로 처리")
    void test3() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.name.eq("하승완"),
                        member.grade.eq(MemberGrade.SILVER)
                ).fetchOne();

        assertThat(findMember).isNotNull();
        assertThat(findMember.getName()).isEqualTo("하승완");
        assertThat(findMember.getGrade()).isEqualTo(MemberGrade.SILVER);
    }

    @Test
    @DisplayName("기본 검색 조건 2")
    void test4() {
        List<Product> products = queryFactory
                .selectFrom(product)
                .where(
                        product.price.amount.gt(350_090L)
                )
                .orderBy(product.price.amount.desc(), product.stockQuantity.asc().nullsLast())
                .fetch();

        assertThat(products.size()).isEqualTo(10);
        assertThat(products.get(0).getPrice()).isEqualTo(Money.of(350_100));
        assertThat(products.get(0).getName()).isEqualTo("라켓 100");
        assertThat(products.get(1).getPrice()).isEqualTo(Money.of(350_099));
        assertThat(products.get(1).getName()).isEqualTo("라켓 99");
    }

    @Test
    @DisplayName("기본 검색 조건 2")
    void test5() {
        List<Member> members = queryFactory
                .selectFrom(member)
                .orderBy(member.id.desc())
                .offset(1) // 0부터 시작 -> 회원 데이터 3개 중에 한 개 건너 띄고
                .limit(2) // 최대 2건을 조회
                .fetch();

        assertThat(members.size()).isEqualTo(2);
        assertThat(members.get(0).getId()).isEqualTo(2);
        assertThat(members.get(1).getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("페이징 - 전체 데이터 건수")
    void test6() {

        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.id.desc())
                .offset(1) // 0부터 시작 -> 회원 데이터 3개 중에 한 개 건너 띄고
                .limit(2) // 최대 2건을 조회
                .fetchResults();

        assertThat(queryResults.getTotal()).isEqualTo(3);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2);

        assertThat(queryResults.getResults().get(0).getId()).isEqualTo(2);
        assertThat(queryResults.getResults().get(1).getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("집합")
    void test7() {

        List<Tuple> result = queryFactory
                .select(product.count(),
                        product.price.amount.sum(),
                        product.price.amount.avg(),
                        product.price.amount.max(),
                        product.price.amount.min()
                )
                .from(product)
                .fetch();

        Tuple tuple = result.get(0);
        Long count = tuple.get(product.count());

        BigDecimal sum = tuple.get(product.price.amount.sum());
        Double avg = tuple.get(product.price.amount.avg());
        BigDecimal max = tuple.get(product.price.amount.max());
        BigDecimal min = tuple.get(product.price.amount.min());

        assertThat(count).isEqualTo(300);
        assertThat(sum.compareTo(BigDecimal.valueOf(50_215_150)) == 0).isTrue();
        assertThat(max.compareTo(BigDecimal.valueOf(350_100)) == 0).isTrue();
        assertThat(min.compareTo(BigDecimal.valueOf(3001)) == 0).isTrue();
    }

    @Test
    @DisplayName("GroupBy")
    void test8() {

        List<Tuple> result = queryFactory
                .select(product.brand, product.price.amount.avg())
                .from(product)
                .groupBy(product.brand)
                .having(product.price.amount.avg().gt(100_000))
                .orderBy(product.price.amount.avg().desc())
                .fetch();

        Tuple tuple1 = result.get(0);
        Tuple tuple2 = result.get(1);

        // 라켓 정보
        String tuple1Brand = tuple1.get(product.brand);
        Double tuple1Avg = tuple1.get(product.price.amount.avg());

        // 신발 정보
        String tuple2Brand = tuple2.get(product.brand);
        Double tuple2Avg = tuple2.get(product.price.amount.avg());

        assertThat(tuple1Brand).isEqualTo("요넥스");
        assertThat(tuple1Avg).isEqualTo(350_050.5);
        assertThat(tuple2Brand).isEqualTo("나이키");
        assertThat(tuple2Avg).isEqualTo(149_050.5);
    }

    @Test
    @DisplayName("조인")
    void test9() {
        em.flush();
        em.clear();

        List<Order> orders = queryFactory
                .selectFrom(order)
                .join(order.member, member)
                .where(member.name.eq("HSW"))
                .fetch();

        // false면 아직 로딩 전 / true면 로딩된 상태
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(orders.get(0).getMember());

        assertThat(loaded).isFalse();
//        Order order1 = orders.get(0);
//        assertThat(order1.getOrderLines().get(0).getProduct().getName()).isEqualTo("공 1");
    }

    @Test
    @DisplayName("조인 - fetch join")
    void test10() {
        em.flush();
        em.clear();

        List<Order> orders = queryFactory
                .selectFrom(order)
                .join(order.member, member).fetchJoin()
                .where(member.name.eq("HSW"))
                .fetch();

        // false면 아직 로딩 전 / true면 로딩된 상태
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(orders.get(0).getMember());

        assertThat(loaded).isTrue();
//        Order order1 = orders.get(0);
//        assertThat(order1.getOrderLines().get(0).getProduct().getName()).isEqualTo("공 1");
    }

    @Test
    @DisplayName("서브 쿼리 - Where")
    void test11() {
        QProduct productSub = new QProduct("productSub");

        Product findProduct = queryFactory
                .selectFrom(product)
                .where(product.price.amount.eq(
                        JPAExpressions
                                .select(productSub.price.amount.max())
                                .from(productSub)
                ))
                .fetchOne();

        assertThat(findProduct.getName()).isEqualTo("라켓 100");
        assertThat(findProduct.getPrice()).isEqualTo(Money.of(350_100L));
    }

    @Test
    @DisplayName("서브 쿼리 - Select")
    void test12() {

        JPQLQuery<BigDecimal> orderTotalPriceExpression = JPAExpressions
                .select(orderLine.orderPrice.amount.sum())
                .from(order)
                .join(orderLine)
                .on(order.id.eq(orderLine.order.id))
                .where(member.id.eq(order.member.id));

        List<Tuple> result = queryFactory
                .select(member.name, orderTotalPriceExpression)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String memberName = tuple.get(member.name);
            BigDecimal orderTotalPrice = tuple.get(orderTotalPriceExpression);

            System.out.printf("%s : %s\n", memberName, (orderTotalPrice != null ? orderTotalPrice : BigDecimal.ZERO));

        }
    }

    @Test
    @DisplayName("서브 쿼리 - From (inline view)")
    void test13() {
        List<jakarta.persistence.Tuple> resultList = em.createQuery("""
                            select t.name, t.fullAddress
                            from (
                               select m.name as name,
                                      m.address.address1 || ' ' || m.address.address2 || ' ' || m.address.zipcode as fullAddress
                               from Member m
                            ) t
                        """, jakarta.persistence.Tuple.class)
                .getResultList();

        for (jakarta.persistence.Tuple tuple : resultList) {
            String memberName = tuple.get(0, String.class);
            String fullAddress = tuple.get(1, String.class);
            System.out.println(memberName + ": " + fullAddress);
        }
    }

    @Test
    @DisplayName("Case 문 - 단순한 조건")
    void test14() {
        List<String> result = queryFactory
                .select(member.name
                        .when("하승완").then("한글")
                        .when("HSW").then("영어")
                        .otherwise("하하하하"))
                .from(member)
                .orderBy(member.name.asc()).fetch();

        assertThat(result.size()).isEqualTo(3);
        assertThat(result).containsExactly("영어", "한글", "한글");
    }

    @Test
    @DisplayName("Case 문 - 복잡한 조건")
    void test15() {
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(product.price.amount.between(0, 100_000)).then("저렴")
                        .when(product.price.amount.between(100_000, 200_000)).then("적당")
                        .otherwise("비쌈")
                )
                .from(product)
                .fetch();

        Map<String, List<String>> collect = result.stream()
                .collect(Collectors.groupingBy(str -> str));

        assertThat(result.size()).isEqualTo(300);
        assertThat(collect.size()).isEqualTo(3);
        assertThat(collect.get("저렴").size()).isEqualTo(100);
        assertThat(collect.get("적당").size()).isEqualTo(100);
        assertThat(collect.get("비쌈").size()).isEqualTo(100);
    }

    @Test
    @DisplayName("Case 문 - Order by절에서 사용")
    void test16() {

        NumberExpression<Integer> priceRank = new CaseBuilder()
                .when(product.price.amount.between(0, 100_000)).then(2)
                .when(product.price.amount.between(100_000, 200_000)).then(1)
                .otherwise(3);

        List<Tuple> result = queryFactory
                .select(product.name, product.price.amount, priceRank)
                .from(product)
                .orderBy(priceRank.asc())
                .fetch();

        for (Tuple tuple : result) {
            String productName = tuple.get(product.name);
            BigDecimal price = tuple.get(product.price.amount);
            Integer rank = tuple.get(priceRank);

            System.out.println(productName + ": " + price + ": " + rank);
        }
    }

    @Test
    @DisplayName("상수 사용")
    void test17() {

        // 자바에서 적용
        Tuple result = queryFactory
                .select(member.name, Expressions.constant("오늘 점심 뭐 먹지?"))
                .from(member)
                .where(member.name.eq("HSW"))
                .fetchOne();

        String memberName = result.get(member.name);
        String constant = result.get(Expressions.constant("오늘 점심 뭐 먹지?"));

        assertThat(memberName).isEqualTo("HSW");
        assertThat(constant).isEqualTo("오늘 점심 뭐 먹지?");
    }

    @Test
    @DisplayName("문자 더하기")
    void test18() {
        String result = queryFactory
                .select(member.name.concat("_").concat(member.grade.stringValue()))
                .from(member)
                .where(member.name.eq("HSW"))
                .fetchOne();

        assertThat(result).isEqualTo("HSW_GOLD");
    }

    @Test
    @DisplayName("Querydsl 빈 생성 - setter")
    void test19() {
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.name,
                        member.grade,
                        member.address.address1
                                .concat(" ")
                                .concat(member.address.address2)
                                .concat(" ")
                                .concat(member.address.zipcode).as("fullAddress")
                ))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    @DisplayName("Querydsl 빈 생성 - Field")
    void test20() {
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.name,
                        member.grade,
                        member.address.address1
                                .concat(" ")
                                .concat(member.address.address2)
                                .concat(" ")
                                .concat(member.address.zipcode).as("fullAddress")
                ))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    @DisplayName("Querydsl 빈 생성 - Constructor")
    void test21() {
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.name,
                        member.address.address1
                                .concat(" ")
                                .concat(member.address.address2)
                                .concat(" ")
                                .concat(member.address.zipcode).as("fullAddress"),
                        member.grade
                ))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    @DisplayName("Querydsl 빈 생성 - QueryProjection")
    void test22() {
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(
                        member.name,
                        member.address.address1
                                .concat(" ")
                                .concat(member.address.address2)
                                .concat(" ")
                                .concat(member.address.zipcode).as("fullAddress"),
                        member.grade
                ))
                .from(member)
                .fetch();

        // then
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    @DisplayName("동적 쿼리 - BooleanBuilder")
    void test23() throws Exception {
        // given
        String nameCondition = "하승완";
        MemberGrade gradeCondition = MemberGrade.SILVER;

        // when
        List<Member> members1 = searchMember1(nameCondition, gradeCondition);
        List<Member> members2 = searchMember1(nameCondition, null);

        // then
        Member member1 = members1.get(0);

        assertThat(members1.size()).isEqualTo(1);
        assertThat(member1.getName()).isEqualTo("하승완");
        assertThat(member1.getGrade()).isEqualTo(MemberGrade.SILVER);

        assertThat(members2.size()).isEqualTo(2);

    }

    private List<Member> searchMember1(String nameCondition, MemberGrade gradeCondition) {
        BooleanBuilder builder = new BooleanBuilder();

        if (nameCondition != null) {
            builder.and(member.name.eq(nameCondition));
        }

        if (gradeCondition != null) {
            builder.and(member.grade.eq(gradeCondition));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    @Test
    @DisplayName("동적 쿼리 - Where")
    void test24() throws Exception {
        // given
        String nameCondition = "하승완";
        MemberGrade gradeCondition = MemberGrade.SILVER;

        // when
        List<Member> members1 = searchMember2(nameCondition, gradeCondition);
        List<Member> members2 = searchMember2(nameCondition, null);

        // then
        Member member1 = members1.get(0);

        assertThat(members1.size()).isEqualTo(1);
        assertThat(member1.getName()).isEqualTo("하승완");
        assertThat(member1.getGrade()).isEqualTo(MemberGrade.SILVER);

        assertThat(members2.size()).isEqualTo(2);
    }

    private List<Member> searchMember2(String nameCondition, MemberGrade gradeCondition) {
        return queryFactory
                .selectFrom(member)
                .where(
                        nameEq(nameCondition),
                        gradeEq(gradeCondition)
                )
                .fetch();
    }

    private BooleanExpression nameEq(String name) {
        return StringUtils.hasText(name) ? member.name.eq(name) : null;
    }

    private BooleanExpression gradeEq(MemberGrade grade) {
        return grade != null ? member.grade.eq(grade) : null;
    }

    @Test
    @DisplayName("수정 벌크 연산")
    void test25() throws Exception {
        // given

        // when
        long count = queryFactory
                .update(product)
                .set(product.description, "매우 비싸네요")
                .where(product.price.amount.goe(300_000))
                .execute();

        // then
        assertThat(count).isEqualTo(100);
    }

    @Test
    @DisplayName("수정 벌크 연산 2")
    void test26() throws Exception {
        // given

        // when
        long count = queryFactory
                .update(product)
                .set(product.price.amount, product.price.amount.multiply(Expressions.constant(1.1)))
                .where(product.name.eq("공 1"))
                .execute();

        // then
        em.flush();
        em.clear();

        Product ball1 = queryFactory
                .selectFrom(product)
                .where(product.name.eq("공 1"))
                .fetchOne();

        assertThat(count).isEqualTo(1);
        assertThat(ball1.getPrice()).isEqualTo(Money.of(3301.10));
    }

    @Test
    @DisplayName("삭제 벌크 연산")
    void test27() throws Exception {
        // given

        // when
        long count = queryFactory
                .delete(member)
                .where(member.name.eq("하승완"), member.grade.eq(MemberGrade.SILVER))
                .execute();

        // then
        em.flush();
        em.clear();

        Long productSize = queryFactory
                .select(member.count())
                .from(member)
                .fetchOne();

        assertThat(count).isEqualTo(1);
        assertThat(productSize).isEqualTo(2);
    }

    @Test
    @DisplayName("SQL function - Replace")
    void test28() throws Exception {
        // given

        // when
        Tuple tuple = queryFactory
                .select(
                        member.name.as("originalName"),
                        Expressions.stringTemplate("function('replace', {0}, {1}, {2})", member.name, "H", "Ha").as("replaceName")
                )
                .from(member)
                .where(member.name.eq("HSW"))
                .fetchOne();

        // then
        String originalName = tuple.get(0, String.class);
        String replaceName = tuple.get(1, String.class);

        assertThat(originalName).isEqualTo("HSW");
        assertThat(replaceName).isEqualTo("HaSW");
    }
    
    @Test
    @DisplayName("SQL function - lower")
    void test29() throws Exception {
        // given
        
        // when
        Tuple tuple = queryFactory
                .select(
                        member.name.as("originalName"),
                        Expressions.stringTemplate("function('lower', {0})", member.name).as("lowerName"),
                        member.name.lower().as("lowerName2")
                )
                .from(member)
                .where(member.name.eq("HSW"))
                .fetchOne();

        // then
        String originalName = tuple.get(0, String.class);
        String lowerName = tuple.get(1, String.class);
        String lowerName2 = tuple.get(2, String.class);

        assertThat(originalName).isEqualTo("HSW");
        assertThat(lowerName).isEqualTo("hsw");
        assertThat(lowerName2).isEqualTo("hsw");
    }
}
