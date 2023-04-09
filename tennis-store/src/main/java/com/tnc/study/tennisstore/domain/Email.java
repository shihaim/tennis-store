package com.tnc.study.tennisstore.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Email {
    private String address;

    public static Email of(String address) {
        return new Email(address);
    }
}
