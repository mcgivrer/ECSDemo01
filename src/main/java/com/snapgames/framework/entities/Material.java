package com.snapgames.framework.entities;

/**
 * 
 */

public class Material {

    public static Material DEFAULT = new Material();

    private String name = "default";
    private double density = 1.0;
    private double elasticity = 1.0;
    private double roughness = 1.0;

    public Material() {
        name = "default";
        density = 1.0;
        elasticity = 1.0;
        roughness = 1.0;
    }

    public Material(String name, double d, double e, double r) {
        this.name = name;
        this.density = d;
        this.elasticity = e;
        this.roughness = r;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return double return the density
     */
    public double getDensity() {
        return density;
    }

    /**
     * @param density the density to set
     */
    public void setDensity(double density) {
        this.density = density;
    }

    /**
     * @return double return the elasticity
     */
    public double getElasticity() {
        return elasticity;
    }

    /**
     * @param elasticity the elasticity to set
     */
    public void setElasticity(double elasticity) {
        this.elasticity = elasticity;
    }

    /**
     * @return double return the roughness
     */
    public double getRoughness() {
        return roughness;
    }

    /**
     * @param roughness the roughness to set
     */
    public void setRoughness(double roughness) {
        this.roughness = roughness;
    }

}
