package com.snapgames.framework.services;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.snapgames.framework.App;
import com.snapgames.framework.components.GraphicComponent;
import com.snapgames.framework.components.PhysicComponent;
import com.snapgames.framework.components.TargetComponent;
import com.snapgames.framework.entities.Camera;
import com.snapgames.framework.entities.Entity;
import com.snapgames.framework.entities.World;
import com.snapgames.framework.math.Vector2d;

/**
 * PhysicEngineService is responsible for managing and updating the physics
 * state of the application's world and entities. It handles physics-related
 * computations such as Newtonian mechanics, collision constraints, and applies
 * world rules (e.g., gravity, boundaries). This service operates within the
 * application's lifecycle and integrates with other services like the
 * EntityManagerService and SceneManagerService.
 *
 * @author Frédéric Delorme
 * @since 0.0.@
 */
public class PhysicEngineService extends AbstractService {

    /**
     * The EntityManagerService instance responsible for managing entities
     * in the application. This variable facilitates access to the core
     * entity management functionality, including operations such as adding,
     * retrieving, updating, and removing entities.
     */
    private EntityManagerService eMgr;
    /**
     * The `currentTime` variable represents the last recorded timestamp
     * in milliseconds. It is used internally to track and calculate time-dependent
     * operations, such as processing physics updates or handling elapsed time
     * calculations.
     */
    private long currentTime = 0;
    /**
     * Represents the number of objects that have been updated during the physics
     * processing in the {@link PhysicEngineService}.
     * <p>
     * This variable is used to track and count the total number of {@link Entity}
     * objects that were successfully updated within a single processing cycle of
     * the physics engine.
     */
    private int nbUpdatedObjects = 0;

    /**
     * The {@code world} variable represents the instance of the {@link World} used in the
     * {@link PhysicEngineService} to simulate physical interactions, apply forces such as
     * gravity, and manage the spatial constraints within a defined play area for entities.
     * <p>
     * This particular instance initializes the {@link World} with a default gravity vector
     * of {@code (0, -0.981)}, simulating a downward gravitational force, and a play area
     * defined by a {@link Rectangle2D.Double} with width 320 and height 200.
     */
    private World world = new World(new Vector2d(0, -0.981), new Rectangle2D.Double(0, 0, 320, 200));


    static double cumulated = 0;


    double UPS = 120.0;

    /**
     * Creates an instance of the PhysicEngineService to manage physics-related
     * computations and updates for the application.
     *
     * @param app the application instance that this service belongs to.
     */
    public PhysicEngineService(App app) {
        super(app);
    }

    /**
     * Retrieves the name of the service.
     *
     * @return the simple name of the class implementing the service.
     */
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Initializes the PhysicEngineService by setting up the necessary configuration and dependencies.
     *
     * @param app  the main application instance providing access to services and configuration.
     * @param args an array of command-line arguments or additional initialization parameters.
     */
    @Override
    public void init(App app, String[] args) {
        ConfigurationService config = app.getService(ConfigurationService.class.getSimpleName());
        world.setGravity(config.getValue("app.physic.world.gravity"));
        world.setPlayArea(config.getValue("app.physic.world.play.area"));
        eMgr = app.getService(EntityManagerService.class.getSimpleName());
        UPS = config.getValue("app.physics.update.rate");
        currentTime = System.currentTimeMillis();
    }

    /**
     * Processes the physics-related updates for the application, including entity
     * behaviors and camera adjustments, based on elapsed time since the last update.
     *
     * @param app the main application instance providing access to services, resources,
     *            and the scene manager for physics updates.
     */
    @Override
    public void process(App app) {
        long previousTime = currentTime;
        nbUpdatedObjects = 0;
        List<Entity> allEntities = collectAllEntities(eMgr.getEntities());
        currentTime = System.currentTimeMillis();
        double elapsed = currentTime - previousTime;
        cumulated += elapsed;
        if (cumulated > 1000.0 / UPS) {
            allEntities.stream().forEach(e -> {
                updateEntity(elapsed, e);
                nbUpdatedObjects++;
            });
            SceneManagerService scnMgr = app.getService(SceneManagerService.class.getSimpleName());
            if (Optional.ofNullable(scnMgr.getCurrentScene()).isPresent()
                    && Optional.ofNullable(scnMgr.getCurrentScene().getCamera()).isPresent()) {
                Camera cam = scnMgr.getCurrentScene().getCamera();
                processCamera(cam, elapsed);
            }
            cumulated = 0;
        }
    }

    /**
     * Adjusts the camera's position to smoothly follow its target entity based on the
     * target's position, size, and the defined tweening factor. This computation also
     * considers the elapsed time to ensure smooth and continuous motion.
     *
     * @param cam     the {@link Camera} instance whose position is being adjusted.
     * @param elapsed the time elapsed since the last frame or update, used to calculate
     *                the interpolation for smooth motion.
     */
    private void processCamera(Camera cam, double elapsed) {
        PhysicComponent camPC = cam.getComponent(PhysicComponent.class);
        TargetComponent camTC = cam.getComponent(TargetComponent.class);
        PhysicComponent targetPC = camTC.getTarget().getComponent(PhysicComponent.class);

        // cam =cam.pos + (target.pos-cam.pos + (target.size+cam.viewport) *
        // 0.5))*tweenFactor*elapsed

        camPC.setPosition(camPC.getPosition()
                .add(targetPC.getPosition()
                        .add(targetPC.getSize().multiply(0.5).substract(camPC.getSize().multiply(0.5))
                                .substract(camPC.getPosition()))
                        .multiply(camTC.getTweenFactor() * Math.min(elapsed, 1))));
        cam.update();
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
    private void updateEntity(double elapsed, Entity e) {
        e.update(elapsed);
        if (e.containsComponent(PhysicComponent.class)) {

            PhysicComponent pc = (PhysicComponent) e.getComponent(PhysicComponent.class);

            switch (pc.getType()) {
                case DYNAMIC -> {
                    applyWorldRules(pc, world);

                    pc.setAcceleration(new Vector2d().addAll(pc.getForces()).multiply(1.0 / pc.getMass()).maximize(2.0));
                    pc.setVelocity(pc.getVelocity().add(pc.getAcceleration().multiply(0.5 * elapsed)).maximize(4.0));
                    pc.setPosition(pc.getPosition().add(pc.getVelocity().multiply(elapsed)));

                    constrainToWorldArea(pc, world);

                    // apply Material roughness on velocity
                    pc.setVelocity(pc.getVelocity().multiply(pc.getMaterial().getRoughness()));

                    // update the corresponding Entity's GraphicComponent shape for rendering.
                    GraphicComponent gc = e.getComponent(GraphicComponent.class);
                    gc.update(pc.getPosition(), pc.getSize());

                    pc.getForces().clear();
                }
                case STATIC -> {
                    // TODO define processing for static entity
                }
                default -> {
                    // TODO define default processing (if any)
                }
            }
        }
    }

    /**
     * Apply all {@link World} rules: apply all world forces on any contained
     * {@link Entity}'s {@link PhysicComponent}.
     *
     * @param pc    the {@link PhysicComponent} from the {@link Entity} to be
     *              updated
     * @param world the {@link World} instance to take into account.
     */
    private void applyWorldRules(PhysicComponent pc, World world) {
        PhysicComponent worldPC = world.getComponent(PhysicComponent.class);
        if (worldPC.getBBox().contains(pc.getBBox())) {
            // apply all World forces to the PhysicComponent.
            pc.getForces().addAll(worldPC.getForces());
        }
    }

    /**
     * Keep all {@link Entity}'s {@link PhysicComponent} position into the
     * {@link World}'s play area.
     *
     * @param pc    the {@link PhysicComponent} from the {@link Entity} to be
     *              updated
     * @param world the {@link World} instance to take into account.
     */
    private void constrainToWorldArea(PhysicComponent pc, World world) {
        PhysicComponent worldPC = world.getComponent(PhysicComponent.class);
        Vector2d position = pc.getPosition();
        Vector2d velocity = pc.getVelocity();
        Vector2d size = pc.getSize();

        Rectangle2D worldRect = worldPC.getBBox();
        if (!world.getPlayArea().contains(position.x, position.y, size.x, size.y)) {
            if (position.x < worldRect.getX()) {
                position.x = worldRect.getX();
                velocity = velocity.set(velocity.x * -worldPC.getMaterial().getElasticity(), velocity.y);
            }
            if (position.x + size.x > worldRect.getX() + worldRect.getWidth()) {
                position.x = worldRect.getX() + worldRect.getWidth() - size.x;
                velocity = velocity.set(velocity.x * -worldPC.getMaterial().getElasticity(), velocity.y);

            }
            if (position.y < worldRect.getY()) {
                position.y = worldRect.getY();
                velocity = velocity.set(velocity.x, velocity.y * -worldPC.getMaterial().getElasticity());
            }
            if (position.y + size.y > worldRect.getY() + worldRect.getHeight()) {
                position.y = worldRect.getY() + worldRect.getHeight() - size.y;
                velocity = velocity.set(velocity.x, velocity.y * -worldPC.getMaterial().getElasticity());
            }
            pc.setPosition(position);
            pc.setVelocity(velocity);
        }
    }

    /**
     * Retrieves the priority level of this service. The priority determines
     * the execution order of services, with lower numbers indicating higher
     * precedence.
     *
     * @return the priority level as an integer
     */
    @Override
    public int getPriority() {
        return 2;
    }

    /**
     * Releases resources, performs clean-up operations, and ensures the proper
     * shutdown of this PhysicEngineService instance. This method is called
     * to free any resources or references held by the service before the
     * application shuts down or the service is no longer needed.
     *
     * @param app the application instance associated with this service, used
     *            to access shared resources or other application-level data
     *            during the disposal process.
     */
    @Override
    public void dispose(App app) {
    }

    /**
     * Retrieves the statistics of the PhysicEngineService, including the count of
     * updated objects processed by the physics engine.
     *
     * @return a map containing statistical data for the service, where the keys
     * represent the statistic names (e.g., "service.physic.engine.object.counter")
     * and the values represent their respective values.
     */
    @Override
    public Map<String, Object> getStats() {
        return Map.of("updated", nbUpdatedObjects,
                "UPS", 120);
    }

    /**
     * Retrieves the {@link World} instance managed by the {@code PhysicEngineService}.
     *
     * @return the current {@link World} instance, which contains the play area, gravity,
     * and entities managed by the physics engine.
     */
    public World getWorld() {
        return this.world;
    }
}
