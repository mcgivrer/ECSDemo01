package com.snapgames.framework.components;

/**
 * The PriorityComponent class represents a component that allows assigning
 * a priority value to an entity or a system within a component-based architecture.
 * Priority can be useful for ordering operations or managing execution precedence,
 * such as systems or behaviors in a game or application framework.
 *
 * This class implements the Component interface, signifying that it can be used
 * as a building block in the framework's component-based design.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class PriorityComponent implements Component {
    private int priority = 0;

    public PriorityComponent() {

    }

    public PriorityComponent setPriority(int p) {
        this.priority = p;
        return this;
    }

    public int getPriority() {
        return this.priority;
    }


}
