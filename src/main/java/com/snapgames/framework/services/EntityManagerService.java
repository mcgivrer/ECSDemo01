package com.snapgames.framework.services;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.snapgames.framework.App;
import com.snapgames.framework.entities.Entity;

/**
 *
 */

public class EntityManagerService extends AbstractService {

    private Map<String, Entity> entities = new ConcurrentHashMap<>();

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

    @Override
    public Map<String, Object> getStats() {
        return Map.of("service.entity.manager.counter.entities", entities.values().size(),
                "service.entity.manager.counter.active", entities.values().stream().filter(Entity::isActive).count());
    }

    public void add(Entity e) {
        this.entities.put(e.getName(), e);
    }

    public Entity get(String name) {
        return this.entities.get(name);
    }

    public Collection<Entity> getEntities() {
        return entities.values();
    }

    public void addAll(Collection<Entity> entitiesList) {
        entitiesList.forEach(e -> add(e));
    }

}
