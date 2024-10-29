package com.merckgroup.framework.services;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.merckgroup.framework.App;

public class InputService extends AbstractService implements KeyListener {

    boolean[] keys = new boolean[1024];

    protected InputService(App app) {
        super(app);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void init(App app, String[] args) {
        // TODO request for RenderingService and retrieve JFrame instance.
        // and attach this service as a Keylistener.
    }

    @Override
    public void process(App app) {
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public void dispose(App app) {
    }

    public boolean isKeyPressed(int vkCode) {
        return keys[vkCode];
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

}
