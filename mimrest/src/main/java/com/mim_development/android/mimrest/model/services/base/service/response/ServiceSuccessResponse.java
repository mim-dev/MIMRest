package com.mim_development.android.mimrest.model.services.base.service.response;

import com.mim_development.android.mimrest.model.services.base.Payload;

import java.util.HashMap;
import java.util.Map;

public class ServiceSuccessResponse {
    private Map<Class<?>, Object> payload;

    public <T extends Payload> ServiceSuccessResponse(Class<T> payloadType, T instance){
        if(payloadType == null){
            throw new NullPointerException("type cannot be null");
        }

        payload = new HashMap<>(1);

        payload.put(payloadType, instance);
    }

    public <T extends Payload> T getPayload(Class<T> payloadType) {

        if(payload.size() != 1){
            try {
                return payloadType.newInstance();
            } catch (InstantiationException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        }

        return payloadType.cast(payload.get(payloadType));
    }
}
