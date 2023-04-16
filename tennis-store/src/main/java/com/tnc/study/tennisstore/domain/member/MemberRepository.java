package com.tnc.study.tennisstore.domain.member;

import com.tnc.study.tennisstore.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(Email email);
}
