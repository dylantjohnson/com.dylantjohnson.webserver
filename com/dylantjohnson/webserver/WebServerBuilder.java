package com.dylantjohnson.webserver;

import com.sun.net.httpserver.*;
import java.io.*;
import java.util.*;
import java.util.function.*;

/**
 * Builder class for {@link WebServer}.
 */
public class WebServerBuilder {
    private File mKeystoreFile;
    private String mKeystorePassword;
    private Map<String, RouteHandler> mRoutes;
    private int mPort = 443;
    private Supplier<InputStream> mErrorBody = new DefaultErrorBody();

    /**
     * Create a fresh WebServerBuilder.
     */
    public WebServerBuilder() {
        mRoutes = new HashMap<>();
        mRoutes.put("/", new DefaultHandler());
    }

    /**
     * Build the WebServer.
     *
     * @return a configured WebServer
     * @throws FileHashingException if the server is unable to watch the keystore file
     */
    public WebServer build() throws FileHashingException {
        return new WebServer(mKeystoreFile, mKeystorePassword, mRoutes, mPort, mErrorBody);
    }

    /**
     * Set the keystore this server will use for getting TLS certificates.
     *
     * @param keystoreFile the file containing the certificates
     * @param keystorePassword the password for the keystore (empty string if no password)
     * @return this builder instance to enable method chaining
     */
    public WebServerBuilder setKeystore(File keystoreFile, String keystorePassword) {
        mKeystoreFile = keystoreFile;
        mKeystorePassword = keystorePassword;
        return this;
    }

    /**
     * Set the port this server will run on.
     *
     * @param port the port number to bind to
     * @return this builder instance to enable method chaining
     */
    public WebServerBuilder setPort(int port) {
        mPort = port;
        return this;
    }

    /**
     * Set the handler for a given route.
     *
     * @param route the route 
     * @param handler the handler that processes the route request
     * @return this builder instance to enable method chaining
     */
    public WebServerBuilder setHandler(String route, RouteHandler handler) {
        mRoutes.put(route, handler);
        return this;
    }

    /**
     * Set the error page this server will send if there's an internal error.
     *
     * @param errorBody the error page body
     * @return this builder instance to enable method chaining
     */
    public WebServerBuilder setErrorBody(String errorBody) {
        mErrorBody = () -> new ByteArrayInputStream(errorBody.getBytes());
        return this;
    }

    /**
     * Set the error page this server will send if there's an internal error.
     *
     * @param errorFile the file to send for the error body
     * @return this builder instance to enable method chaining
     */
    public WebServerBuilder setErrorBody(File errorFile) {
        mErrorBody = () -> {
            try {
                return new FileInputStream(errorFile);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                return InputStream.nullInputStream();
            }
        };
        return this;
    }
}
