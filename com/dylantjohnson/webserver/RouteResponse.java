package com.dylantjohnson.webserver;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The data to send back in response to an HTTP request.
 * <p>
 * To build a response, see {@link RouteResponseBuilder}.
 */
public class RouteResponse {
    private ResponseStatus status;
    private long length;
    private InputStream stream;
    private List<ResponseHeader> responseHeaders;

    /**
     * Internal constructor. Use {@link RouteResponseBuilder} to build a
     * response.
     */
    RouteResponse(ResponseStatus status, long length, InputStream stream,
            List<ResponseHeader> responseHeaders) {
        this.status = status;
        this.length = length;
        this.stream = stream;
        this.responseHeaders = responseHeaders;
    }

    /**
     * Get the HTTP status of the response.
     *
     * @return the response status
     */
    public ResponseStatus getStatus() {
        return this.status;
    }

    /**
     * Get the length of the response.
     *
     * @return the response size in bytes
     */
    public long getLength() {
        return this.length;
    }

    /**
     * Get the data stream of the response.
     *
     * @return the response data stream
     */
    public InputStream getStream() {
        return this.stream;
    }

    /**
     * Get the HTTP headers for this response.
     *
     * @return a list of headers
     */
    public List<ResponseHeader> getHeaders() {
        return new ArrayList<ResponseHeader>(this.responseHeaders);
    }
}
