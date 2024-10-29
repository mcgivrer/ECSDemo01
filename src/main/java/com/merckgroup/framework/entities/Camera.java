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
            return viewport.contains(pc.getBBox());
        }
        return false;
    }
}
