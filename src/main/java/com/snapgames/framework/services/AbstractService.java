package com.snapgames.framework.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.snapgames.framework.App;

/**
 * A default abstract {@link Service} implementation proposing default approach
 * by managing Entity to be processed.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public abstract class AbstractService implements Service {

    /**
     * The application instance associated with this service.
     * Used to access the application's functionality and manage the
     * entities being processed within the service.
     */
    protected App app;

    /**
     * A thread-safe map to store various statistical data for the service.
     * The keys represent specific metric identifiers or attributes, while the values
     * are the corresponding data points or objects related to those metrics. This
     * map is used to track and manage the operational metrics of the service.
     */
    protected Map<String, Object> statistics = new ConcurrentHashMap<>();

    /**
     * Constructs an AbstractService instance with the specified application.
     *
     * @param app the application instance associated with this service. It
     *            provides access to the application's functionality and
     *            manages the entities being processed within the service.
     */
    protected AbstractService(App app) {
        this.app = app;
    }


    /**
     * Retrieves the statistical data for the service.
     *
     * @return a map containing statistical data, where keys are metric identifiers
     * and values represent the corresponding metric values or objects.
     */
    public Map<String, Object> getStatistics() {
        return statistics;
    }

}
