package com.snapgames.framework.components;

import com.snapgames.framework.entities.Entity;

/**
 * The {@code TargetComponent} class is a component used to associate a specific target
 * and a tween factor with an entity in a component-based system. This allows for the
 * representation of an entity's interaction or connection with another entity (target)
 * and defines a tween factor for smooth interpolation or transitions.
 * <p>
 * Responsibilities:
 * - Store a reference to the target entity.
 * - Manage a tween factor for interpolation between states or animations.
 * - Provide getter and fluent setter methods for configuration.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class TargetComponent implements Component {
    private Entity target = null;
    private double tweenFactor = 1.0;

    public TargetComponent() {

    }

    public Entity getTarget() {
        return target;
    }

    public double getTweenFactor() {
        return tweenFactor;
    }


    public TargetComponent setTarget(Entity target) {
        this.target = target;
        return this;
    }

    public TargetComponent setTweenFactor(double tweenFactor) {
        this.tweenFactor = tweenFactor;
        return this;
    }
}
