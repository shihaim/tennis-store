package com.tnc.study.tennisstore;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.Email;
import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.Password;
import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.member.MemberGrade;
import com.tnc.study.tennisstore.domain.member.QMember;
import com.tnc.study.tennisstore.domain.product.Product;
import com.tnc.study.tennisstore.domain.product.QProduct;
import com.tnc.study.tennisstore.domain.product.ball.Ball;
import com.tnc.study.tennisstore.domain.product.ball.BallType;
import com.tnc.study.tennisstore.domain.product.racquet.Racquet;
import com.tnc.study.tennisstore.domain.product.shoes.GroundType;
import com.tnc.study.tennisstore.domain.product.shoes.Shoes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.tnc.study.tennisstore.domain.member.QMember.*;
import static com.tnc.study.tennisstore.domain.product.QProduct.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class QueryDSLTest {


    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    void setup() {
        queryFactory = new JPAQueryFactory(em);

        Member member = new Member(
                Email.of("kjpmj@tnctec.co.kr"),
                Password.of("1234"),
                "김명진",
                new Address("서울시 관악구 은천로 37길 21", "501호", "05123")
        );
        Member member2 = new Member(
                Email.of("kjpmj@naver.com"),
                Password.of("1234"),
                "김명진",
                new Address("서울시 관악구 은천로 37길 21", "501호", "05123")
        );
        Member member3 = new Member(
                Email.of("kjpmj77@kakao.com"),
                Password.of("1234"),
                "MJK",
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
        }
    }

    @Test
    @DisplayName("기본 검색 조건 1")
    void test1() {
        QMember member = new QMember("member");

        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.name.eq("김명진")
                                .and(member.grade.eq(MemberGrade.SILVER))
                )
                .fetchOne();

        assertThat(findMember).isNotNull();
        assertThat(findMember.getName()).isEqualTo("김명진");
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
                        member.name.eq("김명진"),
                        member.grade.eq(MemberGrade.SILVER)
                ).fetchOne();

        assertThat(findMember).isNotNull();
        assertThat(findMember.getName()).isEqualTo("김명진");
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
        System.out.println("sum = " + sum);
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
}
