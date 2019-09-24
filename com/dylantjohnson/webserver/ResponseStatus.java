package com.dylantjohnson.webserver;

/**
 * Known response statuses.
 */
public enum ResponseStatus {
    OK(200),
    MOVED(301),
    ERROR(500);

    private int code;

    private ResponseStatus(int code)  {
        this.code = code;
    }

    /**
     * Get the code for this response status.
     *
     * @return the status code
     */
    public int getCode() {
        return this.code;
    }
}
