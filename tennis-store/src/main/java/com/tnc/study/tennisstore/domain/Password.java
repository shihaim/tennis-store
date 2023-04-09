package com.tnc.study.tennisstore.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class Password {
    private String value;

    public static Password of(String password) {
        return new Password(password);
    }
}
