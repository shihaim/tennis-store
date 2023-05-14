package com.tnc.study.tennisstore;

import com.querydsl.core.annotations.QueryProjection;
import com.tnc.study.tennisstore.domain.member.MemberGrade;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {
    private String name;
    private String fullAddress;
    private MemberGrade grade;
    private String gradeMessage;

    @QueryProjection
    public MemberDto(String name, String fullAddress, MemberGrade grade) {
        this.name = name;
        this.fullAddress = fullAddress;
        this.grade = grade;
        this.gradeMessage = grade.getMessage();
    }
}