package com.merckgroup.framework;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.merckgroup.framework.services.Service;

/**
 * Simple Application strutucture to create java service based application
 * processing.
 * 
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class App {
    private String appName = "Demo01App";
    private Map<String, Service> services = new HashMap<>();

    public App() {
    }

    public void run(String[] args) {
        info(App.class, "Application start %s", appName);
        init(args);
        process();
        dispose();
        info(App.class, "Application %s stop", appName);
    }

    private void init(String[] args) {
        services.values().stream().sorted(Comparator.comparing(Service::getPriority)).forEach(s -> s.init(this, args));
    }

    private void process() {
        services.values().stream().sorted(Comparator.comparing(Service::getPriority)).forEach(s -> s.process(this));
    }

    private void dispose() {
        services.values().stream().sorted(Comparator.comparing(Service::getPriority)).forEach(s -> s.dispose(this));
    }

    private static void log(Class<?> className, String level, String message, Object... args) {
        System.out.printf("%s | %s | %s | %s%n", LocalDateTime.now(), level, className.getCanonicalName(),
                message.formatted(args));
    }

    public static void debug(Class<?> className, String message, Object... args) {
        log(className, "DEBUG", message, args);
    }

    public static void info(Class<?> className, String message, Object... args) {
        log(className, "INFO", message, args);
    }

    public static void warn(Class<?> className, String message, Object... args) {
        log(className, "WARN", message, args);
    }

    public static void error(Class<?> className, String message, Object... args) {
        log(className, "ERROR", message, args);
    }

    /**
     * Add a new {@link Service} to the {@link App} instance.
     * 
     * @param s the new {@link Service} instance to be added to the application.
     */
    protected void add(Service s) {
        this.services.put(s.getName(), s);
    }

    /**
     * default entry point for the applciation. Some arguments can be provided from
     * the command line.
     * 
     * @param args
     */
    public static void main(String[] args) {
        App app = new App();
        app.run(args);
    }

    public Service getService(String serviceName) {
        return services.get(serviceName);
    }
}
