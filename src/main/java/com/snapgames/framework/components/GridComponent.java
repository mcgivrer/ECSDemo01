package com.snapgames.framework.components;

import java.awt.geom.Rectangle2D;

public class GridComponent implements Component{
    private int tileWidth=16;
    private int tileHeight=16;

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
