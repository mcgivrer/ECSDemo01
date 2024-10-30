package com.merckgroup.framework.entities;

import com.merckgroup.framework.components.PhysicComponent;
import com.merckgroup.framework.math.Vector2d;
import com.merckgroup.framework.services.PhysicEngineService;

import java.awt.geom.Rectangle2D;

/**
 * The {@link World} class is used by the {@link PhysicEngineService} service to keep all entities from a
 * {@link com.merckgroup.framework.scenes.Scene} into a limited area and apply some common forces on any contained {@link Entity}.
 *
 * @author Frédéric Delorme
 * @since 0.0.2
 */
public class World extends Entity {

    public World(Vector2d gravity, Rectangle2D.Double aDouble) {
        this.components.add(new PhysicComponent()
                .add(gravity)
                .setPosition(new Vector2d())
                .setSize(aDouble.getWidth(), aDouble.getHeight()));
    }

    public void setGravity(Vector2d g) {
        PhysicComponent pc = getComponent(PhysicComponent.class);
        pc.getForces().clear();
        pc.add(g);
    }

    public void setPlayArea(Rectangle2D playArea) {
        PhysicComponent pc = getComponent(PhysicComponent.class);
        pc.setPosition(new Vector2d())
                .setSize(playArea.getWidth(), playArea.getHeight());
    }
}
