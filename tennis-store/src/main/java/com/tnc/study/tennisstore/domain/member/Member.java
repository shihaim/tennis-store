package com.tnc.study.tennisstore.domain.member;

import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.BooleanToYNConverter;
import com.tnc.study.tennisstore.domain.Email;
import com.tnc.study.tennisstore.domain.Password;
import com.tnc.study.tennisstore.domain.order.Order;
import com.tnc.study.tennisstore.framework.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Embedded
    @AttributeOverride(name = "address", column = @Column(name = "email", unique = true, nullable = false))
    private Email email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password", nullable = false))
    private Password password;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private Address address;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean withdrawal;

    @Enumerated(EnumType.STRING)
    private MemberGrade grade;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public Member(Email email, Password password, String name, Address address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.withdrawal = false;
        this.grade = MemberGrade.BRONZE;
    }

    public void changeMemberInfo(String name, Address address, MemberGrade grade) {
        this.name = name;
        this.address = address;
        this.grade = grade;
    }

    public String initializePassword() {
        String randomPassword = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

        this.password = Password.of(randomPassword);

        return randomPassword;
    }
}
