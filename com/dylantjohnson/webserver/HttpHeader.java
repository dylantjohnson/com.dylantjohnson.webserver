package com.dylantjohnson.webserver;

/**
 * Known HTTP headers.
 */
public enum HttpHeader {
    LOCATION("Location");

    private String name;

    private HttpHeader(String name) {
        this.name = name;
    }

    /**
     * Get the raw name of this header.
     *
     * @return the header name
     */
    public String getName() {
        return this.name;
    }
}
