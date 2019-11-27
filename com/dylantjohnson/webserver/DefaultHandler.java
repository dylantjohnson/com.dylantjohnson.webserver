package com.dylantjohnson.webserver;

/**
 * The handler used by {@link WebServer} for any route if no others are configured.
 */
class DefaultHandler implements RouteHandler {
    @Override
    public RouteResponse handle(RouteRequest request) throws Exception {
        var body = String.join("\n",
            "<!DOCTYPE html>",
            "<html lang=\"en\">",
            "  <head>",
            "    <meta charset=\"UTF-8\">",
            "    <title>com.dylantjohnson.webserver</title>",
            "  </head>",
            "  <body>",
            "    <p>com.dylantjohnson.webserver is running</p>",
            "  </body>",
            "</html>");
        return new RouteResponseBuilder()
            .setStatus(RouteResponse.Status.OK)
            .setBody(body)
            .build();
    }
}
