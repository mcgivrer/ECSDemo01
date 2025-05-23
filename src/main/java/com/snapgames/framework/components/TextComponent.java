package com.snapgames.framework.components;

import com.snapgames.framework.entities.Entity;

import java.awt.*;

/**
 * The TextComponent class provide all the required elements to manage and
 * display some text on screen.
 *
 * @author Frédéric Delorme
 * @since 0.0.2
 */

public class TextComponent implements Component {
    private Font textFont;
    private String text;
    private Object value;
    private Color textColor = Color.WHITE;

    public TextComponent() {
        text = "";
        value = null;
    }

    /**
     * @return Font return the textFont
     */
    public Font getTextFont() {
        return textFont;
    }

    /**
     * @param textFont the textFont to set
     * @return this {@link TextComponent} updated.
     */
    public TextComponent setTextFont(Font textFont) {
        this.textFont = textFont;
        return this;
    }

    /**
     * @return String return the text or the formatted value.
     */
    public String getText() {
        if (text.contains("%")) {
            return text.formatted(value);
        }
        return text;
    }

    /**
     * @param text the text to set
     * @return this {@link TextComponent} updated.
     */
    public TextComponent setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * @return Object return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     * @return this {@link TextComponent} updated.
     */
    public TextComponent setValue(Object value) {
        this.value = value;
        return this;
    }

    public TextComponent setTextColor(Color color) {
        this.textColor = color;
        return this;
    }

    public Color getTextColor() {
        return this.textColor;
    }
}
