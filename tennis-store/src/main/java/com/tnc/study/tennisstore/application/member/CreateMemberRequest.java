package com.tnc.study.tennisstore.application.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateMemberRequest(
        @NotBlank @Email
        String email,
        @NotBlank
        String password,
        @NotBlank
        String name,
        @NotBlank
        String address1,
        @NotBlank
        String address2,
        @NotBlank
        String zipcode
) {

}
