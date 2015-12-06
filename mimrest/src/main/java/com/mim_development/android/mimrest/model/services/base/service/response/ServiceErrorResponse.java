package com.mim_development.android.mimrest.model.services.base.service.response;


import com.mim_development.android.mimrest.exception.OperationException;

public class ServiceErrorResponse {

    private OperationException exception;

    public OperationException getException() {
        return exception;
    }

    public ServiceErrorResponse(OperationException exception) {
        this.exception = exception;
    }
}
