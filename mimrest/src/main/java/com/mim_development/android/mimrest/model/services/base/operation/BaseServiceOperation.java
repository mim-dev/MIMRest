package com.mim_development.android.mimrest.model.services.base.operation;

import com.mim_development.android.mimrest.MIMRest;
import com.mim_development.android.mimrest.exception.OperationException;
import com.mim_development.android.mimrest.model.services.base.http.connection.HttpConnection;
import com.mim_development.android.mimrest.model.services.base.http.executor.HttpExecutorMonitor;
import com.mim_development.android.mimrest.model.services.base.http.response.HttpResponse;
import com.mim_development.android.mimrest.model.services.base.operation.callback.OperationCallback;
import com.mim_development.android.mimrest.model.services.base.operation.response.OperationErrorResponse;
import com.mim_development.android.mimrest.model.services.base.operation.response.OperationSuccessResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class BaseServiceOperation {

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

    abstract protected String getServiceAction();
    abstract protected HttpVerbs getHttpVerb();
    abstract protected Map<String, String> getRequestParameters();
    abstract protected OperationResultPayloadProcessor getOperationResultPayloadProcessor();

    abstract public void invoke();

    public UUID getIdentifier(){
        return identifier;
    }

    protected HttpExecutorMonitor getHttpExecutionMonitor(){
        return new HttpExecutorMonitorImpl(getOperationResultPayloadProcessor());
    }

    protected BaseServiceOperation(OperationCallback callback){
        this.callback = callback;
        identifier = UUID.randomUUID();
    }

    protected HttpConnection getConnection(){

        MIMRest mimRest = MIMRest.getInstance();

        return new HttpConnection(
                mimRest.isSecure(),
                mimRest.getServer(),
                mimRest.getServerApplicationPath(),
                getServiceAction());
    }

    protected int getConnectionTimeoutMillis(){
        return MIMRest.getInstance().getConnectionTimeOutMillis();
    }

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
