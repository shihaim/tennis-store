package com.tnc.study.tennisstore.application.login;

import com.tnc.study.tennisstore.domain.Email;
import com.tnc.study.tennisstore.domain.admin.Admin;
import com.tnc.study.tennisstore.domain.admin.AdminRepository;
import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.member.MemberRepository;
import com.tnc.study.tennisstore.framework.security.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginServiceImpl implements LoginService {

    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername. username: {}", username);

        // Admin 계정 확인
        Optional<Admin> optionalAdmin = adminRepository.findById(username);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();

            List<SimpleGrantedAuthority> roles = List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_HSW")
            );

            return new User(admin.getId(), admin.getPassword().getValue(), roles);
        }

        Optional<Member> optionalMember = memberRepository.findByEmail(Email.of(username));
        Member findMember = optionalMember.orElseThrow(() -> new UsernameNotFoundException(username));

        return new User(
                findMember.getEmail().getAddress(),
                findMember.getPassword().getValue(),
                !findMember.isWithdrawal(),
                true,
                true,
                true,
                new ArrayList<>()
        );
    }
}
