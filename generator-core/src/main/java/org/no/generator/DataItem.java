package org.no.generator;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DataItem {

    private final Object object;

    public DataItem(Object object) {
        this.object = object;
    }

    public Object get() {
        return object;
    }

    public <T> T get(Class<T> type) {
        return type.cast(object);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(object, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}