package com.dylantjohnson.webserver;

import java.io.*;

/**
 * Builder class for generating a RouteResponse.
 */
public class RouteResponseBuilder {
    private RouteResponse.Status mStatus = RouteResponse.Status.OK;
    private long mLength = 0L;
    private InputStream mBody;

    /**
     * Build a response with the configured content.
     *
     * @return the built response
     * @throws NullPointerException if the response body has not been set
     */
    public RouteResponse build() {
        if (mBody == null) {
            throw new NullPointerException("No response body set.");
        }
        return new RouteResponse(mStatus, mLength, mBody);
    }

    /**
     * Get the status of this response.
     *
     * @return the response status code
     */
    RouteResponse.Status getStatus() {
        return mStatus;
    }

    /**
     * Get the length of this response.
     *
     * @return the size (in bytes) of the response body
     */
    long getLength() {
        return mLength;
    }

    /**
     * Get the body of this response.
     *
     * @return the body of this response as a stream
     */
    InputStream getBody() {
        return mBody;
    }

    /**
     * Set the status of this response.
     *
     * @param status the status
     * @return this builder instance to enable easy method chaining
     */
    public RouteResponseBuilder setStatus(RouteResponse.Status status) {
        mStatus = status;
        return this;
    }

    /**
     * Set the body of this response.
     *
     * @param body the body of the response
     * @return this builder instance to enable easy method chaining
     * @throws Exception if the body has already been set for this response
     */
    public RouteResponseBuilder setBody(String body) throws Exception {
        assertBodyUnset();
        byte[] bodyBuffer = body.getBytes();
        mBody = new ByteArrayInputStream(bodyBuffer);
        mLength = bodyBuffer.length;
        return this;
    }

    /**
     * Set the body of this response.
     *
     * @param body the file to send as the body of this response
     * @return this builder instance to enable easy method chaining
     * @throws Exception if the body has already been set for this response
     */
    public RouteResponseBuilder setBody(File body) throws Exception {
        assertBodyUnset();
        mBody = new FileInputStream(body);
        mLength = body.length();
        return this;
    }

    /**
     * Throw an exception if the response body has been set.
     */
    private void assertBodyUnset() throws Exception {
        if (mBody != null) {
            throw new Exception("Body already set.");
        }
    }
}
