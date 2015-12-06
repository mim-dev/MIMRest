package com.mim_development.android.mimrest.model.services.base.http.executor;

import com.mim_development.android.mimrest.model.services.base.http.response.HttpResponse;

/**
 * Defines the behavior for monitoring the results of an HTTP request execution
 */
public interface HttpExecutorMonitor {

    /**
     * Processes a successful HTTP execution request.
     * <p>Success does not mean that the HTTP request itself actually succeeded, only that the call completed without any exceptions.</p>
     * @param response - the results of the request.
     */
    void result(HttpResponse response);

    /**
     * Processes the results of an HTTP request execution that ended with the generation of a throwable.
     * @param throwable
     */
    void error(Throwable throwable);
}
