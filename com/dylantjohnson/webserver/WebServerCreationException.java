package com.dylantjohnson.webserver;

/**
 * This exception is thrown when {@link WebServer} fails to initialize.
 */
public class WebServerCreationException extends Exception {
    /**
     * Wrap an exception.
     *
     * @param cause the exception that interrupted initialization
     */
    public WebServerCreationException(Exception cause) {
        super(cause);
    }
}
