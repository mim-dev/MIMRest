package com.mim_development.android.mimrest.model.services.base.operation.response;


import com.mim_development.android.mimrest.exception.OperationException;
import com.mim_development.android.mimrest.model.services.base.service.response.ServiceErrorResponse;

import java.util.UUID;

public class OperationErrorResponse extends ServiceErrorResponse {

    private UUID identifier;

    public UUID getIdentifier() {
        return identifier;
    }

    public OperationErrorResponse(UUID identifier, OperationException operationException) {
        super(operationException);
        this.identifier = identifier;
    }
}
