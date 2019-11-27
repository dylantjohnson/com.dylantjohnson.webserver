package com.dylantjohnson.webserver;

/**
 * A basic wrapper exception used by {@link FileWatcher}.
 */
public class FileHashingException extends Exception {
    /**
     * Wrap an exception.
     *
     * @param cause the exception to wrap 
     */
    public FileHashingException(Exception cause) {
        super(cause);
    }
}
