package com.snapgames.framework;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.snapgames.framework.services.Service;

/**
 * Simple Application structure to create java-service-based application
 * processing.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class App {
    protected String appName = "App";
    private Map<String, Service> services = new HashMap<>();
    private boolean exit = false;
    private boolean pause = false;
    private long maxLoopCount = -1;

    private int debug = 0;

    public App() {
    }

    public void run(String[] args) {
        info(App.class, "Application start");
        init(args);
        info(App.class, "Application named \"%s\"", appName);
        process();
        dispose();
        info(App.class, "Application %s stop", appName);
    }

    private void init(String[] args) {
        services.values().stream().sorted(Comparator.comparing(Service::getPriority)).forEach(s -> s.init(this, args));
    }

    private void process() {
        long loopCount = 0;
        while (!exit && !(maxLoopCount != -1 && loopCount > maxLoopCount)) {
            if (!pause) {
                services.values().stream().sorted(Comparator.comparing(Service::getPriority)).forEach(s -> s.process(this));
                loopCount++;
            }
        }
    }

    /**
     * Aggregate all Services statistics into one Map.
     *
     * @return a {@link Map} of all statistics.
     */
    public Map<String, Object> getServicesStatistics() {
        Map<String, Object> statistics = services.values().stream()
                .map(Service::getStats) // Récupérer les statistiques de chaque service
                .flatMap(stats -> stats.entrySet().stream()) // Aplatir les entrées de statistiques
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // Clé de la statistique
                        Map.Entry::getValue, // Valeur de la statistique
                        (value1, value2) -> {
                            // Combiner les valeurs si la clé existe déjà
                            // Ici, vous pouvez définir comment combiner les valeurs, par exemple, en additionnant
                            if (value1 instanceof Number && value2 instanceof Number) {
                                return ((Number) value1).doubleValue() + ((Number) value2).doubleValue();
                            }
                            return value1; // Ou une autre logique de combinaison
                        }
                ));
        return statistics;
    }

    /**
     * Retrieve all statistics filtered on keys with keyFiltering.
     *
     * @param keyFiltering the string filtering key to collect only corresponding values.
     * @return a {@link Map} of the corresponding statistics.
     */
    public Map<String, Object> filterStatisticsOn(String keyFiltering) {
        return getServicesStatistics().entrySet()
                .stream().filter((e) -> e.getKey().contains(keyFiltering))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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

    public <T extends Service> T getService(String serviceName) {
        return (T) services.get(serviceName);
    }

    public void requestExit(boolean exit) {
        this.exit = exit;
    }

    /**
     * Default entry point for the application. Some arguments can be provided from
     * the command line.
     *
     * @param args the list of arguments coming from the Java command line.
     */
    public static void main(String[] args) {
        App app = new App();
        app.run(args);
    }

    public void setAppName(String name) {
        this.appName = name;
    }

    public void setTestLoopCounter(long maxLoop) {
        this.maxLoopCount = maxLoop;
    }

    public void setPause(boolean p) {
        this.pause = p;
    }

    public boolean isDebugLevelGreaterThan(int debugLevel) {
        return debug > debugLevel;
    }
}
