package com.merckgroup.framework.services;

import java.awt.geom.Rectangle2D;
import java.util.*;

import com.merckgroup.framework.App;
import com.merckgroup.framework.components.GraphicComponent;
import com.merckgroup.framework.components.PhysicComponent;
import com.merckgroup.framework.components.TargetComponent;
import com.merckgroup.framework.entities.Camera;
import com.merckgroup.framework.entities.Entity;
import com.merckgroup.framework.entities.World;
import com.merckgroup.framework.math.Vector2d;

/**
 * the {@link PhysicEngineService} service is dedicated to compute Newton's laws on the
 * active Entities from the {@link EntityManagerService}.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class PhysicEngineService extends AbstractService {

    private EntityManagerService eMgr;
    private long currentTime = 0;
    private int nbUpdatedObjects = 0;

    private World world = new World(
            new Vector2d(0, -0.981),
            new Rectangle2D.Double(0, 0, 320, 200));

    public PhysicEngineService(App app) {
        super(app);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void init(App app, String[] args) {
        ConfigurationService config = app.getService(ConfigurationService.class.getSimpleName());
        world.setGravity(config.getValue("app.physic.world.gravity"));
        world.setPlayArea(config.getValue("app.physic.world.play.area"));
        eMgr = app.getService(EntityManagerService.class.getSimpleName());
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
            SceneManagerService scnMgr = app.getService(SceneManagerService.class.getSimpleName());
            if (Optional.ofNullable(scnMgr.getCurrentScene().getCamera()).isPresent()) {
                Camera cam = scnMgr.getCurrentScene().getCamera();
                processCamera(cam);
            }
        }
    }

    private void processCamera(Camera cam) {
        PhysicComponent camPC = cam.getComponent(PhysicComponent.class);
        TargetComponent camTC = cam.getComponent(TargetComponent.class);
        PhysicComponent targetPC = camTC.getTarget().getComponent(PhysicComponent.class);

        camPC.setPosition(targetPC.getPosition().substract(camPC.getPosition()).multiply(0.5));
    }

    /**
     * Retrieve all entities from the Entity tree hierarchy.
     *
     * @param entities list of entities to explore and parse
     * @return a linear list of all contained Entity (parent and child)
     */
    private List<Entity> collectAllEntities(Collection<Entity> entities) {
        List<Entity> ets = new ArrayList<>();
        ets.addAll(entities);
        entities.forEach(e -> collectAllEntities(e.getChildren()));
        return ets;
    }

    /**
     * Apply the physic newton's laws on the {@link Entity}.
     *
     * @param elapsed the elapsed time since previous call (in milliseconds)
     * @param e       the {@link Entity} to be updated.
     */
    private void updateEntity(long elapsed, Entity e) {
        e.update(elapsed);
        if (e.containsComponent(PhysicComponent.class)) {
            PhysicComponent pc = (PhysicComponent) e.getComponent(PhysicComponent.class);

            applyWorldRules(pc, world);

            pc.setAcceleration(new Vector2d().addAll(pc.getForces()));
            pc.setVelocity(pc.getVelocity().add(pc.getAcceleration().multiply(0.5 * elapsed)));
            pc.setPosition(pc.getPosition().add(pc.getVelocity().multiply(elapsed)));

            pc.getForces().clear();
            contrainToWorldArea(pc, world);

            // apply Material roughness on velocity
            pc.setVelocity(pc.getVelocity().multiply(pc.getMaterial().getRoughness()));

            // update the corresponding Entity's GraphicComponent shape for rendering.
            GraphicComponent gc = e.getComponent(GraphicComponent.class);
            gc.update(pc.getPosition(), pc.getSize());
        }
    }

    /**
     * Apply all {@link World} rules: apply all world forces on any contained {@link Entity}'s {@link PhysicComponent}.
     *
     * @param pc    the {@link PhysicComponent} from the {@link Entity} to be updated
     * @param world the {@link World} instance to take in account.
     */
    private void applyWorldRules(PhysicComponent pc, World world) {
        PhysicComponent worldPC = world.getComponent(PhysicComponent.class);
        if (!worldPC.getBBox().contains(pc.getBBox())) {

            // apply all World forces to the PhysicComponent.
            worldPC.getForces().forEach(pc::add);
        }
    }

    /**
     * Keep all {@link Entity}'s {@link PhysicComponent} position into the {@link World}'s play area.
     *
     * @param pc    the {@link PhysicComponent} from the {@link Entity} to be updated
     * @param world the {@link World} instance to take in account.
     */
    private void contrainToWorldArea(PhysicComponent pc, World world) {
        PhysicComponent worldPC = world.getComponent(PhysicComponent.class);
        Vector2d position = pc.getPosition();
        Vector2d size = pc.getSize();

        Rectangle2D worldRect = worldPC.getBBox();

        if (position.x < worldRect.getX()) {
            position.x = worldRect.getX();
        }
        if (position.x + size.x > worldRect.getX() + worldRect.getWidth()) {
            position.x = worldRect.getX() + worldRect.getWidth() - size.x;
        }
        if (position.y < worldRect.getY()) {
            position.y = worldRect.getY();
        }
        if (position.y + size.y > worldRect.getY() + worldRect.getHeight()) {
            position.y = worldRect.getY() + worldRect.getHeight() - size.y;
        }
        pc.setPosition(position);
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
