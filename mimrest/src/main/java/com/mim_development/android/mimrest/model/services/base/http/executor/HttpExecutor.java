package com.mim_development.android.mimrest.model.services.base.http.executor;

import android.util.Log;

import com.mim_development.android.mimrest.model.services.base.http.request.HttpRequest;
import com.mim_development.android.mimrest.model.services.base.http.response.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Engine for executing a single HTTP request that does not have an associated payload.
 */
public class HttpExecutor extends BaseHttpExecutor implements Runnable {

    private static final String TAG = HttpExecutor.class.getCanonicalName();

    private HttpRequest request;

    /**
     * Instance construction with requisite dependencies.
     * @param request - definition of the request to be completed.
     * @param monitor - instance monitoring the success / error results of the request execution.
     */
    public HttpExecutor(
            final HttpRequest request,
            final HttpExecutorMonitor monitor) {
        super(monitor);
        this.request = request;
    }

    private String getConnectionString() {
        return request.getConnectionString();
    }

    /**
     *{@inheritDoc}
     */
    protected Map<String, String> getParameters() {
        return request.getParameters();
    }

    /**
     * Provides the headers to be added to the request.
     * @return - headers to add to the request.
     */
    @Override
    protected Map<String, String> getHeaders() {
        return request.getHeaders();
    }

    private int getConnectionTimeoutInMillis() {
        return request.getConnectionTimeoutInMillis();
    }

    /**
     * Executes the HTTP request
     */
    @Override
    public void run() {

        HttpURLConnection connection = null;
        HttpExecutorMonitor monitor = getMonitor();

        try {

            URL url = new URL(getConnectionString() + buildQueryString());
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(request.getVerb());
            addHeaders(connection);
            connection.setConnectTimeout(getConnectionTimeoutInMillis());

            connection.setDoInput(true);
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
}
