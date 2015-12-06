package com.mim_development.android.mimrest.model.services.base.http.request;

import com.mim_development.android.mimrest.model.services.base.http.connection.HttpConnection;
import com.mim_development.android.mimrest.model.services.base.operation.HttpVerbs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    protected String getConnectionString() {
        return connection.toString();
    }

    /**
     * Provides the HTTP headers added to the HTTP request.
     * @return - binary representation of the payload to be included with the HTTP request
     */
    protected Map<String, String> getHeaders() {
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
    protected String getVerb() {
        return verb.name();
    }

    /**
     * Provides the query string parameters to be included with the execution of the HTTP request.
     * @return - query string parameters to be included with the HTTP request.
     */
    protected Map<String, String> getParameters() {
        Map<String, String> parameterCopy = new HashMap<>(parameters.size());
        parameterCopy.putAll(parameters);
        return parameterCopy;
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

    public HttpURLConnection getHttpURLConnection() throws IOException {

        URL url = new URL(getConnectionString() + buildQueryString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(getVerb());
        addHeaders(connection);
        connection.setConnectTimeout(getConnectionTimeoutInMillis());

        connection.setDoInput(true);

        return connection;
    }
}
