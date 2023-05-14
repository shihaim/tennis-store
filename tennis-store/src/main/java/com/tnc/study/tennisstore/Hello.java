package com.tnc.study.tennisstore;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@SequenceGenerator(
        name = "HELLO_SEQ_GENERATOR",
        sequenceName = "HELLO_SEQ",
        initialValue = 1, allocationSize = 50
)
public class Hello {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HELLO_SEQ_GENERATOR")
    private Long id;
}
