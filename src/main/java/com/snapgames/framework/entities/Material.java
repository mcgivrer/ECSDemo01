package com.snapgames.framework.entities;

/**
 * The Material class represents a physical material with specific properties
 * such as name, density, elasticity, and roughness. This class provides a way
 * to define these properties and manage their values through getter and setter methods.
 * A default material instance is also provided with pre-defined values.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */

public class Material {

    /**
     * Default instance of the {@code Material} class with pre-defined properties.
     * This instance represents a material with default values for its attributes:
     * - Name: "default"
     * - Density: 1.0
     * - Elasticity: 1.0
     * - Roughness: 1.0
     * <p>
     * It provides a ready-to-use reference for a generic material configuration.
     */
    public static Material DEFAULT = new Material();

    /**
     * The name of the material instance. This property defines a unique identifier
     * or label for the material and is used for distinguishing different material
     * types. The default value is "default".
     */
    private String name = "default";
    /**
     * Represents the density of the material.
     * The density is a measure of mass per unit volume
     * and is used to define the material's physical property of mass distribution.
     * The default value is set to 1.0.
     */
    private double density = 1.0;
    /**
     * Represents the elasticity property of a material.
     * <p>
     * Elasticity defines the ability of a material to resist deformation and
     * return to its original shape when external forces are applied and removed.
     * Higher values indicate greater elasticity, meaning the material will more
     * readily return to its original state after deformation.
     * <p>
     * The default value is set to 1.0, which represents a standard level
     * of elasticity for a material.
     */
    private double elasticity = 1.0;
    /**
     * Represents the roughness property of a material.
     * Roughness is a measure of the texture or surface irregularity
     * of the material, where higher values indicate a rougher surface.
     * This value is used to simulate physical and aesthetic properties
     * of the material.
     */
    private double roughness = 1.0;

    /**
     * Constructs a new Material object with default property values.
     * The default values are:
     * - name: "default"
     * - density: 1.0
     * - elasticity: 1.0
     * - roughness: 1.0
     */
    public Material() {
        name = "default";
        density = 1.0;
        elasticity = 1.0;
        roughness = 1.0;
    }

    /**
     * Constructs a new Material object with the specified property values.
     *
     * @param name the name of the material
     * @param d    the density of the material
     * @param e    the elasticity of the material
     * @param r    the roughness of the material
     */
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
