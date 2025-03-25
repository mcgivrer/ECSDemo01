package com.snapgames.framework.entities;

import java.awt.geom.Rectangle2D;

import com.snapgames.framework.components.PhysicComponent;
import com.snapgames.framework.math.Vector2d;
import com.snapgames.framework.services.PhysicEngineService;

/**
 * The {@link World} class is used by the {@link PhysicEngineService} service to
 * keep all entities from a {@link com.snapgames.framework.scenes.Scene} into a
 * limited area and apply some common forces on any contained {@link Entity}.
 *
 * @author Frédéric Delorme
 * @since 0.0.2
 */
public class World extends Entity {

    /**
     * The {@code playArea} represents the rectangular area that defines the boundaries
     * or limits within which entities in the {@link World} can exist or interact.
     * It is primarily used by physics components for managing spatial constraints
     * and ensuring entities remain within the defined playable region.
     */
    private Rectangle2D playArea = new Rectangle2D.Double();
    /**
     * The {@code gravity} vector defines the gravitational force applied within the
     * {@link World}. This force impacts all entities contained within the {@link World},
     * simulating a constant acceleration, typically directed downwards, to mimic real-world gravity.
     */
    private Vector2d gravity = new Vector2d();

    /**
     * Constructs a new {@code World} instance with the specified gravity vector and play area.
     * This constructor initializes the world by adding a {@link PhysicComponent} containing the
     * specified gravity force and the play area dimensions.
     *
     * @param gravity the gravitational force to be applied to entities within the world,
     *                represented as a {@link Vector2d}.
     * @param aDouble the rectangular play area boundary for the world, represented as a
     *                {@link Rectangle2D.Double}.
     */
    public World(Vector2d gravity, Rectangle2D.Double aDouble) {
        this.components.add(new PhysicComponent().add(gravity).setPosition(new Vector2d()).setSize(aDouble.getWidth(),
                aDouble.getHeight()));

    }

    /**
     * Updates the gravitational force acting within the {@code World}.
     * This method clears all existing forces in the {@link PhysicComponent}
     * and sets the new gravitational force.
     *
     * @param g the new gravitational force to be applied, represented as a {@link Vector2d}.
     * @return the updated instance of {@code World} with the newly set gravity.
     */
    public World setGravity(Vector2d g) {
        PhysicComponent pc = getComponent(PhysicComponent.class);
        this.gravity = g;
        pc.getForces().clear();
        pc.add(g);
        return this;
    }

    /**
     * Sets the playable area for the {@code World} instance and updates the associated
     * {@link PhysicComponent} with the new dimensions and default position.
     *
     * @param playArea the rectangular area defining the boundaries within which entities
     *                 can exist or interact, represented as a {@link Rectangle2D}.
     * @return the updated {@code World} instance with the newly set play area.
     */
    public World setPlayArea(Rectangle2D playArea) {
        PhysicComponent pc = getComponent(PhysicComponent.class);
        this.playArea = playArea;
        pc.setPosition(new Vector2d()).setSize(playArea.getWidth(), playArea.getHeight());
        return this;
    }

    /**
     * Retrieves the current playable area of the {@code World}.
     *
     * @return the rectangular area defining the boundaries within which entities can exist or interact, represented as a {@link Rectangle2D}.
     */
    public Rectangle2D getPlayArea() {
        return this.playArea;
    }

    /**
     * Retrieves the gravitational force currently acting within the {@code World}.
     *
     * @return the gravitational force, represented as a {@link Vector2d}.
     */
    public Vector2d getGravity() {
        return this.gravity;
    }

}
