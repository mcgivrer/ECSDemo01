package com.merckgroup.framework.services;

import static com.merckgroup.framework.App.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.merckgroup.framework.App;
import com.merckgroup.framework.scenes.Scene;

/**
 * The {@link SceneManager} class is a {@link Service} implementation to manage
 * {@link Scene] , starting by loading the required scene according to the
 * configuration keys <code>app.scenes.list</code> and
 * <code>app.scenes.default</code> respectively the list of scene to me managed
 * by the service, and the default one to be loaded at App start during service
 * initialization phase.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */

public class SceneManager extends AbstractService {

    /**
     * The internal list of Scene instances.
     */
    private Map<String, Scene> scenes = new HashMap<>();
    /**
     * the current active Scene; null if not.
     */
    private Scene currentScene;

    /**
     * Initialize the Scene manager service.
     *
     * @param app the parent {@link App} instance.
     */
    public SceneManager(App app) {
        super(app);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void init(App app, String[] args) {
        ConfigurationService cs = (ConfigurationService) app.getService(ConfigurationService.class.getSimpleName());
        String[] scenesList = cs.getValue("app.scenes.list");
        String defaultSceneName = cs.getValue("app.scenes.default");
        createSceneInstances(scenesList);
        activate(defaultSceneName);
    }

    /**
     * Activate the sceneName instance. if a rpevious scene is active, dispose it.
     *
     * @param sceneName the name of the next scene to be activated.
     */
    private void activate(String sceneName) {
        if (scenes.containsKey(sceneName)) {
            if (currentScene != null) {
                currentScene.dispose(app);
            }
            currentScene = scenes.get(sceneName);
            currentScene.init(app);
            currentScene.create(app);
            EntityManager entMgr = (EntityManager) app.getService(EntityManager.class.getSimpleName());
            entMgr.addAll(currentScene.getEntities());
            info(SceneManager.class, "Scene %s loaded and activated", sceneName);
        }
    }

    /**
     * Create a Scene instances according to configuration list.
     *
     * @param scenesList the list of {@link Scene} implementation classes from the
     *                   configuration file.
     */
    private void createSceneInstances(String[] scenesList) {
        Arrays.asList(scenesList).forEach(scene -> {
            String[] kv = scene.split(":");
            String name = kv[0];
            String sceneClassName = kv[1];
            try {
                Class<?> sceneClass = Class.forName(sceneClassName);
                Scene sceneInstance = (Scene) sceneClass.getConstructor(App.class, String.class).newInstance(app, name);
                scenes.put(name, sceneInstance);
                info(SceneManager.class, "Scene [%s] named \"%s\" instantiated", sceneClassName, name);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
                     | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                error(SceneManager.class, "Unable to instantiate Scene [%s] with name \"%s\"", sceneClassName, name);
            }
        });
    }

    /**
     * On a normal process pace, the {@link SceneManager} will detect is
     * {@link Scene} change is required.
     */
    @Override
    public void process(App app) {
        if (currentScene != null && !currentScene.requestChange().equals("")) {
            String nextSceneName = currentScene.requestChange();
            currentScene.resetRequestChange();
            activate(nextSceneName);
        }
    }

    /**
     * Retrieve service priority.
     */
    @Override
    public int getPriority() {
        return 3;
    }

    /**
     * Dispose all the existing {@link Scene} instances.
     */
    @Override
    public void dispose(App app) {
        scenes.values().stream().forEach(scene -> scene.dispose(app));
    }
}
