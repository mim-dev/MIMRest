package com.mim_development.android.mimrest.model.services.base.operation;

import com.mim_development.android.mimrest.model.services.base.http.executor.HttpExecutor;
import com.mim_development.android.mimrest.model.services.base.http.executor.HttpExecutorMonitor;
import com.mim_development.android.mimrest.model.services.base.http.executor.HttpExecutorMonitorImpl;
import com.mim_development.android.mimrest.model.services.base.http.request.HttpRequest;
import com.mim_development.android.mimrest.model.services.base.operation.callback.OperationCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ServiceOperation {

    private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    private static final String CONTENT_TYPE_HEADER_VALUE = "application/json";

    private static final String ACCEPT_HEADER_NAME = "Accept";
    private static final String ACCEPT_HEADER_VALUE = "application/json";
    private HttpExecutorMonitorImpl httpExecutorMonitorImpl;

    protected OperationCallback operationCallback;
    private UUID identifier;
    private boolean cancelled;

    /**
     * Provides the unique identifier of this operation.
     * @return - the unique identifier of this operation
     */
    public UUID getIdentifier() {
        return identifier;
    }

    protected synchronized boolean isCancelled() {
        return cancelled;
    }

    protected synchronized void setCancelled(boolean value) {
        cancelled = value;
        if(httpExecutorMonitorImpl != null){
            httpExecutorMonitorImpl.setCancelled(cancelled);
        }
    }

    protected OperationCallback getOperationCallback(){
        return operationCallback;
    }

    protected ServiceOperation(OperationCallback operationCallback) {
        this.operationCallback = operationCallback;
        identifier = UUID.randomUUID();
    }

    abstract protected OperationResultPayloadProcessor getOperationResultPayloadProcessor();

    protected HttpExecutorMonitor getHttpExecutorMonitor() {

        if(httpExecutorMonitorImpl == null){
            httpExecutorMonitorImpl = new HttpExecutorMonitorImpl(
                    getIdentifier(), getOperationResultPayloadProcessor(), getOperationCallback());
        }

        return httpExecutorMonitorImpl;
    }

    public void invoke() {
        HttpRequest request = getHttpRequest();
        HttpExecutor executor = new HttpExecutor(request, getHttpExecutorMonitor());
        Thread executorThread = new Thread(executor);
        executorThread.start();
    }

    protected abstract HttpRequest getHttpRequest();

    protected Map<String, String> buildRequestHeaders() {
        Map<String, String> headerMap = new HashMap<>(2);
        headerMap.put(CONTENT_TYPE_HEADER_NAME, getContentTypeHeaderValue());
        headerMap.put(ACCEPT_HEADER_NAME, getAcceptTypeHeaderValue());
        return headerMap;
    }

    protected String getContentTypeHeaderValue() {
        return CONTENT_TYPE_HEADER_VALUE;
    }

    protected String getAcceptTypeHeaderValue() {
        return ACCEPT_HEADER_VALUE;
    }

    public void cancel() {
        setCancelled(true);
    }
}