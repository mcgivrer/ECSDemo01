package com.snapgames.framework.components;

import java.awt.*;

/**
 * The GaugeComponent class represents a customizable gauge component
 * that displays a value within a specified range.
 * It provides mechanisms to set and retrieve the gauge's value,
 * minimum and maximum limits, and its color.
 * <p>
 * This class implements the Component interface, allowing it to be integrated
 * into a component-based architecture commonly used in game engines or
 * application frameworks.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class GaugeComponent implements Component {
    double value = 0;
    double min = 0;
    double max = 100;

    Color gaugeColor = Color.BLUE;

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

    public GaugeComponent setValue(double value) {
        this.value = value;
        return this;
    }

    public double getMin() {
        return min;
    }

    public GaugeComponent setMin(double min) {
        this.min = min;
        return this;
    }

    public double getMax() {
        return max;
    }

    public GaugeComponent setMax(double max) {
        this.max = max;
        return this;
    }

    public Color getGaugeColor() {
        return gaugeColor;
    }

    public GaugeComponent setGaugeColor(Color c) {
        this.gaugeColor = c;
        return this;
    }

}
