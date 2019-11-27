package com.dylantjohnson.webserver;

import com.sun.net.httpserver.*;
import java.net.*;

/**
 * This class represents a web request.
 */
public class RouteRequest {
    /**
     * All of the possible request method types.
     */
    enum Method {
        GET, POST;
    }

    private HttpExchange mExchange;

    /**
     * Construct a RouteRequest from an HttpExchange.
     *
     * @param exchange the exchange to wrap
     */
    RouteRequest(HttpExchange exchange) {
        mExchange = exchange;
    }

    /**
     * Get the method type of this request.
     *
     * @return the method type
     */
    public Method getMethod() {
        if (mExchange.getRequestMethod().equalsIgnoreCase("get")) {
            return Method.GET;
        }
        return Method.POST;
    }

    /**
     * Get the URI of this request.
     *
     * @return the request URI
     */
    public URI getUri() {
        return mExchange.getRequestURI();
    }
}
