package com.tnc.study.tennisstore;

import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.Email;
import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.Password;
import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.member.MemberGrade;
import com.tnc.study.tennisstore.domain.order.Order;
import com.tnc.study.tennisstore.domain.order.OrderLine;
import com.tnc.study.tennisstore.domain.order.Receiver;
import com.tnc.study.tennisstore.domain.product.Product;
import com.tnc.study.tennisstore.domain.product.ball.Ball;
import com.tnc.study.tennisstore.domain.product.ball.BallType;
import com.tnc.study.tennisstore.domain.product.racquet.Racquet;
import com.tnc.study.tennisstore.domain.product.shoes.GroundType;
import com.tnc.study.tennisstore.domain.product.shoes.Shoes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
@Profile(value = "local")
public class initData implements CommandLineRunner {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void run(String... args) throws Exception {
        Member member = new Member(
                Email.of("hashi00518@tnctec.co.kr"),
                Password.of("1234"),
                "하승완",
                new Address("서울특별시 강남구 도곡로 117", "12층", "06253")
        );

        Member member2 = new Member(
                Email.of("hashi00517@tnctec.co.kr"),
                Password.of("1234"),
                "하승완",
                new Address("서울특별시 강남구 도곡로 117", "12층", "06253")
        );

        Member member3 = new Member(
                Email.of("hashi00519@tnctec.co.kr"),
                Password.of("1234"),
                "하승완",
                new Address("서울특별시 강남구 도곡로 117", "12층", "06253")
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
                    "라켓" + i,
                    "라켓 설명" + i,
                    "요넥스",
                    Money.of(350_000L + i),
                    10 + i,
                    305,
                    98,
                    315
            );

            Shoes shoes = new Shoes(
                    "신발" + i,
                    "신발 설명" + i,
                    "나이키",
                    Money.of(149_000L + i),
                    10 + i,
                    GroundType.HARD
            );

            Ball ball = new Ball(
                    "공" + i,
                    "공 설명" + i,
                    "월슨",
                    Money.of(3000L + i),
                    10 + i,
                    BallType.COMPETITION
            );

            em.persist(racquet);
            em.persist(shoes);
            em.persist(ball);
        }

        for (int i = 1; i <= 100; i++) {
            Product product1 = em.find(Product.class, i);
            Product product2 = em.find(Product.class, i + 1);
            Product product3 = em.find(Product.class, i + 2);

            List<OrderLine> orderLines = Arrays.asList(
                    new OrderLine(product1, product1.getPrice(), 1),
                    new OrderLine(product2, product2.getPrice(), 1),
                    new OrderLine(product3, product3.getPrice(), 1)
            );

            Receiver receiver = new Receiver(member.getName(), "010-1234-5678");
            Order order = new Order(member, orderLines, member.getAddress(), receiver, "안전하게 와주세요");
            em.persist(order);
        }
    }
}
