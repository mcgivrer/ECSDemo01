package com.snapgames.framework.entities;

import java.util.ArrayList;
import java.util.List;

import com.snapgames.framework.components.Component;
import com.snapgames.framework.utils.Node;

/**
 * The {@code Entity} class represents a base element in a system designed for managing
 * entities with components and hierarchical relationships. Each {@link Entity} has
 * a unique ID, a UUID, and can contain children and components. The {@link Entity}
 * class supports hierarchical structure creation, component management, and lifecycle
 * handling based on duration and lifetime.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class Entity extends Node {

    List<Component> components = new ArrayList<>();

    private long duration = -1;

    private long lifeTime = 0;

    /**
     * Create a new {@link Entity}.
     */
    public Entity() {
        super();
        this.name = "entity_%s".formatted(id);
    }

    /**
     * Create anew named {@link Entity}.
     *
     * @param name
     */
    public Entity(String name) {
        this();
        this.name = name;
    }

    public Entity add(Component c) {
        components.add(c);
        return this;
    }

    /**
     * Update the current {@link Entity} regarding its duration and lifetime. If
     * duration is differente of -1 lifeTime reach the duration, {@link Entity} is
     * deactivated.
     *
     * @param elapsed the elapsed tie since previous call.
     */
    public void update(double elapsed) {
        if (duration != -1 && duration > 0) {
            if (lifeTime + elapsed < duration) {
                lifeTime += (long) elapsed;
            } else {
                active = false;
            }
        }
    }

    /**
     * Retrieves a component of the specified type from the entity's list of components.
     *
     * @param <T>    the type of the component to retrieve, which must extend {@link Component}.
     * @param class1 the class object representing the type of the component to retrieve.
     * @return the first component in the entity's list of components that matches the specified type.
     * @throws java.util.NoSuchElementException if no component of the specified type is found.
     */
    public <T extends Component> T getComponent(Class<? extends Component> class1) {
        return (T) components.stream().filter(c -> c.getClass().equals(class1)).findFirst().get();
    }

    /**
     * Retrieves the list of components associated with this entity.
     *
     * @return a list of {@link Component} objects associated with this entity.
     */
    public List<Component> getComponents() {
        return components;
    }

    /**
     * Checks if the entity contains a component of the specified class type.
     *
     * @param class1 the class object representing the type of the component to check for.
     * @return {@code true} if a component of the specified class type exists in the entity, {@code false} otherwise.
     */
    public boolean containsComponent(Class<? extends Component> class1) {
        return components.stream().anyMatch(c -> c.getClass().equals(class1));
    }

}
