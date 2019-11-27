package com.dylantjohnson.webserver;

import com.sun.net.httpserver.*;
import java.net.*;

/**
 * The interface to implement for creating custom handlers for a route.
 */
public interface RouteHandler {
    /**
     * Generate a RouteResponse by processing a RouteRequest.
     * 
     * @param request the request to process
     * @return a response containing a status code and content
     * @throws Exception if an error occurs while generating the response
     */
    RouteResponse handle(RouteRequest request) throws Exception;
}
