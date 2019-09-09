package com.dylantjohnson.webserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

/**
 * This handler will redirect requests to a specified domain using HTTPS. It's
 * used by this server to redirect any requests for the HTTP server to the 
 * HTTPS server.
 */
class RedirectHandler implements HttpHandler {
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
    public void handle(HttpExchange exchange) throws IOException {
        var path = exchange.getRequestURI();
        var response = "Redirecting to https".getBytes();
        var headers = exchange.getResponseHeaders();
        headers.set("Location",
            String.format("https://%s%s", this.domain, path));
        exchange.sendResponseHeaders(301, response.length);
        var output = exchange.getResponseBody();
        output.write(response);
        exchange.close();
    }
}
