package com.merckgroup.demo.scenes;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import com.merckgroup.framework.App;
import com.merckgroup.framework.math.Vector2d;
import com.merckgroup.framework.components.GraphicComponent;
import com.merckgroup.framework.components.PhysicComponent;
import com.merckgroup.framework.entities.Entity;
import com.merckgroup.framework.entities.Material;
import com.merckgroup.framework.scenes.AbstractScene;
import com.merckgroup.framework.services.InputService;

/**
 * Demo01 TitleScene to illustrate a Scene implementation.
 */
public class PlayScene extends AbstractScene {

    public PlayScene(App app, String name) {
        super(app, name);
    }

    @Override
    public void create(App app) {
        Entity player = new Entity("player")
                .add(new GraphicComponent()
                        .setColor(Color.WHITE)
                        .setFillColor(Color.RED)
                        .setShape(new Rectangle2D.Double()))
                .add(new PhysicComponent()
                        .setMaterial(Material.DEFAULT)
                        .setMass(60.0)
                        .setPosition(new Vector2d(100.0, 100.0))
                        .setSize(50.0, 50.0));

        add(player);
    }

    @Override
    public void update(App app) {
        InputService input = (InputService) app.getService(InputService.class.getSimpleName());
        if (input.isKeyPressed(KeyEvent.VK_UP)) {

        }
        if (input.isKeyPressed(KeyEvent.VK_DOWN)) {

        }
        if (input.isKeyPressed(KeyEvent.VK_LEFT)) {

        }
        if (input.isKeyPressed(KeyEvent.VK_RIGHT)) {

        }
    }

}
