package com.dylantjohnson.webserver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Builder class for creating a {@link RouteResponse}.
 */
public class RouteResponseBuilder {
    private ResponseStatus status = ResponseStatus.OK;
    private long length = 0;
    private InputStream stream = InputStream.nullInputStream();
    private ArrayList<ResponseHeader> responseHeaders = new ArrayList<>();

    /**
     * Build the response.
     *
     * @return the response data
     */
    public RouteResponse build() {
        return new RouteResponse(this.status, this.length, this.stream,
            this.responseHeaders);
    }

    /**
     * Set the response code/status.
     *
     * @param status the status of the response
     * @return a new RouteResponseBuilder with updated status
     */
    public RouteResponseBuilder setStatus(ResponseStatus status) {
        var builder = this.copy();
        builder.status = status;
        return builder;
    }

    /**
     * Set the response content using a string.
     *
     * @param content the content of the response
     * @return a new RouteResponseBuilder with updated content
     */
    public RouteResponseBuilder setContent(String content) {
        var body = content.getBytes();
        var builder = this.copy();
        builder.length = body.length;
        builder.stream = new ByteArrayInputStream(body);
        return builder;
    }

    /**
     * Set the response content using a file.
     *
     * @param file the file to send
     * @return a new RouteResponseBuilder with updated content
     */
    public RouteResponseBuilder setContent(File file) {
        var builder = this.copy();
        builder.length = file.length();
        try {
            builder.stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            builder.stream = InputStream.nullInputStream();
        }
        return builder;
    }

    /**
     * Add a response header.
     *
     * @param header the type of header
     * @param value the value for the header
     * @return a new RouteResponseBuilder with added header
     */
    public RouteResponseBuilder addHeader(HttpHeader header, String value) {
        var builder = this.copy();
        builder.responseHeaders.add(new ResponseHeader(true, header, value));
        return builder;
    }

    /**
     * Set/overwrite a response header.
     *
     * @param header the type of header
     * @param value the value for the header
     * @return a new RouteResponseBuilder with updated header
     */
    public RouteResponseBuilder setHeader(HttpHeader header, String value) {
        var builder = this.copy();
        builder.responseHeaders.add(new ResponseHeader(false, header, value));
        return builder;
    }

    /**
     * Helper method for creating new instances.
     */
    private RouteResponseBuilder copy() {
        var builder = new RouteResponseBuilder();
        builder.status = this.status;
        builder.length = this.length;
        builder.stream = this.stream;
        builder.responseHeaders = this.responseHeaders;
        return builder;
    }
}