package com.mim_development.android.mimrest;

import android.content.Context;

/**
 * Singleton implementation for library management
 */
public class MIMRest {

    private static MIMRest instance = new MIMRest();

    public static MIMRest getInstance() {
        return instance;
    }

    private boolean serverSecure;
    private String server;
    private String serverApplicationPath;
    private int connectionTimeOutMillis = 8000;

    private Context applicationContext;

    public boolean isServerSecure() {
        return serverSecure;
    }

    public String getServer() {
        return server;
    }

    public String getServerApplicationPath() {
        return serverApplicationPath;
    }

    public int getConnectionTimeOutMillis() {
        return connectionTimeOutMillis;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    /**
     * One time requires library initialization
     *
     * @param secure                  - flag indicating if a secure communication protocol should be used for service calls
     * @param server                  - name / IP address and optional port element of the service URI
     * @param serverApplicationPath   - application path element of the service URI
     * @param connectionTimeOutMillis - time in milliseconds to wait for a service connection to respond
     * @param applicationContext      - application level context of the host application
     */
    public void initialize(
            boolean secure,
            String server,
            String serverApplicationPath,
            int connectionTimeOutMillis,
            Context applicationContext) {

        this.serverSecure = secure;
        this.server = server;
        this.serverApplicationPath = serverApplicationPath;
        this.connectionTimeOutMillis = connectionTimeOutMillis;

        // ensure the application context is saved
        this.applicationContext = applicationContext.getApplicationContext();
    }
}
