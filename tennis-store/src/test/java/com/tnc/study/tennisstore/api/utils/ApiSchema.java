package com.tnc.study.tennisstore.api.utils;

import com.epages.restdocs.apispec.Schema;

public class ApiSchema {
    public static Schema apiResponseSchema() {
        return Schema.schema("ApiResponse");
    }

    public static Schema apiResponseDetailsSchema() {
        return Schema.schema("ApiResponseDetail");
    }
}
