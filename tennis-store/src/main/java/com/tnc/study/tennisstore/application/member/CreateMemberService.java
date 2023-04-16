package com.tnc.study.tennisstore.application.member;

import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.Email;
import com.tnc.study.tennisstore.domain.Password;
import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateMemberService {

    private final MemberRepository memberRepository;

    public Long signUp(CreateMemberRequest request) {
        Email email = Email.of(request.email());

        checkExistMember(email);

        Password password = Password.of(request.password());
        String name = request.name();
        Address address = new Address(
                request.address1(),
                request.address2(),
                request.zipcode()
        );

        Member member = new Member(
                email,
                password,
                name,
                address
        );

        Member saveMember = memberRepository.save(member);
        return saveMember.getId();
    }

    private void checkExistMember(Email email) {
        boolean exists = memberRepository.existsByEmail(email);
        if (exists) {
            throw new DuplicateMemberException();
        }

    }
}
