package com.mim_development.android.mimrest.model.services.base.operation.callback;

import com.mim_development.android.mimrest.model.services.base.operation.response.OperationErrorResponse;
import com.mim_development.android.mimrest.model.services.base.operation.response.OperationSuccessResponse;

public interface OperationCallback {
    void success(final OperationSuccessResponse response);

    void error(final OperationErrorResponse response);
}
