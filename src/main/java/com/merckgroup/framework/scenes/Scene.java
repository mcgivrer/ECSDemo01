package com.merckgroup.framework.scenes;

import java.util.Collection;

import com.merckgroup.framework.App;
import com.merckgroup.framework.entities.Camera;
import com.merckgroup.framework.entities.Entity;

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
