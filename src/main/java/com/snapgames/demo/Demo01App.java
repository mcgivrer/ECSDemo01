
package com.snapgames.demo;

import com.snapgames.framework.App;
import com.snapgames.framework.services.ConfigurationService;
import com.snapgames.framework.services.EntityManagerService;
import com.snapgames.framework.services.InputService;
import com.snapgames.framework.services.PhysicEngineService;
import com.snapgames.framework.services.RenderingService;
import com.snapgames.framework.services.SceneManagerService;

public class Demo01App extends App {

    public Demo01App() {
        super();
        // add all required services where priority are defined in the Service
        // implementation itself.
        add(new ConfigurationService(this));
        add(new EntityManagerService(this));
        add(new PhysicEngineService(this));
        add(new SceneManagerService(this));
        add(new RenderingService(this));
        add(new InputService(this));
    }

    public static void main(String[] args) {
        Demo01App app = new Demo01App();
        app.run(args);
    }
}
