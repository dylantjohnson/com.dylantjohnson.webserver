package com.dylantjohnson.webserver;

/**
 * The default handler that's used by this server if it's not overridden.
 */
class DefaultHandler extends RouteHandler {
    @Override
    public RouteResponse run(RouteRequest request) throws Exception {
        return new RouteResponseBuilder()
            .setStatus(ResponseStatus.OK)
            .setContent("com.dylantjohnson.webserver is running")
            .build();
    }
}
