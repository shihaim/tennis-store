package com.tnc.study.tennisstore;

import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.Email;
import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.Password;
import com.tnc.study.tennisstore.domain.member.Member;
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
                new Address("서울특별시 영등포구 신길동 51-3", "7층", "11111")
        );

        em.persist(member);

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
    }
}