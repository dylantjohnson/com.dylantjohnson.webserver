package com.dylantjohnson.webserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

/**
 * The default handler that's used by this server if it's not overridden.
 */
class DefaultHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var response = "com.dylantjohnson.webserver is running".getBytes();
        exchange.sendResponseHeaders(200, response.length);
        var output = exchange.getResponseBody();
        output.write(response);
        exchange.close();
    }
}
