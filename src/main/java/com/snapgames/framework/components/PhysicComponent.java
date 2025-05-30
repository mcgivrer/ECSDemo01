package com.snapgames.framework.components;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.snapgames.framework.entities.Entity;
import com.snapgames.framework.entities.Material;
import com.snapgames.framework.math.Vector2d;
import com.snapgames.framework.services.PhysicEngineService;

/**
 * The {@code PhysicComponent} class is responsible for managing the physical attributes
 * and properties of an {@link Entity} within a 2D game or simulation. It allows managing
 * the position, size, velocity, acceleration, forces, material, and bounding box of the entity.
 * This component is specifically designed to be used with a physics engine.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class PhysicComponent implements Component {
    /**
     * {@link Entity}'s position in a 2D world.
     */
    private Vector2d position = new Vector2d();
    /**
     * {@link Entity} size in a 2D world
     */
    private Vector2d size = new Vector2d();

    private PhysicType type = PhysicType.DYNAMIC;
    /**
     * {@link Entity}'s velocity.
     */
    private Vector2d velocity = new Vector2d();
    /**
     * {@link Entity} resulting acceleration.
     */
    private Vector2d acceleration = new Vector2d();
    /**
     * Forces applied to the {@link Entity}.
     */
    private List<Vector2d> forces = new ArrayList<>();
    /**
     * A Material for the Entity used in the Newton's physic computation.
     */
    private Material material;
    /**
     * The mass of the {@link Entity} according to the Newton's physic world.
     */
    private double mass;

    /**
     * Bounding Box for the parent Entity.
     */
    private Rectangle2D bbox = new Rectangle2D.Double();

    /**
     * Createa a blanck {@link PhysicComponent} ready to be used within an
     * {@link Entity} fromp the {@link PhysicEngineService}.
     */
    public PhysicComponent() {
        material = Material.DEFAULT;
        mass = 1.0;
    }

    /**
     * @return Vector2d return the position
     */
    public Vector2d getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public PhysicComponent setPosition(Vector2d position) {
        this.position = position;
        update(position, size);
        return this;
    }

    /**
     * @return Vector2d return the velocity
     */
    public Vector2d getVelocity() {
        return velocity;
    }

    /**
     * @param velocity the velocity to set
     */
    public PhysicComponent setVelocity(Vector2d velocity) {
        this.velocity = velocity;
        return this;
    }

    /**
     * @return Vector2d return the acceleration
     */
    public Vector2d getAcceleration() {
        return acceleration;
    }

    /**
     * @param acceleration the acceleration to set
     */
    public PhysicComponent setAcceleration(Vector2d acceleration) {
        this.acceleration = acceleration;
        return this;
    }

    /**
     * @return double return the mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public PhysicComponent setMass(double mass) {
        this.mass = mass;
        return this;
    }

    /**
     * @return List<Vector2d> return the forces
     */
    public List<Vector2d> getForces() {
        return forces;
    }

    /**
     * @param forces the forces to set
     */
    public void setForces(List<Vector2d> forces) {
        this.forces = forces;
    }

    /**
     * Add a specific Vector2d force to the forces list.
     *
     * @param force
     */
    public PhysicComponent add(Vector2d force) {
        this.forces.add(force);
        return this;
    }

    /**
     * @return Material return the material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @param material the material to set
     */
    public PhysicComponent setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * @return Vector2d return the size
     */
    public Vector2d getSize() {
        return size;
    }

    /**
     * Set the size for this {@link PhysicComponent}.
     *
     * @param w the width for the Entity having this {@link PhysicComponent}.
     * @param h the height for the Entity having this {@link PhysicComponent}.
     * @return the updated {@link PhysicComponent}.
     */
    public PhysicComponent setSize(double w, double h) {
        this.size = new Vector2d(w, h);
        update(position, size);
        return this;
    }

    /**
     * Update the bounding box of this {@link PhysicComponent} according to its
     * position and size.
     *
     * @param position
     * @param size
     */
    private void update(Vector2d position, Vector2d size) {
        this.bbox.setRect(position.getX(), position.getY(), size.getX(), size.getY());
    }

    /**
     * return the BoundingBox for this {@link PhysicComponent}.
     *
     * @return the corresponding Rectangle2D
     */
    public Rectangle2D getBBox() {
        return bbox;
    }

    /**
     * @return PhysicType return the type
     */
    public PhysicType getType() {
        return type;
    }

    /**
     * @param type the type to set
     * @return the updated {@link PhysicComponent}.
     */
    public PhysicComponent setType(PhysicType type) {
        this.type = type;
        return this;
    }

}
