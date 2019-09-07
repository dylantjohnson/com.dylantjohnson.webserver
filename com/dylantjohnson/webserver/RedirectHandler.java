package com.dylantjohnson.webserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

class RedirectHandler implements HttpHandler {
    private String domain;
    
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
