package com.snapgames.framework.scenes;

import java.util.Collection;

import com.snapgames.framework.App;
import com.snapgames.framework.entities.Camera;
import com.snapgames.framework.entities.Entity;

/**
 * 
 */

public interface Scene {

    String getName();

    void init(App app);

    void create(App app);

    void update(App app);

    Collection<Entity> getEntities();

    Entity getEntity(String string);

    void dispose(App app);

    String requestChange();

    void resetRequestChange();

    Camera getCamera();
}
