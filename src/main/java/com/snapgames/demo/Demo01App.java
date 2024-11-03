
package com.snapgames.demo;

import com.snapgames.framework.App;
import com.snapgames.framework.io.InputListener;
import com.snapgames.framework.services.ConfigurationService;
import com.snapgames.framework.services.EntityManagerService;
import com.snapgames.framework.services.InputService;
import com.snapgames.framework.services.PhysicEngineService;
import com.snapgames.framework.services.RenderingService;
import com.snapgames.framework.services.SceneManagerService;

import java.awt.event.KeyEvent;

public class Demo01App extends App implements InputListener {

    public Demo01App() {
        super();
        // add all required services where priority are defined in the Service
        // implementation itself.
        add(new ConfigurationService(this));
        add(new EntityManagerService(this));
        add(new PhysicEngineService(this));
        add(new SceneManagerService(this));
        add(new RenderingService(this));

        InputService is = new InputService(this);
        add(is);
        is.register(this);
    }


    @Override
    public void onKeyReleased(App app, KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            requestExit(true);
        }
    }

    public static void main(String[] args) {
        Demo01App app = new Demo01App();
        app.run(args);
    }
}
