package com.snapgames.framework.io;

import com.snapgames.framework.App;

import java.awt.event.KeyEvent;

/**
 * This {@link InputListener} interface to be implemented from any class in your game inheriting from {@link App}
 * will allow you to process {@link KeyEvent} from the main game window ({@link javax.swing.JFrame}).
 *
 * @author Frédéric Delorme
 * @since 0.0.3
 */
public interface InputListener {
    /**
     * Process any KeyPressed {@link KeyEvent}.
     *
     * @param app the parent App instance.
     * @param ke  the {@link KeyEvent} to be processed.
     */
    default void onKeyPressed(App app, KeyEvent ke) {
    }

    /**
     * Process any KeyReleased {@link KeyEvent}.
     *
     * @param app the parent App instance.
     * @param ke  the {@link KeyEvent} to be processed.
     */
    default void onKeyReleased(App app, KeyEvent ke) {
    }
}
