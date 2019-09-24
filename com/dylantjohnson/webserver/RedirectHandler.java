package com.dylantjohnson.webserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

/**
 * This handler will redirect requests to a specified domain using HTTPS. It's
 * used by this server to redirect any requests for the HTTP server to the 
 * HTTPS server.
 */
class RedirectHandler extends RouteHandler {
    private String domain;
    
    /**
     * Create a new handler.
     *
     * @param domain the domain this handler should redirect requests to
     */
    public RedirectHandler(String domain) {
        this.domain = domain;
    }

    @Override
    public RouteResponse run(RouteRequest request) throws Exception {
        return new RouteResponseBuilder()
            .setStatus(ResponseStatus.MOVED)
            .setHeader(HttpHeader.LOCATION,
                String.format("https://%s%s", this.domain, request.getPath()))
            .setContent("redirecting to https")
            .build();
    }
}
