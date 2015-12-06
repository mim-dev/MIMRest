package com.mim_development.android.mimrest.model.services.base.http.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {

    private int status;
    private Map<String, List<String>> headers;
    private byte[] payload;

    public HttpResponse(Map<String, List<String>> headers, byte[] payload, int status) {
        this.headers = headers;
        this.payload = payload;
        this.status = status;
    }

    public byte[] getPayload() {
        byte[] result = Arrays.copyOf(payload, payload.length);
        return result;
    }

    public Map<String, List<String>> getHeaders(){
        Map<String, List<String>> result = new HashMap<>(headers.size());

        for(String headerName : headers.keySet()){

            List<String> src = headers.get(headerName);

            List<String> headerValues;

            if(src != null) {
                headerValues = new ArrayList<>(src.size());
                headerValues.addAll(src);
            } else{
                headerValues = new ArrayList<>(0);
            }

            result.put(headerName, headerValues);
        }

        return result;
    }

    public int getStatus() {
        return status;
    }

}
