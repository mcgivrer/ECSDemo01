
package com.merckgroup.demo;

import com.merckgroup.framework.App;
import com.merckgroup.framework.services.ConfigurationService;
import com.merckgroup.framework.services.EntityManagerService;
import com.merckgroup.framework.services.InputService;
import com.merckgroup.framework.services.PhysicEngineService;
import com.merckgroup.framework.services.RenderingService;
import com.merckgroup.framework.services.SceneManagerService;

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
