package com.snapgames.framework.components;

public class GaugeComponent implements Component {
    double value = 0;
    double min = 0;
    double max = 100;

    public GaugeComponent() {
    }

    public GaugeComponent(double value, double min, double max) {
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }
}
