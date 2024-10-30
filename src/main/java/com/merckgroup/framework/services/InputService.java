package com.merckgroup.framework.services;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

import com.merckgroup.framework.App;

public class InputService extends AbstractService implements KeyListener {

    private final boolean[] keys = new boolean[1024];
    private long nbPressedKeys = 0;
    private long nbGetKeys = 0;
    private long nbEvents = 0;

    public InputService(App app) {
        super(app);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void init(App app, String[] args) {
        RenderingService rs = app.getService(RenderingService.class.getSimpleName());
        rs.addListener(this);
    }

    @Override
    public void process(App app) {
    }

    @Override
    public int getPriority() {
        return 6;
    }

    @Override
    public void dispose(App app) {
    }

    @Override
    public Map<String, Object> getStats() {
        return Map.of("service.input.service.counter.events", nbEvents,
                "service.input.service.counter.pressed.keys", nbPressedKeys,
                "service.input.service.counter.get.keys", nbGetKeys);
    }

    public boolean isKeyPressed(int vkCode) {
        nbGetKeys++;
        return keys[vkCode];
    }

    @Override
    public void keyTyped(KeyEvent e) {
        nbEvents++;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
        nbEvents++;
        nbPressedKeys++;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
        nbEvents++;
    }
}
