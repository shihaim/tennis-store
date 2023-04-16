package com.tnc.study.tennisstore.framework.web.response;

import java.util.Collections;
import java.util.List;

public interface Content<T> {

    List<T> getContent();

    static <T> Content<T> of(List<T> content) {
        return new ContentImpl<>(content);
    }

    static <T> Content<T> empty() {
        return new ContentImpl<>(Collections.emptyList());
    }
}
