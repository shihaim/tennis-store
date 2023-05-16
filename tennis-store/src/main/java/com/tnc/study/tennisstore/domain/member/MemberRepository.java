package com.tnc.study.tennisstore.domain.member;

import com.tnc.study.tennisstore.domain.Email;
import com.tnc.study.tennisstore.domain.member.query.MemberCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    boolean existsByEmail(Email email);

    Optional<Member> findByEmail(Email email);
}
