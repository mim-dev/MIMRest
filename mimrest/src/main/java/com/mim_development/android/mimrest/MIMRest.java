package com.mim_development.android.mimrest;

public class MIMRest {

    private static MIMRest instance = new MIMRest();
    public static MIMRest getInstance(){
        return instance;
    }

    private boolean secure;
    private String server;
    private String serverApplicationPath;
    private int connectionTimeOutMillis = 8000;

    public boolean isSecure() {
        return secure;
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

    public void initialize(
            boolean secure,
            String server,
            String serverApplicationPath,
            int connectionTimeOutMillis){

        this.secure = secure;
        this.server = server;
        this.serverApplicationPath = serverApplicationPath;
        this.connectionTimeOutMillis = connectionTimeOutMillis;
    }
}
