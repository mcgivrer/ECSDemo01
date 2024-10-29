
package com.merckgroup.framework.scenes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.merckgroup.framework.App;
import com.merckgroup.framework.entities.Entity;

public abstract class AbstractScene implements Scene {

    protected Map<String, Entity> entities = new HashMap<>();
    protected String name = "undefined";
    protected App app;

    protected String requestNextScene = "";

    protected AbstractScene(App app, String name) {
        this.app = app;
        this.name = name;
    }

    @Override
    public void dispose(App app) {

    }

    @Override
    public Collection<Entity> getEntities() {
        return entities.values();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void init(App app) {
    }

    public void add(Entity e) {
        entities.put(e.getName(), e);
    }

    public String requestChange() {
        return this.requestNextScene;
    }

    public void resetRequestChange() {
        this.requestNextScene = "";
    }
}
