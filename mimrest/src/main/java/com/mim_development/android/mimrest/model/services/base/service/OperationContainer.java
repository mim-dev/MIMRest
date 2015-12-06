package com.mim_development.android.mimrest.model.services.base.service;


import com.mim_development.android.mimrest.model.services.base.operation.ServiceOperation;
import com.mim_development.android.mimrest.model.services.base.service.callback.ServiceCallback;

public class OperationContainer {

    private ServiceOperation op;
    private ServiceCallback callback;

    public OperationContainer(ServiceOperation op, ServiceCallback callback) {
        this.op = op;
        this.callback = callback;
    }

    public ServiceOperation getOp() {
        return op;
    }

    public ServiceCallback getCallback() {
        return callback;
    }
}
