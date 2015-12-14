package com.mim_development.android.mimrest.model.services.base.service.processors;


import android.os.Handler;

import com.mim_development.android.mimrest.MIMRest;
import com.mim_development.android.mimrest.model.services.base.operation.callback.MainThreadServiceCallbackProcessor;
import com.mim_development.android.mimrest.model.services.base.service.callback.ServiceCallback;
import com.mim_development.android.mimrest.model.services.base.service.response.ServiceSuccessResponse;

import java.util.UUID;

public class MainThreadServiceSuccessProcessor implements MainThreadServiceCallbackProcessor {

    private UUID operationIdentifier;
    private ServiceSuccessResponse serviceSuccessResponse;
    private ServiceCallback serviceCallback;

    public MainThreadServiceSuccessProcessor(
            UUID operationIdentifier,
            ServiceSuccessResponse serviceSuccessResponse,
            ServiceCallback serviceCallback) {
        this.operationIdentifier = operationIdentifier;
        this.serviceSuccessResponse = serviceSuccessResponse;
        this.serviceCallback = serviceCallback;
    }

    @Override
    public void processResultOnMainThread() {
        Handler mainThreadHandler =
                new Handler(MIMRest.getInstance().getApplicationContext().getMainLooper());
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                serviceCallback.success(operationIdentifier, serviceSuccessResponse);
            }
        });
    }
}
