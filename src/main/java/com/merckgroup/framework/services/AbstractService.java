package com.merckgroup.framework.services;

import com.merckgroup.framework.App;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A default abstract {@link Service} implementation proposing default approach
 * by managing Entity to be processed.
 *
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public abstract class AbstractService implements Service {

    protected App app;

    protected Map<String, Object> statistics = new ConcurrentHashMap<>();

    protected AbstractService(App app) {
        this.app = app;
    }


    public Map<String, Object> getStatistics() {
        return statistics;
    }

}
