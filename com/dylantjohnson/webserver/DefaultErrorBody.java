package com.dylantjohnson.webserver;

import java.io.*;
import java.util.function.*;

/**
 * The error page generator used by {@link WebServer} if another is not configured.
 */
class DefaultErrorBody implements Supplier<InputStream> {
    @Override
    public InputStream get() {
        var body = String.join("\n",
            "<!DOCTYPE html>",
            "<html lang=\"en\">",
            "  <head>",
            "    <meta charset=\"UTF-8\">",
            "    <title>Internal Error</title>",
            "  </head>",
            "  <body>",
            "    <p>com.dylantjohnson.webserver experienced an internal error.</p>",
            "  </body>",
            "</html>");
        return new ByteArrayInputStream(body.getBytes());
    }
}
