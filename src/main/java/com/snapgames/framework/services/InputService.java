package com.snapgames.framework.services;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.snapgames.demo.scenes.PlayScene;
import com.snapgames.framework.App;
import com.snapgames.framework.io.InputListener;

/**
 * The {@link InputListener} class provide a key input processing service.
 * <p>
 * All the registered {@link InputListener} in the listeners will be processed on KeyPressed and KeyReleased event
 * from the {@link KeyEvent}.
 *
 * @author Frédéric Delorme
 * @since 0.0.2
 */
public class InputService extends AbstractService implements KeyListener {

    private final boolean[] keys = new boolean[1024];
    private long nbPressedKeys = 0;
    private long nbGetKeys = 0;
    private long nbEvents = 0;

    /**
     * The internal list of {@link InputListener} to be processed.
     */
    List<InputListener> listeners = new ArrayList<>();

    /**
     * Create the new instance of the {@link InputService}.
     *
     * @param app the parent {@link App} instance.
     */
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
        // nothing to do for this event based service.
    }

    @Override
    public int getPriority() {
        return 6;
    }

    @Override
    public void dispose(App app) {
        // nothing specific to be released/dispose for this service.
    }

    @Override
    public Map<String, Object> getStats() {
        return Map.of("events", nbEvents,
                "pk", nbPressedKeys, "gk", nbGetKeys);
    }

    /**
     * Retrieve pressed status for the requested key code.
     *
     * @param vkCode the key code to be checked.
     * @return true if pressed.
     */
    public boolean isKeyPressed(int vkCode) {
        nbGetKeys++;
        return keys[vkCode];
    }

    /**
     * Handles the keyTyped event triggered when a key is typed. This method
     * updates the internal counter for the number of key events processed.
     *
     * @param e the KeyEvent containing information about the typed key.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        nbEvents++;
    }

    /**
     * Handles the keyPressed event triggered when a key is pressed. This method
     * updates the internal state, tracks the number of key press events, and
     * notifies all registered InputListeners about the key press event.
     *
     * @param e the KeyEvent containing information about the pressed key.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
        nbEvents++;
        nbPressedKeys++;
        listeners.forEach(il -> il.onKeyPressed(app, e));
    }

    /**
     * Handles the keyReleased event triggered when a key is released. This method
     * updates the internal state by marking the key as released, increments the
     * number of events, and notifies all registered InputListeners about the key
     * release event.
     *
     * @param e the KeyEvent containing information about the released key.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
        nbEvents++;
        listeners.forEach(il -> il.onKeyReleased(app, e));
    }

    /**
     * register a new InputListener to be processed on KeyEvent KeyPressed and KeyReleased.
     *
     * @param il a new instance of an InputListener.
     */
    public void register(InputListener il) {
        this.listeners.add(il);
    }
}
