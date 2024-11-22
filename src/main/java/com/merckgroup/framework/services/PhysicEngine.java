package com.merckgroup.framework.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.merckgroup.framework.App;
import com.merckgroup.framework.components.GraphicComponent;
import com.merckgroup.framework.components.PhysicComponent;
import com.merckgroup.framework.entities.Entity;
import com.merckgroup.framework.math.Vector2d;

/**
 * the {@link PhysicEngine} service is dedicated to compute Newton's laws on the
 * active Entities from the {@link EntityManager}.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class PhysicEngine extends AbstractService {

    private EntityManager eMgr;
    private long currentTime = 0;
    private int nbUpdatedObjects = 0;

    public PhysicEngine(App app) {
        super(app);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void init(App app, String[] args) {
        eMgr = (EntityManager) app.getService(EntityManager.class.getSimpleName());
        currentTime = System.currentTimeMillis();
    }

    @Override
    public void process(App app) {
        long previousTime = currentTime;
        nbUpdatedObjects = 0;
        List<Entity> allEntities = collectAllEntities(eMgr.getEntities());
        currentTime = System.currentTimeMillis();
        long elapsed = currentTime - previousTime;
        if (elapsed > 0) {
            allEntities.stream().sorted().forEach(e -> {
                updateEntity(elapsed, e);
                nbUpdatedObjects++;
            });
        }
    }

    private List<Entity> collectAllEntities(Collection<Entity> entities) {
        List<Entity> ets = new ArrayList<>();
        ets.addAll(entities);
        entities.forEach(e -> collectAllEntities(e.getChildren()));
        return ets;
    }

    private void updateEntity(long elapsed, Entity e) {
        e.update(elapsed);
        if (e.containsComponent(PhysicComponent.class)) {
            PhysicComponent pc = (PhysicComponent) e.getComponent(PhysicComponent.class);

            pc.setAcceleration(new Vector2d().addAll(pc.getForces()));
            pc.setVelocity(pc.getVelocity().add(pc.getAcceleration().multiply(elapsed)));
            pc.setPosition(pc.getPosition().add(pc.getVelocity().multiply(elapsed)));

            pc.getForces().clear();

            // update corresponding GraphicComponent shape for rendering.
            GraphicComponent gc = e.getComponent(GraphicComponent.class);
            gc.update(pc.getPosition(), pc.getSize());
        }
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public void dispose(App app) {
    }

    @Override
    public Map<String, Object> getStats() {
        return Map.of("service.physic.engine.object.counter", nbUpdatedObjects);
    }

}
