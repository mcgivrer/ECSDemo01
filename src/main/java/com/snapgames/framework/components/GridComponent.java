package com.snapgames.framework.components;

import java.awt.geom.Rectangle2D;

/**
 * The {@link GridComponent} class represents a component designed to manage
 * a rectangular grid, with each grid cell defined by a specific tile width
 * and tile height. This component allows configuration of the tile dimensions
 * and an associated bounding box for the grid.
 * <p>
 * This class implements the Component interface, making it compatible with
 * component-based architecture systems such as game engines or application
 * frameworks.
 * <p>
 * Key Responsibilities:
 * - Define and expose the width and height of individual grid tiles.
 * - Handle a rectangular bounding box representing an area for the grid.
 * - Provide fluent setter methods to enable easy configuration of attributes.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class GridComponent implements Component {
    private int tileWidth = 16;
    private int tileHeight = 16;

    private Rectangle2D box;

    public GridComponent(int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public Rectangle2D getBox() {
        return box;
    }

    public GridComponent setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
        return this;
    }

    public GridComponent setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
        return this;
    }

    public GridComponent setBox(Rectangle2D box) {
        this.box = box;
        return this;
    }
}
