package com.snapgames.framework.services;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.snapgames.framework.App;
import com.snapgames.framework.entities.Entity;

/**
 * The EntityManagerService class is responsible for managing a collection of {@link Entity}
 * instances within an application. This is a concrete implementation of {@link AbstractService},
 * designed to extend its functionality for managing entities.
 * <p>
 * The service provides methods for adding, retrieving, and manipulating {@link Entity}
 * objects, as well as generating statistics about the entities it manages.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */

public class EntityManagerService extends AbstractService {

    /**
     * A thread-safe collection of {@link Entity} instances, indexed by their unique names.
     * This map provides a mechanism for managing and retrieving {@link Entity} objects by name.
     * The underlying implementation uses a {@link ConcurrentHashMap} to ensure thread safety,
     * enabling concurrent access and modification of entities.
     */
    private Map<String, Entity> entities = new ConcurrentHashMap<>();

    /**
     * Constructs a new EntityManagerService instance. This constructor initializes the service
     * with the provided {@link App} instance and clears the internal collection of entities.
     *
     * @param app the application context that this service will be associated with. It provides
     *            shared resources and configuration required by the service.
     */
    public EntityManagerService(App app) {
        super(app);
        entities.clear();
    }

    @Override
    public String getName() {
        return EntityManagerService.class.getSimpleName();
    }

    @Override
    public void init(App app, String[] args) {
    }

    @Override
    public void process(App app) {
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public void dispose(App app) {

    }

    /**
     * Generates a map with statistical data about the managed entities.
     * <p>
     * The map includes the following key-value pairs:
     * - "service.entity.manager.counter.entities": the total number of entities.
     * - "service.entity.manager.counter.active": the count of active entities.
     *
     * @return a map containing statistics about the entities, specifically the total count
     * and the count of active entities.
     */
    @Override
    public Map<String, Object> getStats() {
        return Map.of("service.entity.manager.counter.entities", entities.values().size(),
                "service.entity.manager.counter.active", entities.values().stream().filter(Entity::isActive).count());
    }

    /**
     * Adds the specified {@link Entity} to the collection of managed entities.
     *
     * @param e the {@link Entity} to be added to the collection. The entity is indexed internally
     *          by its unique name, which is retrieved using {@link Entity#getName()}.
     */
    public void add(Entity e) {
        this.entities.put(e.getName(), e);
    }

    /**
     * Retrieves an {@link Entity} by its name from the collection of managed entities.
     *
     * @param name the name of the {@link Entity} to retrieve.
     * @return the {@link Entity} associated with the specified name, or {@code null} if no matching entity is found.
     */
    public Entity get(String name) {
        return this.entities.get(name);
    }

    /**
     * Retrieves a collection of all managed {@link Entity} instances.
     *
     * @return a {@link Collection} of {@link Entity} objects currently managed by this service.
     */
    public Collection<Entity> getEntities() {
        return entities.values();
    }

    /**
     * Adds a collection of {@link Entity} objects to the collection of managed entities.
     *
     * @param entitiesList a {@link Collection} of {@link Entity} objects to be added. Each entity
     *                     in the collection is added individually using the {@link #add(Entity)} method.
     */
    public void addAll(Collection<Entity> entitiesList) {
        entitiesList.forEach(e -> add(e));
    }

}
