package com.dylantjohnson.webserver;

import java.io.*;

/**
 * This class represents a response to a web request.
 */
public class RouteResponse {
    /**
     * All of the possible status codes.
     */
    enum Status {
        ERROR(500), OK(200);

        private int mCode;

        private Status(int code) {
            mCode = code;
        }

        /**
         * Get the error code number.
         *
         * @return the error code
         */
        public int getCode() {
            return mCode;
        }
    }

    private Status mStatus;
    private long mLength;
    private InputStream mBody;

    /**
     * Create a response.
     * <p>
     * This constructor is for internal use. Use {@link RouteResponseBuilder}.
     *
     * @param status the status for the response
     * @param length the size of the response body
     * @param body the response body
     */
    RouteResponse(Status status, long length, InputStream body) {
        mStatus = status;
        mLength = length;
        mBody = body;
    }

    /**
     * Get the status.
     *
     * @return the status code
     */
    Status getStatus() {
        return mStatus;
    }

    /**
     * Get the length.
     *
     * @return the length (in bytes) of the response body
     */
    long getLength() {
        return mLength;
    }

    /**
     * Get the body.
     *
     * @return a stream of the response body
     */
    InputStream getBody() {
        return mBody;
    }
}
