package com.snapgames.framework.utils;

import com.snapgames.framework.entities.Entity;

import java.util.*;

/**
 * Represents a node in a hierarchical structure with support for parent-child relationships,
 * unique identification, and active state management.
 *
 * @author Frédéric Delorme
 * @since 1.0.5
 */
public class Node {
    /**
     * internale index entity counter.
     */
    private static long index = 0;
    /**
     * As soon as adding child, a root tree is created.
     */
    private static Node root = null;
    /**
     * Unique internal {@link Entity} ID generated on the auto-incremented index
     * counter.
     */
    protected long id = index++;
    protected UUID uuid = null;
    /**
     * Name for this {@link Entity} instance.
     */
    protected String name;
    protected boolean active = true;
    /**
     * List of children for this Entity. default is an empty list.
     */
    private List<Node> children = new ArrayList<>();

    public Node() {
        this.uuid = UUID.randomUUID();
    }

    /**
     * Set the root tree for those {@link Entity}.
     *
     * @param r
     */
    public static void setRoot(Node r) {
        root = r;
    }

    /**
     * retrieve the unique Id for this {@link Entity}.
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * Retrieve the unique UUID for thie {@link Entity}.
     *
     * @return
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Retrieve the name for this {@link Entity}.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Add a child {@link Entity} to this Enttiy. Initialize root tree node if
     * required.
     *
     * @param child the child {@link Entity} to be added.
     * @return the updated Entity as a fluent API.
     */
    public Node add(Node child) {
        if (Optional.ofNullable(root).isEmpty()) {
            Node.setRoot(this);
        }
        children.add(child);
        return this;
    }

    /**
     * retrieve all the current children of this {@link Entity}.
     *
     * @return a collection of all child entities.
     */
    public Collection<Node> getChildren() {
        return children;
    }

    /**
     * Set the current {@link Entity} active.
     *
     * @param a the active value.
     * @return the updated {@link Entity}.
     */
    public Node setActive(boolean a) {
        this.active = a;
        return this;
    }


    /**
     * Checks if the current {@link Entity} is active.
     *
     * @return {@code true} if the entity is active, {@code false} otherwise.
     */
    public boolean isActive() {
        return active;
    }
}
