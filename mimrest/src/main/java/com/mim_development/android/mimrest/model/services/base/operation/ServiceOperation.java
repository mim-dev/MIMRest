package com.mim_development.android.mimrest.model.services.base.operation;

import com.mim_development.android.mimrest.exception.OperationException;
import com.mim_development.android.mimrest.model.services.base.http.executor.HttpExecutor;
import com.mim_development.android.mimrest.model.services.base.http.executor.HttpExecutorMonitor;
import com.mim_development.android.mimrest.model.services.base.http.request.HttpRequest;
import com.mim_development.android.mimrest.model.services.base.http.response.HttpResponse;
import com.mim_development.android.mimrest.model.services.base.operation.callback.OperationCallback;
import com.mim_development.android.mimrest.model.services.base.operation.response.OperationErrorResponse;
import com.mim_development.android.mimrest.model.services.base.operation.response.OperationSuccessResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ServiceOperation  {

    private class HttpExecutorMonitorImpl implements HttpExecutorMonitor {

        private OperationResultPayloadProcessor payloadProcessor;

        public HttpExecutorMonitorImpl(
                OperationResultPayloadProcessor payloadProcessor){
            this.payloadProcessor = payloadProcessor;
        }

        @Override
        public void result(HttpResponse response) {
            if(callback != null){

                UUID operationIdentifier = getIdentifier();
                int status = response.getStatus();

                if(status >= 200 && status < 300){

                    try {
                        OperationSuccessResponse successResponse = payloadProcessor.processResponse(
                                operationIdentifier, response.getPayload());
                        callback.success(successResponse);
                    } catch (Exception e) {
                        callback.error(new OperationErrorResponse(
                                operationIdentifier,
                                new OperationException("Failed to process service response")));
                    }
                } else{
                    callback.error(new OperationErrorResponse(
                            operationIdentifier,
                            new OperationException("HTTP error code [" + response.getStatus() + "] received.")));
                }
            }
        }

        @Override
        public void error(Throwable throwable) {
            if(callback != null){
                callback.error(new OperationErrorResponse(
                        getIdentifier(),
                        new OperationException("HTTP operation exception received.", throwable)));
            }
        }
    }

    protected OperationCallback callback;
    private UUID identifier;

    public UUID getIdentifier(){
        return identifier;
    }

    protected ServiceOperation(OperationCallback callback){
        this.callback = callback;
        identifier = UUID.randomUUID();
    }

    abstract protected OperationResultPayloadProcessor getOperationResultPayloadProcessor();

    public void invoke(){
        HttpRequest request = getHttpRequest();
        HttpExecutor executor = new HttpExecutor(request, new HttpExecutorMonitorImpl(getOperationResultPayloadProcessor()));
        Thread executorThread = new Thread(executor);
        executorThread.start();
    }

    protected abstract HttpRequest getHttpRequest();

    protected Map<String, String> buildRequestHeaders(){
        Map<String, String> headerMap = new HashMap<>(2);
        headerMap.put("Content-Type", "Application/JSON");
        headerMap.put("Accept", "Application/JSON");
        return headerMap;
    }

    public void cancel(){
        ;       // do nothing for now
    }
}
