package com.snapgames.framework.services;

import static com.snapgames.framework.App.*;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.snapgames.framework.App;
import com.snapgames.framework.math.Vector2d;

/**
 * This ConfigurationService class is {@link Service} implementation to load and
 * manage configuration (String key:Typed values)
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class ConfigurationService extends AbstractService {
    /**
     * internal map of all values instances
     */
    private Map<String, Object> values = new ConcurrentHashMap<>();

    /**
     * Properties object to load properties file key/values.
     */
    private Properties config = new Properties();

    /**
     * THe default configuration file name.
     */
    private String defaultConfigurationFileName;
    private long nbGetValues = 0;

    /**
     * Configuration service initialization according to parent app.
     *
     * @param app the parent App instance.
     */
    public ConfigurationService(App app) {
        super(app);
        defaultConfigurationFileName = "/config.properties";
    }

    /**
     * Retrieves the name of the service.
     *
     * @return the simple name of the class implementing the service.
     */
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void init(App app, String[] args) {
        parseArgs(args);
        loadConfiguration();
        parseArgs(args);
    }

    /**
     * Processes the given application instance within the configuration service.
     *
     * @param app the application instance to be processed.
     */
    @Override
    public void process(App app) {
    }

    /**
     * Retrieves the priority value for this service, which determines
     * its execution order during initialization, processing, and disposal.
     *
     * @return the priority value of the service as an integer
     */
    @Override
    public int getPriority() {
        return 0;
    }

    /**
     * Releases resources and performs cleanup operations for this configuration service.
     *
     * @param app the parent application instance associated with this service.
     */
    @Override
    public void dispose(App app) {
    }

    @Override
    public Map<String, Object> getStats() {
        return Map.of("service.configuration.service.values", values, "service.configuration.service.counter.getValue",
                nbGetValues, "service.configuration.service.counter.config.file.entries", config.entrySet().size());
    }

    /**
     * Parse all the args to extract key/values.
     *
     * @param args Array of args from the Java CLI.
     */
    private void parseArgs(String[] args) {
        for (String arg : args) {
            if (arg.contains("=")) {
                String[] kv = arg.split("=");
                extractConfigValue(kv[0], kv[1]);
            }
        }
    }

    /**
     * Extracts and processes a specific configuration value based on the given key.
     * Updates the application state or configuration parameters accordingly.
     * Handles different configuration fields such as application settings,
     * debug levels, window properties, rendering parameters, and physics engine
     * properties. Logs the configuration changes or issues for unknown arguments.
     *
     * @param key   the configuration key indicating what parameter is being extracted.
     * @param value the value associated with the configuration key to be processed.
     */
    private void extractConfigValue(String key, String value) {
        switch (key.toLowerCase()) {
            case "app.config", "config", "c" -> {
                defaultConfigurationFileName = value;
                info(ConfigurationService.class, "Configuration file name set to %s", defaultConfigurationFileName);
            }
            case "app.debug.level", "debuglevel", "dl" -> {
                int dl = Integer.parseInt(value);
                values.put("app.debug.level", dl);
                app.setDebugLevel(dl);
                info(ConfigurationService.class, "Debug Level set to %s (value from 0 to 6)", value);
            }
            case "app.debug.counter", "testcounter", "tc" -> {
                long counter = Long.parseLong(value);
                values.put("app.debug.counter", counter);
                info(ConfigurationService.class, "Test mode: max loop counter set to %s", value);
                app.setTestLoopCounter(counter);
            }
            case "app.scenes.list", "scenes" -> {
                values.put("app.scenes.list", value.split(";"));
                info(ConfigurationService.class, "Scene list is [%s]", value);
            }
            case "app.window.title" -> {
                values.put("app.window.title", value);
                app.setAppName(value);
                info(ConfigurationService.class, "Set application name and window title to \"%s\"", value);
            }
            case "app.scenes.default", "scene" -> {
                values.put("app.scenes.default", value);
                info(ConfigurationService.class, "Default scene is %s", value);
            }
            case "app.render.buffer.size" -> {
                String[] sizes = value.split("x");
                Dimension buffSize = new Dimension(Integer.parseInt(sizes[0]), Integer.parseInt(sizes[1]));
                values.put("app.render.buffer.size", buffSize);
                info(ConfigurationService.class, "Rendering buffer size set to %s", value);
            }
            case "app.render.window.size" -> {
                String[] sizes = value.split("x");
                Dimension buffSize = new Dimension(Integer.parseInt(sizes[0]), Integer.parseInt(sizes[1]));
                values.put("app.render.window.size", buffSize);
                info(ConfigurationService.class, "Rendering window size set to %s", value);
            }
            case "app.physic.world.gravity" -> {
                String[] sizes = value.split(",");
                Vector2d gravity = new Vector2d(Double.parseDouble(sizes[0]), Double.parseDouble(sizes[1]));
                values.put("app.physic.world.gravity", gravity);
                info(ConfigurationService.class, "Physic Engine World gravity set to %s", gravity);
            }
            case "app.physic.world.play.area" -> {
                String[] sizes = value.split("x");
                Rectangle2D playArea = new Rectangle2D.Double(0, 0, Double.parseDouble(sizes[0]), Double.parseDouble(sizes[1]));
                values.put("app.physic.world.play.area", playArea);
                info(ConfigurationService.class, "Physic Engine World play area size set to %s", value);
            }
            case "app.render.window.max.buffers" -> {
                int maxBuffers = Integer.parseInt(value);
                values.put("app.render.window.max.buffers", maxBuffers);
                info(ConfigurationService.class, "Rendering max buffer number set to %s", value);
            }

            default -> {
                warn(ConfigurationService.class, "Unknown argument %s:%s", key, value);
            }
        }
    }

    /**
     * Loads the configuration from the specified default configuration file.
     * The method reads key-value pairs from the configuration file, validates
     * non-null keys, and extracts corresponding values to process them further.
     * In the event of an error during the file reading process, it logs an
     * appropriate error message indicating the failure reason.
     * <p>
     * This method relies on the configuration file being accessible at the
     * specified location and that the file contains valid key-value pairs.
     * <p>
     * Throws an IOException in case of failure to read the configuration file.
     */
    private void loadConfiguration() {
        try {
            config.load(App.class.getResourceAsStream(defaultConfigurationFileName));
            config.entrySet().stream().filter(e -> e.getKey() != null)
                    .forEach(e -> extractConfigValue((String) e.getKey(), (String) e.getValue()));
        } catch (IOException ie) {
            error(App.class, "Unable to read configuration file %s : %s", defaultConfigurationFileName,
                    ie.getMessage());
        }
    }

    /**
     * Retrn the typed value for the requiest key.
     *
     * @param <T>         the output type of the value
     * @param attrKeyName the key to be retrieved.
     * @return the corresponding value from the values map.
     */
    public <T> T getValue(String attrKeyName) {
        nbGetValues++;
        return (T) values.get(attrKeyName);
    }

}
