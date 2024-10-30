
package com.merckgroup.demo;

import com.merckgroup.framework.App;
import com.merckgroup.framework.services.ConfigurationService;
import com.merckgroup.framework.services.EntityManager;
import com.merckgroup.framework.services.InputService;
import com.merckgroup.framework.services.PhysicEngine;
import com.merckgroup.framework.services.RenderingService;
import com.merckgroup.framework.services.SceneManager;

public class Demo01App extends App {

    public Demo01App() {
        super();
        // add all required services where priority are defined in the Service
        // implementation itself.
        add(new ConfigurationService(this));
        add(new EntityManager(this));
        add(new PhysicEngine(this));
        add(new SceneManager(this));
        add(new RenderingService(this));
        add(new InputService(this));
    }

    public static void main(String[] args) {
        Demo01App app = new Demo01App();
        app.run(args);
    }
}
