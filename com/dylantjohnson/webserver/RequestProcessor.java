package com.dylantjohnson.webserver;

import com.sun.net.httpserver.*;
import java.io.*;
import java.util.function.*;

/**
 * This class is used internally to wrap RouteHandlers into regular HttpHandlers.
 * <p>
 * This wrapper ensures a few things are accomplished automatically. If the RouteHandler fails
 * unexpectedly and throws an exception, it will be caught and the response will set to a
 * configured error response page with a status code of 500. If the RouteHandler neglects to
 * read the request body, this will ensure that it gets exhausted before closing the request.
 */
class RequestProcessor implements HttpHandler {
    private RouteHandler mHandler;
    private Supplier<InputStream> mErrorBody;

    /**
     * Create a RequestProcessor.
     *
     * @param handler the RouteHandler to wrap
     * @param errorBody a function that generates a response to send if the handler fails
     */
    public RequestProcessor(RouteHandler handler, Supplier<InputStream> errorBody) {
        mHandler = handler;
        mErrorBody = errorBody;
    }

    @Override
    public void handle(HttpExchange request) throws IOException {
        int status;
        long length;
        InputStream body;
        try {
            var response = mHandler.handle(new RouteRequest(request));
            status = response.getStatus().getCode();
            length = response.getLength();
            body = response.getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
            status = RouteResponse.Status.ERROR.getCode();
            length = 0;
            body = mErrorBody.get();
        }
        exhaustStream(request.getRequestBody());
        request.sendResponseHeaders(status, length);
        var output = request.getResponseBody();
        body.transferTo(output);
        body.close();
        output.close();
    }

    /**
     * Read any remaining input from a stream and close it.
     *
     * @param stream the stream to exhaust
     * @throws IOException if there is a problem reading the stream
     */
    private static void exhaustStream(InputStream stream) throws IOException {
        while (stream.available() > 0) {
            stream.read();
        }
        stream.close();
    }
}
