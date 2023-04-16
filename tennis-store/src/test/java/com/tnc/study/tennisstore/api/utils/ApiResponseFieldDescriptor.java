package com.tnc.study.tennisstore.api.utils;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;

import java.util.Arrays;

public class ApiResponseFieldDescriptor {

    public static FieldDescriptor[] apiResponseFieldDescriptors() {
        return Arrays.asList(
                PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("Response Code"),
                PayloadDocumentation.fieldWithPath("type").type(JsonFieldType.STRING).description("Response Type"),
                PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("Response Message")
        ).toArray(new FieldDescriptor[0]);
    }
}
