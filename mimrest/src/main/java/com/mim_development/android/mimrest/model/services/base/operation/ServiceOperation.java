package com.mim_development.android.mimrest.model.services.base.operation;

import com.mim_development.android.mimrest.model.services.base.http.executor.HttpExecutor;
import com.mim_development.android.mimrest.model.services.base.http.executor.HttpExecutorMonitor;
import com.mim_development.android.mimrest.model.services.base.http.executor.HttpExecutorMonitorImpl;
import com.mim_development.android.mimrest.model.services.base.http.request.HttpRequest;
import com.mim_development.android.mimrest.model.services.base.operation.callback.OperationCallback;
import com.mim_development.android.mimrest.utility.StringUtil;

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

        String contentTypeHeaderValue = getContentTypeHeaderValue();
        String acceptTypeHeaderValue = getAcceptTypeHeaderValue();

        if (StringUtil.isNotEmpty(contentTypeHeaderValue)) {
            headerMap.put(CONTENT_TYPE_HEADER_NAME, contentTypeHeaderValue);
        }

        if (StringUtil.isNotEmpty(acceptTypeHeaderValue)) {
            headerMap.put(ACCEPT_HEADER_NAME, acceptTypeHeaderValue);
        }

        return headerMap;
    }

    /**
     * Provides the value to be used for the Content-Type header.
     * <p>
     * Override to change default value.  Return StringUtil.EMPTY to exclude the Content-Type header from being added.
     * </p>
     *
     * @return - the value to be used for the Content-Type header
     */
    protected String getContentTypeHeaderValue() {
        return CONTENT_TYPE_HEADER_VALUE;
    }

    /**
     * Provides the value to be used for the Accept-Type header.
     * <p>
     *     Override to change default value.  Return StringUtil.EMPTY to exclude the Accept-Type header from being added.
     * </p>
     * @return - the value to be used for the Accept-Type header
     */
    protected String getAcceptTypeHeaderValue() {
        return ACCEPT_HEADER_VALUE;
    }

    /**
     * Sets the cancelled state of the operation to true
     */
    public void cancel() {
        setCancelled(true);
    }
}