package com.mim_development.android.mimrest.model.services.base.http.request;

import com.mim_development.android.mimrest.model.services.base.http.connection.HttpConnection;
import com.mim_development.android.mimrest.model.services.base.operation.HttpVerbs;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Map;

/**
 * Http request elements for http calls with a defined payload.  Class is immutable.
 */
public class HttpExtendedRequest extends HttpRequest {

    private byte[] payload;

    /**
     * Instance construction with requisite dependencies and properties
     * @param connection - connection elements.
     * @param verb - HTTP verb.
     * @param headers - HTTP headers added to the HTTP call.
     * @param connectionTimeoutInMillis - number of milliseconds to wait for the connection to complete.
     * @param parameters - HTTP query string parameters.  The parameters should be plain text, they will be URL encoded within this constructor.
     * @param payload - the payload to be included with the request.
     */
    public HttpExtendedRequest(
            final HttpConnection connection,
            HttpVerbs verb,
            final Map<String, String> headers,
            final int connectionTimeoutInMillis,
            final Map<String, String> parameters,
            final byte[] payload) {

        super(connection, verb, headers, connectionTimeoutInMillis, parameters);
        this.payload = Arrays.copyOf(payload, payload.length);
    }

    @Override
    public HttpURLConnection getHttpURLConnection() throws IOException {
        HttpURLConnection connection =  super.getHttpURLConnection();

        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(payload);

        return connection;
    }
}
