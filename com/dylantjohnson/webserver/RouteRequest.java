package com.dylantjohnson.webserver;

import com.sun.net.httpserver.HttpExchange;
import java.net.URI;

/**
 * Information about an HTTP request.
 */
public class RouteRequest {
    private URI path;

    /**
     * Internal constructor. This class shouldn't be instantiated manually.
     */
    RouteRequest(HttpExchange request) {
        this.path = request.getRequestURI();
    }

    /**
     * Get the path of this request.
     *
     * @return the request path
     */
    public URI getPath() {
        return this.path;
    }
}
