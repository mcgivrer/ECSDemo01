package com.merckgroup.framework.components;

import com.merckgroup.framework.entities.Entity;

public class TargetComponent implements Component {
    private Entity target = null;
    private double tweenFactor = 1.0;

    public TargetComponent() {

    }

    public Entity getTarget() {
        return target;
    }

    public double getTweenFactor() {
        return tweenFactor;
    }


    public TargetComponent setTarget(Entity target) {
        this.target = target;
        return this;
    }

    public TargetComponent setTweenFactor(double tweenFactor) {
        this.tweenFactor = tweenFactor;
        return this;
    }
}
