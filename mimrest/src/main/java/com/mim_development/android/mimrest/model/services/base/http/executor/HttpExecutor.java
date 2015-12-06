package com.mim_development.android.mimrest.model.services.base.http.executor;

import android.util.Log;

import com.mim_development.android.mimrest.model.services.base.http.request.HttpRequest;
import com.mim_development.android.mimrest.model.services.base.http.response.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * Engine for executing a single HTTP request on separate thread.
 */
public class HttpExecutor implements Runnable {

    private static final String TAG = HttpExecutor.class.getCanonicalName();
    private static final String CONTENT_LENGTH_HEADER_NAME = "Content-Length";

    private HttpExecutorMonitor monitor;
    private HttpRequest request;

    /**
     * Instance construction with requisite dependencies.
     * @param request - definition of the request to be completed.
     * @param monitor - instance monitoring the success / error results of the request execution.
     */
    public HttpExecutor(
            final HttpRequest request,
            final HttpExecutorMonitor monitor) {
        this.monitor = monitor;
        this.request = request;
    }

    /**
     * Executes the HTTP request
     */
    @Override
    public void run() {

        HttpURLConnection connection = null;

        try {

            connection = request.getHttpURLConnection();
            int responseCode = connection.getResponseCode();
            byte[] responsePayload;

            Map<String, List<String>> responseHeaders = connection.getHeaderFields();

            // process the response
            if (responseCode > 199 && responseCode < 300) {
                InputStream inputStream = connection.getInputStream();
                responsePayload = processResponseContent(responseHeaders, inputStream);
                inputStream.close();
            } else {
                InputStream errorStream = connection.getErrorStream();
                responsePayload = processResponseContent(responseHeaders, errorStream);
                errorStream.close();

                Log.e(TAG, "run(): received an HTTP error.  Status is:[" + responseCode
                        + "].  Payload is:[" + (new String(responsePayload)) + "]");
            }

            connection.disconnect();

            if (monitor != null) {
                HttpResponse response = new HttpResponse(
                        responseHeaders, responsePayload, responseCode);
                monitor.result(response);
            }
        } catch (IOException e) {
            if (monitor != null) {
                monitor.error(e);
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
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
}
