package com.merckgroup.framework.components;

import java.util.ArrayList;
import java.util.List;

import com.merckgroup.framework.Vector2d;
import com.merckgroup.framework.entities.Entity;
import com.merckgroup.framework.entities.Material;

public class PhysicComponent implements Component {
    private Vector2d position = new Vector2d();
    private Vector2d size = new Vector2d();
    private Vector2d velocity = new Vector2d();
    private Vector2d acceleration = new Vector2d();
    private List<Vector2d> forces = new ArrayList<>();
    private Material material;
    private double mass;

    /**
     * Createa a blanck {@link PhysicComponent} ready to used into an
     * {@link Entity}.
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

    public PhysicComponent setSize(double w, double h) {

        this.size = new Vector2d(w, h);
        return this;
    }

}
