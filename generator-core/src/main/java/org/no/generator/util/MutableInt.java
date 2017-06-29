package org.no.generator.util;

public class MutableInt {

    private long min;

    private long max;

    private long value;

    public MutableInt(long value) {
        this.value = value;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public MutableInt() {
        super();
    }

    public long getAndAdd(long v) {
        long tmp = value;
        value += v;
        if (value >= max) {
            value += min - max;
        }
        return tmp;
    }

}

