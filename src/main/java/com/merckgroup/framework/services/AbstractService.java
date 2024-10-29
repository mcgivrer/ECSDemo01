package com.merckgroup.framework.services;

import com.merckgroup.framework.App;

/**
 * A default abstract {@link Service} implementation proposing default approach
 * by managing Entity to be processed.
 * 
 * @author Frédéric Delorme
 * @since 0.0.1
 */
public abstract class AbstractService implements Service {

    protected App app;

    protected AbstractService(App app) {
        this.app = app;
    }

}
