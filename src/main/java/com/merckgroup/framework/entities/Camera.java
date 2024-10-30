package com.merckgroup.framework.entities;

import com.merckgroup.framework.components.PhysicComponent;
import com.merckgroup.framework.components.TargetComponent;

import java.awt.geom.Rectangle2D;

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

}
