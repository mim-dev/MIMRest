package com.mim_development.android.mimrest.model.services.base.service;


import com.mim_development.android.mimrest.model.services.base.operation.ServiceOperation;
import com.mim_development.android.mimrest.model.services.base.operation.callback.OperationCallback;
import com.mim_development.android.mimrest.model.services.base.operation.response.OperationErrorResponse;
import com.mim_development.android.mimrest.model.services.base.operation.response.OperationSuccessResponse;
import com.mim_development.android.mimrest.model.services.base.service.callback.ServiceCallback;
import com.mim_development.android.mimrest.model.services.base.service.processors.MainThreadServiceErrorProcessor;
import com.mim_development.android.mimrest.model.services.base.service.processors.MainThreadServiceSuccessProcessor;
import com.mim_development.android.mimrest.model.services.base.service.response.ServiceErrorResponse;
import com.mim_development.android.mimrest.model.services.base.service.response.ServiceSuccessResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Service {

    private Map<UUID, OperationContainer> operations;
    private final Object operationsMapLock = new Object();

    protected OperationCallback getOperationCallback() {
        return new OperationCallback() {
            @Override
            public void success(final OperationSuccessResponse response) {
                final UUID operationIdentifier = response.getIdentifier();

                OperationContainer opContainer
                        = fetchAndDeleteOperationContainer(operationIdentifier);

                if (opContainer != null) {
                    final ServiceCallback serviceCallback = opContainer.getCallback();
                    getMainThreadServiceSuccessProcessor(
                            operationIdentifier,
                            response,
                            serviceCallback).processResultOnMainThread();
                }
            }

            @Override
            public void error(final OperationErrorResponse response) {
                final UUID operationIdentifier = response.getIdentifier();

                OperationContainer opContainer
                        = fetchAndDeleteOperationContainer(operationIdentifier);

                if (opContainer != null) {
                    final ServiceCallback serviceCallback = opContainer.getCallback();
                    getMainThreadServiceErrorProcessor(
                            operationIdentifier,
                            response,
                            serviceCallback).processResultOnMainThread();
                }
            }
        };
    }

    protected MainThreadServiceSuccessProcessor getMainThreadServiceSuccessProcessor(
            UUID operationIdentifier,
            ServiceSuccessResponse serviceSuccessResponse,
            ServiceCallback serviceCallback) {
        return new MainThreadServiceSuccessProcessor(
                operationIdentifier,
                serviceSuccessResponse,
                serviceCallback);
    }

    protected MainThreadServiceErrorProcessor getMainThreadServiceErrorProcessor(
            UUID operationIdentifier,
            ServiceErrorResponse serviceErrorResponse,
            ServiceCallback serviceCallback) {
        return new MainThreadServiceErrorProcessor(
                operationIdentifier,
                serviceErrorResponse,
                serviceCallback);
    }

    protected Service() {
        operations = new HashMap<>(10);
    }

    protected void addOperation(ServiceOperation op, ServiceCallback callback) {

        UUID operationIdentifier = op.getIdentifier();
        OperationContainer opContainer = new OperationContainer(
                op, callback);

        synchronized (operationsMapLock) {
            operations.put(operationIdentifier, opContainer);
        }
    }

    protected OperationContainer fetchAndDeleteOperationContainer(UUID operationIdentifier) {

        OperationContainer opContainer = null;

        synchronized (operationsMapLock) {
            if (operations.containsKey(operationIdentifier)) {
                opContainer = operations.get(operationIdentifier);
                if (opContainer != null) {
                    operations.remove(operationIdentifier);
                }
            }
        }

        return opContainer;
    }

    protected UUID invokeOperation(ServiceOperation op, ServiceCallback callback) {
        UUID identifier = op.getIdentifier();
        addOperation(op, callback);
        op.invoke();
        return identifier;
    }

    public void cancelOperation(UUID operationIdentifier) {
        OperationContainer opContainer = fetchAndDeleteOperationContainer(operationIdentifier);
        if (opContainer != null) {
            opContainer.getOp().cancel();

        }
    }
}
