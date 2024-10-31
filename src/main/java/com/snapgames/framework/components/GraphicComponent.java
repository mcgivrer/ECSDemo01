package com.snapgames.framework.components;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import com.snapgames.framework.entities.Camera;
import com.snapgames.framework.entities.Entity;
import com.snapgames.framework.math.Vector2d;

/**
 * the {@link GraphicComponent} class is a {@link Component} instance to deliver
 * graphics properties for drawing an
 * {@link com.snapgames.framework.entities.Entity}.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */

public class GraphicComponent implements Component {
    private Color color;
    private Color fillColor;
    private Shape shape;

    private boolean stickToViewport = false;

    /**
     * initialize the {@link GraphicComponent}.
     */
    public GraphicComponent() {
        // nothing specific for now.
    }

    /**
     * @return Color return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public GraphicComponent setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * @return Color return the fillColor
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     * @param fillColor the fillColor to set
     */
    public GraphicComponent setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    /**
     * @return Shape return the shape
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * @param shape the shape to set
     */
    public GraphicComponent setShape(Shape shape) {
        this.shape = shape;
        return this;
    }

    public void update(Vector2d position, Vector2d size) {
        this.shape = new Rectangle2D.Double(position.x, position.y, size.x, size.y);
    }

    /**
     * @return boolean defining if the parent {@link Entity} is sticked to the
     *         active {@link Camera} viewport.
     */
    public boolean isStickToViewport() {
        return stickToViewport;
    }

    /**
     * @param flag boolean defines if the parent {@link Entity} is sticked to the
     *                 active {@link Camera} viewport.
     * @return the updated {@link GraphicComponent}.
     */
    public GraphicComponent setStickToViewport(boolean flag) {
        this.stickToViewport = flag;
        return this;
    }
}
