package com.snapgames.framework;

import com.snapgames.framework.services.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The {@code App} class serves as the base implementation of a service-oriented
 * application. It facilitates the initialization, processing, and disposal of
 * various {@link Service} instances within the application lifecycle.
 * <p>
 * This application lifecycle includes:
 * - Initialization of all registered services.
 * - Processing of each service in a controlled loop based on certain conditions.
 * - Graceful disposal of services and resources upon application exit.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public class App {
    /**
     * Represents the name of the application.
     * This variable is used to identify the application instance
     * and can be modified using the setAppName method.
     */
    protected String appName = "App";
    /**
     * A collection of {@link Service} instances managed by the {@link App} class.
     * The services are stored in a {@link Map} where the key represents the name
     * of the service, and the value is the {@link Service} instance. This map
     * provides centralized management of all services used in the application,
     * enabling operations such as initialization, processing, and disposal in
     * accordance with their defined priorities.
     */
    private Map<String, Service> services = new HashMap<>();
    /**
     * This variable indicates whether the application should exit or continue running.
     * It is used as a flag to control the main processing loop.
     * When set to {@code true}, the application will terminate its operation.
     */
    private boolean exit = false;
    /**
     * Indicates whether the application processing is paused.
     * <p>
     * When set to true, the application will temporarily halt processing operations
     * during the execution loop in the {@code process} method of the {@code App} class.
     * Setting this to false will resume processing.
     * <p>
     * Default value is {@code false}.
     */
    private boolean pause = false;
    /**
     * This variable defines the maximum number of iterations the application can
     * process within the `process` loop of the {@link App}. A value of -1 indicates
     * that there is no limit on the number of iterations and the application will
     * process indefinitely until another condition ends the loop.
     * <p>
     * It is used in combination with the `exit` variable and the `pause` state to
     * control the application's main execution loop.
     */
    private long maxLoopCount = -1;

    /**
     * Debug level used to control the verbosity of application logs.
     * A value of 0 typically represents no debugging, whereas higher values
     * enable more detailed debug information.
     */
    private int debug = 0;

    /**
     * Default constructor for the {@code App} class. This initializes the application
     * with default settings and prepares it to manage services, handle the run
     * lifecycle, and respond to requests such as processing and exiting.
     */
    public App() {
    }

    /**
     * Executes the main lifecycle of the application, including initialization, processing,
     * and cleanup of its services.
     *
     * @param args the command-line arguments provided to the application. These arguments
     *             will be passed to the application and its services for initialization and
     *             configuration.
     */
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
                services.values().stream().sorted(Comparator.comparing(Service::getPriority))
                        .forEach(s -> s.process(this));
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
        Map<String, Object> statistics = services.values().stream().map(Service::getStats) // Récupérer les statistiques
                // de chaque service
                .flatMap(stats -> stats.entrySet().stream()) // Aplatir les entrées de statistiques
                .collect(Collectors.toMap(Map.Entry::getKey, // Clé de la statistique
                        Map.Entry::getValue, // Valeur de la statistique
                        (value1, value2) -> {
                            // Combiner les valeurs si la clé existe déjà
                            // Ici, vous pouvez définir comment combiner les valeurs, par exemple, en
                            // additionnant
                            if (value1 instanceof Number && value2 instanceof Number) {
                                return ((Number) value1).doubleValue() + ((Number) value2).doubleValue();
                            }
                            return value1; // Ou une autre logique de combinaison
                        }));
        return statistics;
    }

    /**
     * Retrieve all statistics filtered on keys with keyFiltering.
     *
     * @param keyFiltering the string filtering key to collect only corresponding
     *                     values.
     * @return a {@link Map} of the corresponding statistics.
     */
    public Map<String, Object> filterStatisticsOn(String keyFiltering) {
        return getServicesStatistics().entrySet().stream().filter((e) -> e.getKey().contains(keyFiltering))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Disposes of all registered services in the application by invoking the
     * {@code dispose} method of each {@link Service} instance. Services are
     * processed in the order of their priority, as defined by the {@link Service#getPriority()}.
     * The {@link Service#dispose(App)} method is called for each service, passing the current
     * application instance as a parameter.
     * <p>
     * This method ensures the proper cleanup of resources used by the services,
     * following the reverse order of priority compared to their initialization
     * and processing logic.
     */
    private void dispose() {
        services.values().stream().sorted(Comparator.comparing(Service::getPriority)).forEach(s -> s.dispose(this));
    }

    /**
     * Logs a formatted message with the specified logging level and class context.
     *
     * @param className the {@link Class} object representing the context of the log message.
     * @param level     the level of the log message (e.g., "DEBUG", "INFO", "WARN", "ERROR").
     * @param message   the log message template with placeholders for arguments.
     * @param args      the arguments to be formatted into the message template.
     */
    private static void log(Class<?> className, String level, String message, Object... args) {
        System.out.printf("%s | %s | %s | %s%n", LocalDateTime.now(), level, className.getCanonicalName(),
                message.formatted(args));
    }

    /**
     * Logs a debug message with the specified class context and message format.
     *
     * @param className the {@link Class} object representing the context of the log message.
     * @param message   the log message template with placeholders for arguments.
     * @param args      the arguments to be formatted into the message template.
     */
    public static void debug(Class<?> className, String message, Object... args) {
        log(className, "DEBUG", message, args);
    }

    /**
     * Logs an informational message with the specified class context and message format.
     *
     * @param className the {@link Class} object representing the context of the log message.
     * @param message   the log message template with placeholders for arguments.
     * @param args      the arguments to be formatted into the message template.
     */
    public static void info(Class<?> className, String message, Object... args) {
        log(className, "INFO", message, args);
    }

    /**
     * Logs a warning message with the specified class context and message format.
     *
     * @param className the {@link Class} object representing the context of the log message.
     * @param message   the log message template with placeholders for arguments.
     * @param args      the arguments to be formatted into the message template.
     */
    public static void warn(Class<?> className, String message, Object... args) {
        log(className, "WARN", message, args);
    }

    /**
     * Logs an error message with the specified class context and message format.
     *
     * @param className the {@link Class} object representing the context of the log message.
     * @param message   the log message template with placeholders for arguments.
     * @param args      the arguments to be formatted into the message template.
     */
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
     * Retrieves a service instance by its name from the application's service registry.
     * The service must implement the {@link Service} interface and is cast to the desired
     * type specified by the generic type parameter.
     *
     * @param <T>         a type that extends {@link Service}, representing the type of the
     *                    service to be retrieved.
     * @param serviceName the name of the service to retrieve, as registered within the
     *                    application.
     * @return the {@link Service} instance cast to the specified type, or {@code null} if
     * no service with the given name exists in the registry.
     */
    public <T extends Service> T getService(String serviceName) {
        return (T) services.get(serviceName);
    }

    /**
     * Requests the application to change its exit state. Setting this flag to {@code true}
     * will indicate that the application should exit its lifecycle.
     *
     * @param exit a boolean value indicating whether to request exiting the application.
     *             If {@code true}, an exit request is made; otherwise, the application continues running.
     */
    public void requestExit(boolean exit) {
        this.exit = exit;
        info(App.class, "exit has been requested");
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

    /**
     * Sets the name of the application.
     *
     * @param name the name to be assigned to the application.
     */
    public void setAppName(String name) {
        this.appName = name;
    }

    /**
     * Sets the maximum number of loops to be executed during the test.
     *
     * @param maxLoop the maximum number of iterations for the test loop.
     */
    public void setTestLoopCounter(long maxLoop) {
        this.maxLoopCount = maxLoop;
    }

    /**
     * Sets the pause state for the application lifecycle.
     *
     * @param p a boolean value indicating whether the application should pause.
     *          If {@code true}, the application pauses its processing;
     *          otherwise, it resumes.
     */
    public void setPause(boolean p) {
        this.pause = p;
    }

    /**
     * Checks if the current debug level of the application is greater than the specified debug level.
     *
     * @param debugLevel the debug level to compare against the current debug level of the application
     * @return {@code true} if the current debug level is greater than the specified debug level,
     * {@code false} otherwise
     */
    public boolean isDebugLevelGreaterThan(int debugLevel) {
        return debug > debugLevel;
    }

    /**
     * Sets the debug level for the application.
     *
     * @param dl the integer value representing the desired debug level to be set.
     */
    public void setDebugLevel(int dl) {
        this.debug = dl;
    }

    /**
     * Retrieves the current debug level of the application.
     *
     * @return an integer representing the current debug level.
     */
    public int getDebugLevel() {
        return debug;
    }

}
