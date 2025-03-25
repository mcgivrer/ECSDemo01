package com.snapgames.demo.scenes;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import com.snapgames.framework.App;
import com.snapgames.framework.components.*;
import com.snapgames.framework.entities.Camera;
import com.snapgames.framework.entities.Entity;
import com.snapgames.framework.entities.Material;
import com.snapgames.framework.entities.World;
import com.snapgames.framework.io.InputListener;
import com.snapgames.framework.math.Vector2d;
import com.snapgames.framework.scenes.AbstractScene;
import com.snapgames.framework.services.InputService;
import com.snapgames.framework.services.PhysicEngineService;

/**
 * The PlayScene class represents a specific gameplay scene in the application.
 * It extends the {@link AbstractScene} class and implements the {@link InputListener} interface
 * to handle keyboard input and manage entities within the scene.
 * <p>
 * This class is responsible for initializing, rendering, updating entities, and managing user input.
 * It includes pre-defined entities such as a player, score display, energy gauge, and a grid for the scene layout.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class PlayScene extends AbstractScene implements InputListener {

    public PlayScene(App app, String name) {
        super(app, name);
    }

    /**
     * Initializes and configures the scene by creating and registering entities
     * such as the player, score, energy gauge, and grid. Entities are set up with
     * their respective components and added to the scene. It also configures the
     * camera and other relevant elements necessary for the scene's operation.
     *
     * @param app the application instance providing access to services and
     *            necessary resources for entity creation and registration.
     */
    @Override
    public void create(App app) {
        // register this Scene as an InputListener
        InputService is = app.getService(InputService.class.getSimpleName());
        is.register(this);

        World w = ((PhysicEngineService) app.getService(PhysicEngineService.class.getSimpleName())).getWorld();
        Entity player = new Entity("player")
                .add(new GraphicComponent()
                        .setColor(Color.WHITE)
                        .setFillColor(Color.RED)
                        .setShape(new Rectangle2D.Double()))
                .add(new PhysicComponent()
                        .setMaterial(new Material("player_mat", 1.0, 1.0, 0.99))
                        .setMass(60.0)
                        .setPosition(new Vector2d(100.0, 100.0))
                        .setSize(16.0, 18.0))
                .add(new PriorityComponent().setPriority(1));
        setCamera(new Camera("cam01").setViewport(320, 200).setTarget(player, 0.02));
        add(player);

        Entity score = new Entity("score")
                .add(new GraphicComponent()
                        .setColor(null)
                        .setFillColor(null)
                        .setStickToViewport(true))
                .add(new PhysicComponent()
                        .setMaterial(Material.DEFAULT)
                        .setMass(1.0)
                        .setPosition(new Vector2d(10.0, 32.0))
                        .setType(PhysicType.STATIC))
                .add(new TextComponent()
                        .setText("00000")
                        .setTextColor(Color.WHITE))
                .add(new PriorityComponent().setPriority(2));
        add(score);

        Entity energyGauge = new Entity("energy")
                .add(new GraphicComponent()
                        .setColor(Color.WHITE)
                        .setFillColor(Color.BLACK)
                        .setStickToViewport(true))
                .add(new PhysicComponent()
                        .setMaterial(Material.DEFAULT)
                        .setMass(1.0)
                        .setPosition(new Vector2d(270.0, 28.0))
                        .setSize(40, 6)
                        .setType(PhysicType.STATIC))
                .add(new GaugeComponent(100, 0, 100).setGaugeColor(Color.RED))
                .add(new PriorityComponent().setPriority(2));
        add(energyGauge);

        generateBouncingEnemies(10);

        Entity grid = new Entity("grid")
                .add(new GraphicComponent()
                        .setColor(Color.GRAY))
                .add(new PhysicComponent()
                        .setMaterial(Material.DEFAULT)
                        .setMass(1.0)
                        .setPosition(new Vector2d(0, 0))
                        .setSize(w.getPlayArea().getWidth(), w.getPlayArea().getHeight())
                        .setType(PhysicType.DYNAMIC))
                .add(new GridComponent(16, 16).setBox(w.getPlayArea()))
                .add(new PriorityComponent().setPriority(-10));
        add(grid);
    }

    public void generateBouncingEnemies(int nb) {
        for (int i = 0; i < nb; i++) {
            Entity enemy = new Entity("enemy_%d".formatted(i))
                    .add(new GraphicComponent()
                            .setColor(Color.CYAN)
                            .setFillColor(Color.BLUE)
                            .setShape(new Ellipse2D.Double(-8.0, -8.0, 16.0, 16.0)))
                    .add(new PhysicComponent()
                            .setType(PhysicType.DYNAMIC)
                            .setMaterial(new Material("enemy_mat", 1.0, 0.20, 1.12))
                            .setMass(Math.random() * 100.0 + 10.0)
                            .setPosition(new Vector2d(-160 + Math.random() * 320.0, -100 + Math.random() * 200.0))
                            .setVelocity(
                                    new Vector2d(-0.00001 + Math.random() * 0.00002, -0.00001 + Math.random() * 0.00002)
                            )
                            .setSize(8.0, 8.0))
                    .add(new PriorityComponent().setPriority(2 + i));
            add(enemy);
        }
    }

    /**
     * Updates the current game scene by checking inputs and applying forces to the
     * player's physical component based on the pressed keys.
     *
     * @param app the application instance providing access to services and game
     *            entities. The method uses this parameter to fetch the input
     *            service and the player's entity for processing updates.
     */
    @Override
    public void update(App app) {
        InputService input = (InputService) app.getService(InputService.class.getSimpleName());
        Entity player = getEntity("player");
        PhysicComponent pc = player.getComponent(PhysicComponent.class);

        if (input.isKeyPressed(KeyEvent.VK_UP)) {
            pc.getForces().add(new Vector2d(0, -0.0005));
        }
        if (input.isKeyPressed(KeyEvent.VK_DOWN)) {
            pc.getForces().add(new Vector2d(0, 0.0002));

        }
        if (input.isKeyPressed(KeyEvent.VK_LEFT)) {
            pc.getForces().add(new Vector2d(-0.0002, 0));

        }
        if (input.isKeyPressed(KeyEvent.VK_RIGHT)) {
            pc.getForces().add(new Vector2d(0.0002, 0));
        }
    }

    /**
     * Handles key release events within the PlayScene. Depending on the released key,
     * this method performs specific actions such as toggling debug levels or reversing gravity.
     *
     * @param app the application instance providing access to services and resources for game processing.
     * @param ke  the {@link KeyEvent} representing the key release event, which includes the key code to identify the action to perform.
     */
    @Override
    public void onKeyReleased(App app, KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_D -> {
                // switch debug level mode.
                app.setDebugLevel(app.getDebugLevel() + 1 < 6 ? app.getDebugLevel() + 1 : 0);
            }
            case KeyEvent.VK_G -> {
                // reverse gravity
                PhysicEngineService pes = app.getService(PhysicEngineService.class.getSimpleName());
                pes.getWorld().setGravity(pes.getWorld().getGravity().multiply(-1));
            }
            default -> {
                // do nothing specific for any other keys.
            }
        }
    }
}
