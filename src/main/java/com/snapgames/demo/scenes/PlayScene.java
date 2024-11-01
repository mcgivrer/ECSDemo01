package com.snapgames.demo.scenes;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import com.snapgames.framework.App;
import com.snapgames.framework.components.*;
import com.snapgames.framework.entities.Camera;
import com.snapgames.framework.entities.Entity;
import com.snapgames.framework.entities.Material;
import com.snapgames.framework.entities.World;
import com.snapgames.framework.math.Vector2d;
import com.snapgames.framework.scenes.AbstractScene;
import com.snapgames.framework.services.InputService;
import com.snapgames.framework.services.PhysicEngineService;

/**
 * Demo01 TitleScene to illustrate a Scene implementation.
 */
public class PlayScene extends AbstractScene {

    public PlayScene(App app, String name) {
        super(app, name);
    }

    @Override
    public void create(App app) {

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
        setCamera(new Camera("cam01").setViewport(320, 200).setTarget(player, 0.002));
        add(player);

        Entity score = new Entity("score")
                .add(new GraphicComponent()
                        .setColor(Color.WHITE)
                        .setStickToViewport(true))
                .add(new PhysicComponent()
                        .setMaterial(Material.DEFAULT)
                        .setMass(1.0)
                        .setPosition(new Vector2d(10.0, 32.0))
                        .setType(PhysicType.STATIC))
                .add(new TextComponent()
                        .setText("00000"))
                .add(new PriorityComponent().setPriority(2));
        add(score);

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
                .add(new PriorityComponent().setPriority(100));
        add(grid);
    }

    @Override
    public void update(App app) {
        InputService input = (InputService) app.getService(InputService.class.getSimpleName());
        Entity player = getEntity("player");
        PhysicComponent pc = player.getComponent(PhysicComponent.class);

        if (input.isKeyPressed(KeyEvent.VK_UP)) {
            pc.getForces().add(new Vector2d(0, -0.99));
        }
        if (input.isKeyPressed(KeyEvent.VK_DOWN)) {
            pc.getForces().add(new Vector2d(0, 0.002));

        }
        if (input.isKeyPressed(KeyEvent.VK_LEFT)) {
            pc.getForces().add(new Vector2d(-0.002, 0));

        }
        if (input.isKeyPressed(KeyEvent.VK_RIGHT)) {
            pc.getForces().add(new Vector2d(0.002, 0));

        }
    }
}
