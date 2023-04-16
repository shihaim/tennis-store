package com.tnc.study.tennisstore.api.utils;

import com.tnc.study.tennisstore.framework.domain.CodeModel;
import org.springframework.restdocs.snippet.Attributes;

import java.util.Arrays;
import java.util.List;

public class CodeModelDocumentation {
    public static final String ENUM = "enum";
    public static final String ENUM_VALUES_KEY = "enumValues";

    public static Attributes.Attribute codeModelToAttribute(Class<? extends CodeModel> enumType) {
        if (!enumType.isEnum()) {
            throw new IllegalArgumentException("The given type is not an enum.");
        }

        CodeModel[] constants = enumType.getEnumConstants();

        List<String> codeModels = Arrays.stream(constants)
                .map(e -> "%s - %s".formatted(e.getCode(), e.getMessage()))
                .toList();

        return Attributes.key(ENUM_VALUES_KEY).value(codeModels);
    }
}
