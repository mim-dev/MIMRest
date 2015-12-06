package com.mim_development.android.mimrest.model.services.base.operation;

import com.mim_development.android.mimrest.model.services.base.http.executor.HttpExecutor;
import com.mim_development.android.mimrest.model.services.base.http.request.HttpRequest;
import com.mim_development.android.mimrest.model.services.base.operation.callback.OperationCallback;

public abstract class ServiceOperation extends BaseServiceOperation {

    protected ServiceOperation(OperationCallback callback){
        super(callback);
    }

    @Override
    public void invoke(){
        HttpRequest request = getHttpRequest();
        HttpExecutor executor = new HttpExecutor(request, getHttpExecutionMonitor());
        Thread executorThread = new Thread(executor);
        executorThread.start();
    }

    protected HttpRequest getHttpRequest(){
        return new HttpRequest(
                getConnection(),
                getHttpVerb(),
                buildRequestHeaders(),
                getConnectionTimeoutMillis(),
                getRequestParameters());
    }
}
