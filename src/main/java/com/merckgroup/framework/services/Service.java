package com.merckgroup.framework.services;

import com.merckgroup.framework.App;

import java.util.Map;

/**
 * A default Service interface to be manage by the {@link App} instance.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public interface Service {

    /**
     * Retrieve the {@link Service} name.
     *
     * @return
     */
    String getName();

    /**
     * Initialize the {@link Service} instance.
     *
     * @param app the parent owning app instance.
     */
    void init(App app, String[] args);

    /**
     * Processing the {@link Service}.
     *
     * @param app the parent owning app instance.
     */
    void process(App app);

    /**
     * Define the {@link Service} priority for execution order during
     * initialization, processing and disposing.
     *
     * @return
     */
    int getPriority();

    /**
     * disposing all the resources from this {@link Service}.
     *
     * @param app the parent owning app instance.
     */
    void dispose(App app);

    /**
     * Get service Statistics
     *
     * @return
     */
    Map<String, Object> getStats();
}
