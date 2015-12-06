package com.mim_development.android.mimrest.model.services.base.operation.response;

import com.mim_development.android.mimrest.model.services.base.Payload;
import com.mim_development.android.mimrest.model.services.base.service.response.ServiceSuccessResponse;

import java.util.UUID;

public class OperationSuccessResponse extends ServiceSuccessResponse {

    private UUID identifier;

    public UUID getIdentifier() {
        return identifier;
    }

    public <T extends Payload> OperationSuccessResponse(
            UUID identifier, Class<T> type, T instance) {
        super(type, instance);
        this.identifier = identifier;
    }
}
