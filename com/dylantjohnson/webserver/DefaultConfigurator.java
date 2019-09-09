package com.dylantjohnson.webserver;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import javax.net.ssl.SSLContext;

/**
 * A simple {@link HttpsConfigurator} that will initialize an HTTPS connection
 * with whatever parameters the {@link SSLContext} supports.
 */
class DefaultConfigurator extends HttpsConfigurator {
    /**
     * Create a new configurator.
     *
     * @param context the SSL configuration
     */
    public DefaultConfigurator(SSLContext context) {
        super(context);
    }

    @Override
    public void configure(HttpsParameters params) {
        var context = this.getSSLContext();
        params.setSSLParameters(context.getSupportedSSLParameters());
    }
}
