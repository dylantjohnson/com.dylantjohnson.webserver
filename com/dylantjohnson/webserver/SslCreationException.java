package com.dylantjohnson.webserver;

import javax.net.ssl.*;

/**
 * A wrapper exception that's thrown when there's a problem creating an {@link SSLContext}.
 */
public class SslCreationException extends Exception {
    /**
     * Wrap an exception.
     *
     * @param cause the exception to wrap
     */
    public SslCreationException(Exception cause) {
        super(cause);
    }
}
