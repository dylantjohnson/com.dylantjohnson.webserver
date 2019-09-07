package com.dylantjohnson.webserver;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import javax.net.ssl.SSLContext;

class DefaultConfigurator extends HttpsConfigurator {
    public DefaultConfigurator(SSLContext context) {
        super(context);
    }

    @Override
    public void configure(HttpsParameters params) {
        var context = this.getSSLContext();
        params.setSSLParameters(context.getSupportedSSLParameters());
    }
}
