
package com.snapgames.framework.scenes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.snapgames.framework.App;
import com.snapgames.framework.entities.Camera;
import com.snapgames.framework.entities.Entity;

/**
 * The {@code AbstractScene} class represents a base class for implementing scenes
 * in an application. It provides fundamental functionalities to manage scene-specific
 * entities, the active camera, and scene transitions. This class serves as a parent
 * class for concrete scenes and implements the {@link Scene} interface.
 * <p>
 * Responsibilities of the {@code AbstractScene} include:
 * - Managing a collection of {@link Entity} objects that belong to the scene.
 * - Handling the active {@link Camera} for rendering the scene.
 * - Facilitating the request and management of transitions between scenes.
 * - Providing foundational lifecycle methods like initialization and disposal
 * for concrete scenes to override.
 * <p>
 * Subclasses should provide specific implementations of the scene's behavior
 * by overriding the required methods from the {@link Scene} interface.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public abstract class AbstractScene implements Scene {

    /**
     * A map that stores the entities associated with this scene, using their names as unique keys.
     * Each entry in this map represents an {@link Entity} and its corresponding string identifier.
     * This allows for efficient retrieval and management of entities within the scene.
     * <p>
     * The entities in the map are specific to the current scene and are managed through various
     * operations such as addition, retrieval, and removal.
     */
    protected Map<String, Entity> entities = new HashMap<>();
    /**
     * The name of the scene represented as a {@code String}.
     * It serves as a unique identifier or label for distinguishing
     * the scene within the application framework.
     * <p>
     * This variable is typically used for scene management operations,
     * such as transitioning between scenes or retrieving a specific scene.
     */
    protected String name;
    /**
     * A reference to the {@link App} instance associated with this scene.
     * This variable provides access to the core application framework and allows
     * interaction with shared functionalities, such as services, logging, and application state management.
     * <p>
     * It is typically used to facilitate communication and operations between the
     * scene and the main application.
     */
    protected App app;

    /**
     * The {@code activeCamera} field represents the current active {@link Camera}
     * object used by the {@code AbstractScene} to manage the visual representation
     * of the scene. This {@link Camera} is responsible for determining the
     * visible region within the game or application, applying transformations,
     * and tracking entities within its viewport.
     * <p>
     * It can be adjusted or replaced using the {@code setCamera(Camera cam)} method,
     * and its current state can be accessed via the {@code getCamera()} method.
     * <p>
     * The {@code activeCamera} is typically used in rendering operations to define
     * the part of the game world or application interface that needs to be viewed
     * by the user, as well as for entity tracking or view-related calculations.
     */
    protected Camera activeCamera;

    /**
     * The {@code requestNextScene} variable is used to hold the name of the next scene
     * that is requested to be loaded in the application. It serves as a mechanism
     * to signal a desired scene transition within the framework.
     * <p>
     * This variable is typically updated when a scene transition is initiated
     * and acts as a placeholder to inform the system of the target scene.
     */
    protected String requestNextScene = "";

    /**
     * Constructs an AbstractScene with a specified application instance and name.
     *
     * @param app  the application instance providing access to services and resources.
     * @param name the name of the scene, used to identify and manage it during the application's lifecycle.
     */
    protected AbstractScene(App app, String name) {
        this.app = app;
        this.name = name;
    }

    /**
     * Disposes of resources and performs cleanup tasks associated with the current scene.
     * This method is called when the scene is no longer active or is being removed from memory.
     *
     * @param app the application instance providing context and resources for cleanup operations.
     */
    @Override
    public void dispose(App app) {

    }

    /**
     * Retrieves the collection of all entities managed by the current scene.
     *
     * @return a collection of {@link Entity} objects representing all entities
     * within the scene.
     */
    @Override
    public Collection<Entity> getEntities() {
        return entities.values();
    }

    /**
     * Retrieves an {@link Entity} object associated with the specified identifier.
     *
     * @param string the identifier of the entity to retrieve.
     * @return the {@link Entity} associated with the given identifier or {@code null}
     * if no entity with the specified identifier is found.
     */
    public Entity getEntity(String string) {
        return entities.get(string);
    }

    /**
     * Retrieves the name of the scene.
     *
     * @return the name of the scene as a {@code String}.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Initializes the scene with the specified application context.
     * This method sets up necessary resources, entities, or configurations
     * for the scene to function within the application's lifecycle.
     *
     * @param app the application instance providing context, services,
     *            and resources for the scene initialization.
     */
    @Override
    public void init(App app) {
    }

    /**
     * Adds the specified {@link Entity} to the internal collection of entities managed by the scene.
     * The entity is associated with its name for easy retrieval.
     *
     * @param e the {@link Entity} to be added to the scene. The name of the entity, retrieved via {@code e.getName()},
     *          acts as a key for managing the entity in the scene's internal structure.
     */
    public void add(Entity e) {
        entities.put(e.getName(), e);
    }

    /**
     * Requests a change to a new scene by providing the name of the next scene
     * to be activated within the application lifecycle.
     *
     * @return the name of the next scene as a {@code String}.
     */
    public String requestChange() {
        return this.requestNextScene;
    }

    /**
     * Resets the request to change the current scene by clearing the name of the
     * next scene to be activated. This method sets the {@code requestNextScene}
     * field to an empty string, effectively canceling any pending scene change
     * requests.
     */
    public void resetRequestChange() {
        this.requestNextScene = "";
    }

    /**
     * Retrieves the current active {@link Camera} associated with the scene.
     *
     * @return the active {@link Camera} instance of the scene, or {@code null} if no camera is set.
     */
    public Camera getCamera() {
        return this.activeCamera;
    }

    /**
     * Sets the active {@link Camera} for the scene. The specified camera will be
     * associated with the scene and used for rendering and controlling the
     * viewport.
     *
     * @param cam the {@link Camera} instance to be set as the active camera for
     *            the scene.
     */
    public void setCamera(Camera cam) {
        this.activeCamera = cam;
    }
}
