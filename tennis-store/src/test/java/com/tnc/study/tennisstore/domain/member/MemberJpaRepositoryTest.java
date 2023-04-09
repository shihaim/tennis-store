package com.tnc.study.tennisstore.domain.member;

import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.Email;
import com.tnc.study.tennisstore.domain.Password;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class MemberJpaRepositoryTest {

//    @Autowired
//    MemberJpaRepository repository;

    @Autowired
    MemberRepository repository;

    @Test
    @DisplayName("회원 테스트")
    void testMember() throws Exception {
        //given
        Email email = Email.of("hashi00518@tnctec.co.kr");
        Address address = new Address("서울시 영등포구", "신길동 51-3", "11111");
        Password password = Password.of("1234");
        Member member = new Member(email, password, "하승완", address);

        //when
        Member savedMember = repository.save(member);
        Member findMember = repository.findById(savedMember.getId()).get();

        //then
        assertThat(findMember.getEmail()).isEqualTo(email);
        assertThat(findMember.getName()).isEqualTo("하승완");
        assertThat(findMember.getAddress()).isEqualTo(address);
        assertThat(findMember.getGrade()).isEqualTo(MemberGrade.BRONZE);
        assertThat(findMember.isWithdrawal()).isFalse();
        assertThat(findMember).isEqualTo(member);
    
    }
}