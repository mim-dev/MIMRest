package com.mim_development.android.mimrest.model.services.base.http.executor;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base engine for executing HTTP requests
 */
public abstract class BaseHttpExecutor {

    private static final String CONTENT_LENGTH_HEADER_NAME = "Content-Length";

    /**
     *
     * @return
     */
    protected abstract Map<String, String> getHeaders();
    private HttpExecutorMonitor monitor;

    /**
     * Instance construction.
     * @param monitor - instance monitoring the success / error completion of the HTTP request.
     */
    protected BaseHttpExecutor(final HttpExecutorMonitor monitor) {
        this.monitor = monitor;
    }

    /**
     * Provides the query string parameters to be included with the request.
     * @return - query string name / value pairs.
     */
    protected abstract Map<String, String> getParameters();

    /**
     * Adds HTTP connection headers.
     * @param connection - the connection information to be used during the execution of the request.
     */
    protected void addHeaders(HttpURLConnection connection) {
        Map<String, String> headers = getHeaders();

        if(headers != null) {
            for (String headerName : headers.keySet()) {
                connection.setRequestProperty(headerName, headers.get(headerName));
            }
        }
    }

    /**
     * Provides access to the instance monitoring success / error completion of the HTTP request.
     * @return - HTTP request monitor.
     */
    protected HttpExecutorMonitor getMonitor() {
        return monitor;
    }


    protected byte[] processResponseContent(
            Map<String, List<String >> responseHeaders, InputStream stream) throws IOException{

        byte[] responseBytes;

        List<String> contentLengthHeaderValues = responseHeaders.get(CONTENT_LENGTH_HEADER_NAME);

        if(contentLengthHeaderValues == null || contentLengthHeaderValues.size() != 1){
            responseBytes = new byte[0];
        } else{
            try {
                int contentLength = Integer.parseInt(contentLengthHeaderValues.get(0));
                responseBytes = new byte[contentLength];
                stream.read(responseBytes, 0, contentLength);
            } catch(NumberFormatException e){
                responseBytes = new byte[0];
            }
        }

        return responseBytes;
    }

    protected String buildQueryString(){

        String queryString;

        Map<String, String> parameters = getParameters();
        if(parameters != null && parameters.size() > 0){
            StringBuffer stringBuffer = new StringBuffer();

            Set<String> keyList = parameters.keySet();

            String[] keys = new String[keyList.size()];
            keys = parameters.keySet().toArray(keys);
            stringBuffer.append("?");
            stringBuffer.append(keys[0]);
            stringBuffer.append("=");
            stringBuffer.append(parameters.get(keys[0]));

            for(int keyIndex= 1; keyIndex < keys.length; keyIndex++){
                stringBuffer.append("&");
                stringBuffer.append(keys[keyIndex]);
                stringBuffer.append("=");
                stringBuffer.append(parameters.get(keys[keyIndex]));
            }

            queryString = stringBuffer.toString();

        } else{
            queryString = "";
        }

        return queryString;
    }
}
