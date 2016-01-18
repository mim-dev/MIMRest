package com.mim_development.android.mimrest.model.services.base.http.executor;

import com.mim_development.android.mimrest.exception.OperationException;
import com.mim_development.android.mimrest.model.services.base.http.response.HttpResponse;
import com.mim_development.android.mimrest.model.services.base.operation.OperationResultPayloadProcessor;
import com.mim_development.android.mimrest.model.services.base.operation.callback.OperationCallback;
import com.mim_development.android.mimrest.model.services.base.operation.response.OperationErrorResponse;
import com.mim_development.android.mimrest.model.services.base.operation.response.OperationSuccessResponse;

import java.util.UUID;

/**
 * Default implementation of {@link HttpExecutorMonitor}.
 */
public class HttpExecutorMonitorImpl implements HttpExecutorMonitor {

    private OperationResultPayloadProcessor payloadProcessor;
    private OperationCallback operationCallback;
    private boolean cancelled = false;
    private UUID identifier;

    /**
     * Sets the cancellation state of this monitor.
     * @return - the cancellation state to which to set this monitor
     */
    public synchronized void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Provides the cancellation state of this monitor.
     * @return - the cancellation state of this monitor
     */
    public synchronized boolean isCancelled() {
        return cancelled;
    }

    /**
     * Provides the unique identifier of this monitor.
     * @return - the unique identifier of this monitor
     */
    protected UUID getIdentifier() {
        return identifier;
    }

    /**
     * Provides the callback this monitor invokes upon completion.
     * @return - the callback this monitor invokes upon completion
     */
    protected OperationCallback getOperationCallback() {
        return operationCallback;
    }

    /**
     * Instance construction with requisite elements.
     * @param identifier - the unique identifier of this monitor
     * @param payloadProcessor - the payload processor invoked when data is received
     * @param operationCallback - the callback this monitor invokes upon completion
     */
    public HttpExecutorMonitorImpl(
            UUID identifier,
            OperationResultPayloadProcessor payloadProcessor,
            OperationCallback operationCallback) {
        this.identifier = identifier;
        this.payloadProcessor = payloadProcessor;
        this.operationCallback = operationCallback;
    }

    @Override
    public void result(HttpResponse response) {

        OperationCallback callback = getOperationCallback();
        if (callback != null && !isCancelled()) {

            UUID operationIdentifier = getIdentifier();
            int status = response.getStatus();

            if (status >= 200 && status < 300) {

                try {
                    OperationSuccessResponse successResponse = payloadProcessor.processResponse(
                            operationIdentifier, response.getPayload());
                    callback.success(successResponse);
                } catch (Exception e) {
                    callback.error(new OperationErrorResponse(
                            operationIdentifier,
                            new OperationException("Failed to process service response")));
                }
            } else {
                callback.error(new OperationErrorResponse(
                        operationIdentifier,
                        new OperationException("HTTP error code [" + response.getStatus() + "] received.")));
            }
        }
    }

    @Override
    public void error(Throwable throwable) {

        OperationCallback callback = getOperationCallback();
        if (callback != null && !isCancelled()) {
            callback.error(new OperationErrorResponse(
                    getIdentifier(),
                    new OperationException("HTTP operation exception received.", throwable)));
        }
    }

}