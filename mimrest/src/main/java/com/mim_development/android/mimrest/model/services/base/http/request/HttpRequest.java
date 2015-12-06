package com.mim_development.android.mimrest.model.services.base.http.request;

import com.mim_development.android.mimrest.model.services.base.http.connection.HttpConnection;
import com.mim_development.android.mimrest.model.services.base.operation.HttpVerbs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Http request elements for http calls with a defined payload.  Class is immutable.
 */
public class HttpRequest {

    private HttpConnection connection;
    private Map<String, String> headers;
    private int connectionTimeoutInMillis;
    private HttpVerbs verb;
    private Map<String, String> parameters;

    /**
     * Instance construction with requisite dependencies and properties
     * @param connection - connection elements.
     * @param verb - HTTP verb.
     * @param headers - HTTP headers added to the HTTP call.
     * @param connectionTimeoutInMillis - number of milliseconds to wait for the connection to complete.
     * @param parameters - HTTP query string parameters.  The parameters should be plain text, they will be URL encoded within this constructor.
     */
    public HttpRequest(
            final HttpConnection connection,
            final HttpVerbs verb,
            final Map<String, String> headers,
            final int connectionTimeoutInMillis,
            final Map<String, String> parameters) {

        this.connection = connection;
        this.headers = headers;
        this.connectionTimeoutInMillis = connectionTimeoutInMillis;
        this.verb = verb;

        this.parameters = new HashMap<>(parameters.size());

        for (String key : parameters.keySet()) {
            String encodedKey;

            try {
                encodedKey = URLEncoder.encode(key, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                encodedKey = key;
            }

            String encodedValue;
            String value = parameters.get(key);
            try {
                encodedValue = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                encodedValue = value;
            }

            this.parameters.put(encodedKey, encodedValue);
        }
    }

    /**
     * Provides the payload to be included with the execution of the HTTP request.
     * @return - binary representation of the payload to be included with the HTTP request
     */
    public String getConnectionString() {
        return connection.toString();
    }

    /**
     * Provides the HTTP headers added to the HTTP request.
     * @return - binary representation of the payload to be included with the HTTP request
     */
    public Map<String, String> getHeaders() {
        Map<String, String> result = new HashMap<>(headers.size());
        result.putAll(headers);
        return result;
    }

    /**
     * Provides the number of milliseconds to wait for the connection to complete.
     * @return - the number of milliseconds to wait for the connection to complete.
     */
    public int getConnectionTimeoutInMillis() {
        return connectionTimeoutInMillis;
    }

    /**
     * Provides the HTTP verb used to execute the HTTP request.
     * @return - the HTTP verb used to execute the request.
     */
    public String getVerb() {
        return verb.name();
    }

    /**
     * Provides the query string parameters to be included with the execution of the HTTP request.
     * @return - query string parameters to be included with the HTTP request.
     */
    public Map<String, String> getParameters() {
        Map<String, String> parameterCopy = new HashMap<>(parameters.size());
        parameterCopy.putAll(parameters);
        return parameterCopy;
    }
}
