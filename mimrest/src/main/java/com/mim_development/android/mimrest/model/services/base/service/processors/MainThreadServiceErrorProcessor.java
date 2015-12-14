package com.mim_development.android.mimrest.model.services.base.service.processors;


import android.os.Handler;

import com.mim_development.android.mimrest.MIMRest;
import com.mim_development.android.mimrest.model.services.base.operation.callback.MainThreadServiceCallbackProcessor;
import com.mim_development.android.mimrest.model.services.base.service.callback.ServiceCallback;
import com.mim_development.android.mimrest.model.services.base.service.response.ServiceErrorResponse;

import java.util.UUID;

public class MainThreadServiceErrorProcessor implements MainThreadServiceCallbackProcessor {

    private UUID operationIdentifier;
    private ServiceErrorResponse serviceErrorResponse;
    private ServiceCallback serviceCallback;

    public MainThreadServiceErrorProcessor(
            UUID operationIdentifier,
            ServiceErrorResponse serviceErrorResponse,
            ServiceCallback serviceCallback) {
        this.operationIdentifier = operationIdentifier;
        this.serviceErrorResponse = serviceErrorResponse;
        this.serviceCallback = serviceCallback;
    }

    @Override
    public void processResultOnMainThread() {
        Handler mainThreadHandler =
                new Handler(MIMRest.getInstance().getApplicationContext().getMainLooper());
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                serviceCallback.error(operationIdentifier, serviceErrorResponse);
            }
        });
    }
}
