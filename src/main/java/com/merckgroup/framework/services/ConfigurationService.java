package com.merckgroup.framework.services;

import static com.merckgroup.framework.App.*;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.merckgroup.framework.App;

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
     * Properties object to load properteis file key/values.
     */
    private Properties config = new Properties();

    /**
     * THe default configuration file name.
     */
    private String defaultConfigurationFileName;

    /**
     * Configuration service initialization according to parent app.
     *
     * @param app the parent App instance.
     */
    public ConfigurationService(App app) {
        super(app);
        defaultConfigurationFileName = "/config.properties";
    }

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

    @Override
    public void process(App app) {
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void dispose(App app) {
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
     * Extract values according to the known key. the recognised key will extract
     * Typed values to the internal values map.
     *
     * @param key   the configuration key
     * @param value the extracted String value.
     */
    private void extractConfigValue(String key, String value) {
        switch (key.toLowerCase()) {
            case "app.config", "config", "c" -> {
                defaultConfigurationFileName = value;
                info(ConfigurationService.class, "Configuration file name set to %s", defaultConfigurationFileName);
            }
            case "app.debug.level", "debuglevel", "dl" -> {
                values.put("app.debug.level", Integer.parseInt(value));
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
            default -> {
                warn(ConfigurationService.class, "Unknown argument %s:%s", key, value);
            }
        }
    }

    /**
     * Read the properties file and extract known keys/values.
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
        return (T) values.get(attrKeyName);
    }

}
