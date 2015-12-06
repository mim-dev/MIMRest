package com.mim_development.android.mimrest.model.services.base.operation;

import com.mim_development.android.mimrest.model.services.base.operation.response.OperationSuccessResponse;

import java.util.UUID;

public interface OperationResultPayloadProcessor {
    OperationSuccessResponse processResponse(
            UUID identifier, byte[] responsePayload) throws Exception;
}
