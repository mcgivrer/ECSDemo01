package com.snapgames.framework.scenes;

import java.util.Collection;

import com.snapgames.framework.App;
import com.snapgames.framework.utils.Node;
import com.snapgames.framework.entities.Camera;
import com.snapgames.framework.entities.Entity;

/**
 * The {@code Scene} interface defines the core structure and behavior for a scene
 * in an application. A scene represents a distinct state or view within the application
 * that manages its own entities, lifecycle, and associated resources. Each scene
 * interacts with an {@link App} instance to access global services and lifecycle events.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */

public interface Scene {

    /**
     * Retrieves the name of the scene.
     *
     * @return the name of the scene as a {@code String}.
     */
    String getName();

    /**
     * Initializes the scene with the provided {@link App} instance. This method sets up
     * all necessary configurations and resources required for the scene to function
     * properly within the application context.
     *
     * @param app the {@link App} instance that provides access to global services
     *            and application lifecycle events for initializing the scene.
     */
    void init(App app);

    /**
     * Creates the scene and initializes all necessary entities, components, or resources
     * required for this scene to operate. This method is typically invoked after a scene
     * has been initialized and provides the necessary setup for the specific functional
     * aspects of the scene.
     *
     * @param app the {@link App} instance providing global services and context to the
     *            scene during the creation process.
     */
    void create(App app);

    /**
     * Updates the current state of the scene. This method is responsible for processing
     * and managing the entities, components, and interactions that define a scene's
     * behavior during the application lifecycle.
     *
     * @param app the {@link App} instance providing context and access to global
     *            services needed for the update operations.
     */
    void update(App app);

    /**
     * Retrieves the collection of entities currently managed by the scene.
     *
     * @return a collection of {@link Entity} objects representing all entities
     * associated with this scene.
     */
    Collection<Node> getEntities();

    /**
     * Retrieves a specific {@link Entity} instance managed by the scene, identified by its name.
     *
     * @param string the name of the {@link Entity} to be retrieved.
     * @return the {@link Entity} instance with the specified name, or {@code null} if no entity with the provided name exists in the scene.
     */
    Entity getEntity(String string);

    /**
     * Releases and cleans up all resources associated with the scene. This method
     * is intended to be invoked when the scene is no longer needed or is about to
     * be discarded, ensuring that any allocated resources are properly disposed of.
     *
     * @param app the {@link App} instance associated with the scene, used to access
     *            global services or application context necessary for performing
     *            cleanup operations.
     */
    void dispose(App app);

    /**
     * Requests a change in the current scene or its state. The specific implementation
     * defines the nature of the change being requested, such as transitioning to a
     * different scene or modifying scene behavior.
     *
     * @return a {@code String} representing the requested change. The returned value
     * may correspond to a scene identifier, an action, or any other relevant
     * information necessary to process the request.
     */
    String requestChange();

    /**
     * Resets the current request for a change in the scene. This method is typically used
     * to clear or nullify any pending scene change requests that may have been initiated,
     * ensuring that no unintended state transitions occur. The specific implementation
     * details depend on how scene change requests are managed within the application.
     */
    void resetRequestChange();

    /**
     * Retrieves the {@link Camera} instance associated with the scene. The camera is responsible
     * for managing the viewport and interacting with entities within the scene's viewable area.
     *
     * @return the {@link Camera} instance associated with this scene.
     */
    Camera getCamera();
}
