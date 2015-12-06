package com.mim_development.android.mimrest.model.services.base.service;


import com.mim_development.android.mimrest.model.services.base.operation.BaseServiceOperation;
import com.mim_development.android.mimrest.model.services.base.operation.callback.OperationCallback;
import com.mim_development.android.mimrest.model.services.base.operation.response.OperationErrorResponse;
import com.mim_development.android.mimrest.model.services.base.operation.response.OperationSuccessResponse;
import com.mim_development.android.mimrest.model.services.base.service.callback.ServiceCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Service {

    private Map<UUID, OperationContainer> operations;
    private Object operationsMapLock = new Object();

    protected OperationCallback getOperationCallback(){
        return new OperationCallback() {
            @Override
            public void success(OperationSuccessResponse response) {
                UUID operationIdentifier = response.getIdentifier();

                ServiceCallback serviceCallback
                        = fetchCallbackAndDeleteOperation(operationIdentifier);

                if (serviceCallback != null) {
                    serviceCallback.success(operationIdentifier, response);
                }
            }

            @Override
            public void error(OperationErrorResponse response) {
                UUID operationIdentifier = response.getIdentifier();

                ServiceCallback serviceCallback
                        = fetchCallbackAndDeleteOperation(operationIdentifier);

                if (serviceCallback != null) {
                    serviceCallback.error(operationIdentifier, response);
                }
            }
        };
    }

    protected Service(){
        operations = new HashMap<>(10);
    }

    protected void addOperation(BaseServiceOperation op, ServiceCallback callback) {

        UUID operationIdentifier = op.getIdentifier();
        OperationContainer opContainer = new OperationContainer(
                op, callback);

        synchronized (operationsMapLock) {
            operations.put(operationIdentifier, opContainer);
        }
    }

    protected void removeOperation(UUID operationIdentifier) {
        synchronized (operationsMapLock) {
            if (operations.containsKey(operationIdentifier)) {
                operations.remove(operationIdentifier);
            }
        }
    }

    protected ServiceCallback fetchCallbackAndDeleteOperation(UUID operationIdentifier) {

        OperationContainer opContainer = null;
        ServiceCallback operationServiceCallback = null;

        synchronized (operationsMapLock) {
            if (operations.containsKey(operationIdentifier)) {
                opContainer = operations.get(operationIdentifier);
                if (opContainer != null) {
                    operations.remove(operationIdentifier);
                }
            }
        }

        if (opContainer != null) {
            operationServiceCallback = opContainer.getCallback();
        }

        return operationServiceCallback;
    }

    protected UUID invokeOperation(BaseServiceOperation op, ServiceCallback callback){
        UUID identifier = op.getIdentifier();
        addOperation(op, callback);
        op.invoke();
        return identifier;
    }

    public void cancelOperation(UUID operationIdentifier) {
        removeOperation(operationIdentifier);
    }
}
