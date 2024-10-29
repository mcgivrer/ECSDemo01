package com.merckgroup.framework.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.merckgroup.framework.components.Component;
import com.merckgroup.framework.components.PhysicComponent;

public class Entity {
    /**
     * internale index entity counter.
     */
    private static long index = 0;
    /**
     * Unique internal {@link Entity} ID generated on the auto-incremented index
     * counter.
     */
    private long id = index++;

    private UUID uuid = null;

    /**
     * Name for this {@link Entity} instance.
     */
    private String name;

    /**
     * As soon as adding child, a root tree is created.
     */
    private static Entity root = null;

    List<Component> components = new ArrayList<>();

    /**
     * List of children for this Entity. default is an empty list.
     */
    private List<Entity> children = new ArrayList<>();

    private long duration = -1;

    private long lifeTime = 0;
    private boolean active = true;

    /**
     * Create a new {@link Entity}.
     */
    public Entity() {
        this.uuid = UUID.randomUUID();
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
    public Entity add(Entity child) {
        if (Optional.ofNullable(root).isEmpty()) {
            setRoot(this);
        }
        children.add(child);
        return this;
    }

    public Entity add(Component c) {
        components.add(c);
        return this;
    }

    /**
     * Set the root tree for those {@link Entity}.
     * 
     * @param r
     */
    public static void setRoot(Entity r) {
        root = r;
    }

    /**
     * retrieve all the current children of this {@link Entity}.
     * 
     * @return a collection of all child entities.
     */
    public Collection<Entity> getChildren() {
        return children;
    }

    /**
     * Set the current {@link Entity} active.
     * 
     * @param a the active value.
     * @return the updated {@link Entity}.
     */
    public Entity setActive(boolean a) {
        this.active = a;
        return this;
    }

    /**
     * Update the current {@link Entity} regarding its duration and lifetime. If
     * duration is differente of -1 lifeTime reach the duration, {@link Entity} is
     * deactivated.
     * 
     * @param elapsed the elapsed tie since previous call.
     */
    public void update(long elapsed) {
        if (duration != -1 && duration > 0) {
            if (lifeTime + elapsed < duration) {
                lifeTime += elapsed;
            } else {
                active = false;
            }
        }
    }

    public Component getComponent(Class<? extends Component> class1) {
        return components.stream().filter(c -> c.getClass().equals(class1)).findFirst().get();
    }

    public List<Component> getComponents() {
        return components;
    }

    public boolean containsComponent(Class<PhysicComponent> class1) {
        return components.stream().anyMatch(c -> c.getClass().equals(class1));
    }
}
