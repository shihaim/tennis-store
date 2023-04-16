package com.tnc.study.tennisstore.framework.web.response;

import org.springframework.util.Assert;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContentImpl<T> implements Content<T>, Serializable {
    @Serial
    private static final long serialVersionUID = -3106985663940308429L;

    private final List<T> content = new ArrayList<>();

    public ContentImpl(List<T> content) {
        Assert.notNull(content, "content must no be null");
        this.content.addAll(content);
    }

    @Override
    public List<T> getContent() {
        return content;
    }
}
