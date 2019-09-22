package com.dylantjohnson.webserver;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;

/**
 * The core webserver.
 * <p>
 * If configured to do so, this class contains both an HTTP and HTTPS server.
 * By default, the HTTP server simply redirects all requests to the HTTPS
 * server.
 */
public class WebServer {
    private HttpsServer httpsServer = null;
    private HttpServer httpServer = null;

    /**
     * This is an internal constructor. Use {@link WebServerBuilder}.
     */
    WebServer(HttpServer httpServer, HttpsServer httpsServer) {
        this.httpServer = httpServer;
        this.httpsServer = httpsServer;
    }

    /**
     * Start the webserver.
     */
    public void start() {
        if (this.httpServer != null) {
            System.out.println(String.format("Running HTTP server on %s",
                this.httpServer.getAddress()));
            this.httpServer.start();
        }

        if (this.httpsServer != null) {
            System.out.println(String.format("Running HTTPS server on %s",
                this.httpsServer.getAddress()));
            this.httpsServer.start();
        }
    }
}
