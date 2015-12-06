package com.mim_development.android.mimrest.model.services.base.service.callback;

import com.mim_development.android.mimrest.model.services.base.service.response.ServiceErrorResponse;
import com.mim_development.android.mimrest.model.services.base.service.response.ServiceSuccessResponse;

import java.util.UUID;

public interface ServiceCallback {
    void success(UUID operationId, ServiceSuccessResponse response);
    void error(UUID operationId, ServiceErrorResponse response);
}
