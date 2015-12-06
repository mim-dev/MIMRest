package com.mim_development.android.mimrest.model.services.base.operation;

import com.mim_development.android.mimrest.model.services.base.http.executor.HttpPayloadExecutor;
import com.mim_development.android.mimrest.model.services.base.http.request.HttpPayloadRequest;
import com.mim_development.android.mimrest.model.services.base.operation.callback.OperationCallback;

public abstract class RequestPayloadServiceOperation extends BaseServiceOperation {

    protected RequestPayloadServiceOperation(OperationCallback callback){
        super(callback);
    }

    @Override
    public void invoke(){
        HttpPayloadRequest request = getHttpPayloadRequest();
        HttpPayloadExecutor executor = new HttpPayloadExecutor(request, getHttpExecutionMonitor());
        Thread executorThread = new Thread(executor);
        executorThread.start();
    }

    protected HttpPayloadRequest getHttpPayloadRequest(){
        return new HttpPayloadRequest(getConnection(),
                getHttpVerb(),
                buildRequestHeaders(),
                getConnectionTimeoutMillis(),
                getRequestParameters(),
                getRequestPayload());
    }

    protected abstract byte[] getRequestPayload();
}
