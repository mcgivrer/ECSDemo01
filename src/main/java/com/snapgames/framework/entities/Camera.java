package com.snapgames.framework.entities;

import java.awt.geom.Rectangle2D;

import com.snapgames.framework.components.PhysicComponent;
import com.snapgames.framework.components.TargetComponent;

public class Camera extends Entity {
    public Rectangle2D viewport = new Rectangle2D.Double(0, 0, 320, 200);

    public Camera(String name) {
        super(name);
        add(new TargetComponent());
        add(new PhysicComponent());
    }

    public boolean hasEntityInView(Entity e) {
        if (e.containsComponent(PhysicComponent.class)) {
            PhysicComponent pc = e.getComponent(PhysicComponent.class);
            return viewport.contains(pc.getBBox()) || viewport.intersects(pc.getBBox());
        }
        return false;
    }

    public Camera setTarget(Entity player, double tweenFactor) {
        TargetComponent tc = getComponent(TargetComponent.class);
        tc.setTarget(player);
        tc.setTweenFactor(tweenFactor);
        return this;
    }

    public Camera setTarget(Entity player) {
        TargetComponent tc = getComponent(TargetComponent.class);
        tc.setTarget(player);
        return this;
    }

    public Camera setTweenFactor(double tweenFactor) {
        TargetComponent tc = getComponent(TargetComponent.class);
        tc.setTweenFactor(tweenFactor);
        return this;
    }

    public Camera setViewport(int w, int h) {
        PhysicComponent pc = getComponent(PhysicComponent.class);
        viewport.setRect(pc.getPosition().getX(), pc.getPosition().getY(), w, h);
        pc.setSize(w, h);
        return this;
    }

    public Rectangle2D getViewPort() {
        return this.viewport;
    }
}
