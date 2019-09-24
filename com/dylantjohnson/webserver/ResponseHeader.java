package com.dylantjohnson.webserver;

/**
 * Internal class for storing added/updated response headers.
 * <p>
 * Headers can be added or set. If they are set, they overwrite the value they
 * had before. If they are added, the header is duplicated with the new value.
 */
class ResponseHeader {
    private boolean isAdd;
    private HttpHeader key;
    private String value;

    /**
     * Create a new response header.
     *
     * @param isAdd whether this header should be added instead of overwriting
     * @param key the key for this header
     * @param value the value for this header
     */
    public ResponseHeader(boolean isAdd, HttpHeader key, String value) {
        this.isAdd = isAdd;
        this.key = key;
        this.value = value;
    }

    /**
     * Check if this header is being added instead of overwriting.
     *
     * @return whether the header is being added (true) or overwriting (false)
     */
    public boolean getIsAdd() {
        return this.isAdd;
    }

    /**
     * Get the key for this header.
     *
     * @return the header key
     */
    public HttpHeader getKey() {
        return this.key;
    }

    /**
     * Get the value for this header.
     *
     * @return the header value
     */
    public String getValue() {
        return this.value;
    }
}
