package com.tnc.study.tennisstore.application.member;

import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.Email;
import com.tnc.study.tennisstore.domain.Password;
import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.member.MemberGrade;
import com.tnc.study.tennisstore.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    CreateMemberService createMemberService;

    @Autowired
    FindMemberService findMemberService;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        Email email = Email.of("hashi00518@tnctec.co.kr");
        Password password = Password.of("1234");
        String name = "하승완";
        Address address = new Address("서울시 영등포구 신길동 51-3", "7층", "11111");

        Member member = new Member(email, password, name, address);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("회원 등록 서비스")
    void testCreateMember() {
        String email = "hashi00517@tnctec.co.kr";
        String password = "1234";
        String name = "하승완";
        String address1 = "서울시 영등포구 신길동 51-3";
        String address2 = "7층";
        String zipcode = "11111";

        CreateMemberRequest createMemberRequest = new CreateMemberRequest(email, password, name, address1, address2, zipcode);

        Long memberId = createMemberService.signUp(createMemberRequest);
        Member findMember = memberRepository.findById(memberId).get();

        assertThat(findMember.getEmail().getAddress()).isEqualTo(email);
        assertThat(findMember.getName()).isEqualTo(name);
        assertThat(findMember.getPassword().getValue()).isEqualTo(password);
        assertThat(findMember.getAddress().getAddress1()).isEqualTo(address1);
        assertThat(findMember.getAddress().getAddress2()).isEqualTo(address2);
        assertThat(findMember.getAddress().getZipcode()).isEqualTo(zipcode);
        assertThat(findMember.isWithdrawal()).isFalse();
        assertThat(findMember.getGrade()).isEqualTo(MemberGrade.BRONZE);
    }

    @Test
    @DisplayName("회원 이메일 중복 체크")
    void testCheckDuplicateEmail() throws Exception {
        //given
        String email = "hashi00518@tnctec.co.kr";
        String password = "1234";
        String name = "하승완";
        String address1 = "서울시 영등포구 신길동 51-3";
        String address2 = "7층";
        String zipcode = "11111";

        CreateMemberRequest createMemberRequest = new CreateMemberRequest(email, password, name, address1, address2, zipcode);

        //when
        DuplicateMemberException exception = Assertions.assertThrows(DuplicateMemberException.class,
                () -> createMemberService.signUp(createMemberRequest));

        //then
        assertThat(exception.getMessage()).isEqualTo("Email 중복");

    }

    @Test
    @DisplayName("회원 조회 서비스")
    void testFIndMembers() throws Exception {
        //given
        Email email = Email.of("hashi00518@gmail.com");
        Password password = Password.of("1234");
        String name = "HSW";
        Address address = new Address("서울시 영등포구 신길동 51-3", "7층", "11111");

        Member member = new Member(email, password, name, address);
        memberRepository.save(member);

        //when
        List<FindMemberResponse> members = findMemberService.findMembers();

        //then
        assertThat(members.size()).isEqualTo(2);
        assertThat(members).extracting("email").containsExactly("hashi00518@tnctec.co.kr", "hashi00518@gmail.com");
        assertThat(members).extracting("name").containsExactly("하승완", "HSW");
    }

}