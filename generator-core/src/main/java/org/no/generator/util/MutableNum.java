package org.no.generator.util;

public class MutableNum {

    private double min;

    private double max;

    private double value;

    public MutableNum(double value) {
        this.value = value;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public MutableNum() {
        super();
    }

    public double getAndAdd(double v) {
        double tmp = value;
        value += v;
        if (value >= max) {
            value += min - max;
        }
        return tmp;
    }

}
