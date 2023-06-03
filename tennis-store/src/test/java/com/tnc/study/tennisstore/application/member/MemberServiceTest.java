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
    ChangeMemberInfoService changeMemberInfoService;

    @Autowired
    DeleteMemberService deleteMemberService;

    @Autowired
    InitializePasswordService initializePasswordService;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        Email email = Email.of("hashi00518@tnctec.co.kr");
        Password password = Password.of("1234");
        String name = "하승완";
        Address address = new Address("서울특별시 강남구 도곡로 117", "12층", "06253");

        Member member = new Member(email, password, name, address);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("회원 등록 서비스")
    void testCreateMember() {
        String email = "hashi00517@tnctec.co.kr";
        String password = "1234";
        String name = "하승완";
        String address1 = "서울특별시 강남구 도곡로 117";
        String address2 = "12층";
        String zipcode = "06253";

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
        String address1 = "서울특별시 강남구 도곡로 117";
        String address2 = "12층";
        String zipcode = "06253";

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
        Address address = new Address("서울특별시 강남구 도곡로 117", "12층", "06253");

        Member member = new Member(email, password, name, address);
        memberRepository.save(member);

        //when
        List<FindMemberResponse> members = findMemberService.findMembers();

        //then
        assertThat(members.size()).isEqualTo(2);
        assertThat(members).extracting("email").containsExactly("hashi00518@tnctec.co.kr", "hashi00518@gmail.com");
        assertThat(members).extracting("name").containsExactly("하승완", "HSW");
    }

    @Test
    @DisplayName("회원 정보 변경 서비스")
    void testChangeMemberInfo() throws Exception {
        // given
        String name = "HSW";
        String address1 = "서울시 강남구 도곡로 117";
        String address2 = "12층";
        String zipcode = "06253";
        MemberGrade grade = MemberGrade.GOLD;

        ChangeMemberInfoRequest request = new ChangeMemberInfoRequest(
                name,
                address1,
                address2,
                zipcode,
                grade
        );

        Member member = memberRepository.findAll().get(0);

        // when
        Long memberId = changeMemberInfoService.changeMemberInfo(member.getId(), request);
        Member findMember = memberRepository.findById(memberId).get();

        // then
        assertThat(findMember.getName()).isEqualTo(name);
        assertThat(findMember.getAddress()).isEqualTo(new Address(address1, address2, zipcode));
        assertThat(findMember.getGrade()).isEqualTo(grade);
    }

    @Test
    @DisplayName("회원 삭제 서비스")
    void testDeleteMember() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);

        // when
        deleteMemberService.deleteMember(member.getId());
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(members).isEmpty();
    }


    @Test
    @DisplayName("회원 패스워드 초기화 서비스")
    void testInitializePassword() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);
        Long memberId = member.getId();

        // when
        String initializedPassword = initializePasswordService.initializePassword(memberId);

        // then
        assertThat(member.getPassword().getValue()).isEqualTo(initializedPassword);
    }

    @Test
    @DisplayName("회원 ID를 통한 단건 조회 서비스")
    void testFindMember() throws Exception {
        //given
        String email = "hashi00517@tnctec.co.kr";
        String password = "1234";
        String name = "하승완";
        String address1 = "서울특별시 강남구 도곡로 117";
        String address2 = "12층";
        String zipcode = "06253";

        Member member = new Member(
                Email.of(email),
                Password.of(password),
                name,
                new Address(address1, address2, zipcode));

        Member saveMember = memberRepository.save(member);

        //when
        FindMemberResponse findMember = findMemberService.findMember(saveMember.getId());

        //then
        assertThat(findMember.memberId()).isEqualTo(saveMember.getId());
        assertThat(findMember.name()).isEqualTo(saveMember.getName());
        assertThat(findMember.address1()).isEqualTo(saveMember.getAddress().getAddress1());
        assertThat(findMember.address2()).isEqualTo(saveMember.getAddress().getAddress2());
        assertThat(findMember.zipcode()).isEqualTo(saveMember.getAddress().getZipcode());
        assertThat(findMember.grade()).isEqualTo(MemberGrade.BRONZE);

    }

}