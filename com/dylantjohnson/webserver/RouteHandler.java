package com.dylantjohnson.webserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.InputStream;
import java.io.IOException;

/**
 * Custom route handlers must extend this class.
 */
public abstract class RouteHandler implements HttpHandler {
    /**
     * Process a web request and send a response.
     *
     * @param request the incoming request to process
     * @return the response to send back
     * @throws Exception if anything goes wrong
     */
    public abstract RouteResponse run(RouteRequest request) throws Exception;

    /**
     * The response to send when a request fails (throws an exception).
     * <p>
     * Override this method to provide a custom error response. By default, this
     * sends a very simple HTML page with status 500.
     */
    public RouteResponse onError() {
        var body = String.join("\n",
            "<!DOCTYPE html>",
            "<html lang=\"en\">",
            "  <head>",
            "    <meta charset=\"utf-8\">",
            "    <title>Internal Error</title>",
            "  </head>",
            "  <body>",
            "    <p>There was a problem processing your request.</p>",
            "  </body>",
            "</html>");
        return new RouteResponseBuilder()
            .setStatus(ResponseStatus.ERROR)
            .setContent(body)
            .build();
    }

    @Override
    public final void handle(HttpExchange request) throws IOException {
        RouteResponse response = null;
        try {
            response = this.run(new RouteRequest(request));
        } catch (Exception e) {
            response = this.onError();
        }
        var code = response.getStatus().getCode();
        var length = response.getLength();
        var headers = request.getResponseHeaders();
        for (var header : response.getHeaders()) {
            if (header.getIsAdd()) {
                headers.add(header.getKey().getName(), header.getValue());
            } else {
                headers.set(header.getKey().getName(), header.getValue());
            }
        }
        request.sendResponseHeaders(code, length);
        try (var outputStream = request.getResponseBody();
                var responseStream = response.getStream();
                var inputStream = request.getRequestBody()) {
            this.exhaustStream(inputStream);
            responseStream.transferTo(outputStream);
        }
    }

    /**
     * Helper method to read remainder of an InputStream.
     * <p>
     * Custom handlers may ignore or otherwise neglect to read the whole
     * body of a request. The HttpServer docs recommend reading it all, so this
     * is used to make sure that happens.
     */
    private void exhaustStream(InputStream stream) throws IOException {
        var read = stream.read();
        while (read > -1) {
            read = stream.read();
        }
    }
}
