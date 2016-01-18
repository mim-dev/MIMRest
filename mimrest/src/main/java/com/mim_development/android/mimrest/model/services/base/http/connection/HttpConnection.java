package com.mim_development.android.mimrest.model.services.base.http.connection;

import com.mim_development.android.mimrest.utility.StringUtil;

/**
 * HTTP connection elements
 */
public class HttpConnection {

    private String server;
    private String path;
    private String action;
    private boolean secure;

    protected String getServer() {
        return server;
    }

    protected String getPath() {
        return path;
    }

    protected String getAction() {
        return action;
    }

    protected boolean isSecure() {
        return secure;
    }

    /**
     * Instantiates a new instance.
     * @param secure - flag indicating if the connection should be treated as a secure connection.
     * @param server - server element which should not have leading or trailing path separators, i.e. '/' (required)
     * @param path - path element which should not have leading or trailing path separators, i.e. '/' (optional)
     * @param action - action element which should not have leading or trailing path separators, i.e. '/' (optional)
     */
    public HttpConnection(
            boolean secure,
            String server,
            String path,
            String action) {

        if(StringUtil.isEmpty(server)){
            throw new IllegalArgumentException("The server argument is required.");
        }

        this.server = scrubPathElement(server);
        this.path = scrubPathElement(path);
        this.action = scrubPathElement(action);
        this.secure = secure;
    }

    /**
     * Creates a new instance (deep copy) from an existing instance.
     * @param copy - existing instance used to initialize the new instance.
     */
    public HttpConnection(HttpConnection copy) {
        secure = copy.isSecure();
        server = copy.getServer();
        path = copy.getPath();
        action = copy.getAction();
    }

    /**
     * Presents a fully populated connection instance.
     * @return - the fully populated connection string.
     */
    @Override
    public String toString() {
        String stringValue;

                if(StringUtil.isNotEmpty(getPath())){
                    stringValue = (isSecure() ? "https://" : "http://") + getServer() + "/" + getPath() + "/" + getAction();
                } else{
                    stringValue = (isSecure() ? "https://" : "http://") + getServer() + "/" + getAction();
                }

        return stringValue;
    }

    protected String scrubPathElement(final String pathElement){

        String scrubbedPathElement = pathElement;

        if(pathElement == null){
            scrubbedPathElement =  "";
        } else{
            if(scrubbedPathElement.startsWith("/")){
                scrubbedPathElement = scrubbedPathElement.substring(1);
            }

            if(scrubbedPathElement.endsWith("/")){
                scrubbedPathElement = scrubbedPathElement.substring(0, scrubbedPathElement.length() - 2);
            }
        }
        return scrubbedPathElement;
    }
}
