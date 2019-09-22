package com.dylantjohnson.webserver;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;
import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

/**
 * Builder class for {@link WebServer}.
 */
public class WebServerBuilder {
    private String domain = "localhost";
    private boolean isHttp = false;
    private SSLContext ssl = null;
    private int httpPort = 80;
    private int httpsPort = 443;

    /**
     * Build the configured server.
     *
     * @return a configured {@link WebServer}
     * @throws WebServerCreationException when unable to create HTTP/S servers
     */
    public WebServer build() throws WebServerCreationException {
        try {
            var executor = new MultiThreadExecutor();
            var address = InetAddress.getLocalHost();
            HttpServer httpServer = null;
            HttpsServer httpsServer = null;

            if (this.isHttp) {
                var socket = new InetSocketAddress(address, this.httpPort);
                var server = HttpServer.create(socket, 0);
                server.setExecutor(executor);
                server.createContext("/", new RedirectHandler(this.domain));
                httpServer = server;
            }

            if (this.ssl != null) {
                var socket = new InetSocketAddress(address, this.httpsPort);
                var server = HttpsServer.create(socket, 0);
                server.setExecutor(executor);
                server.setHttpsConfigurator(new DefaultConfigurator(this.ssl));
                server.createContext("/", new DefaultHandler());
                httpsServer = server;
            }

            return new WebServer(httpServer, httpsServer);
        } catch (Exception e) {
            throw new WebServerCreationException(e);
        }
    }

    /** 
     * Set the domain for this server.
     * <p>
     * If this is not called, the default domain is "localhost." Use this method
     * if {@link useHttp} is called. The default behaviour of the HTTP server is
     * to redirect to HTTPS, so it needs to know the domain of the HTTPS server.
     *
     * @param domain the domain of the server
     * @return a new builder with the domain configured
     */
    public WebServerBuilder setDomain(String domain) {
        var builder = this.copy();
        builder.domain = domain;
        return builder;
    }

    /**
     * When the server runs, start an HTTP server.
     * <p>
     * Be sure to call {@link setDomain} when using this method, unless you
     * plan on changing the default behaviour of the HTTP server.
     *
     * @return a new builder that will use HTTP
     */
    public WebServerBuilder useHttp() {
        var builder = this.copy();
        builder.isHttp = true;
        return builder;
    }

    /**
     * When the server runs, start an HTTPS server.
     *
     * @param keystoreFile the keystore containing certificates for your domain
     * @param keystorePass the password for accessing the keystore
     * @return a new builder that will use HTTPS
     * @throws WebServerCreationException when unable to read keystore
     */
    public WebServerBuilder useHttps(File keystoreFile, String keystorePass)
            throws WebServerCreationException {
        try {
            var password = keystorePass.toCharArray();
            var keystore = KeyStore.getInstance(keystoreFile, password);
            var keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keystore, password);
            var ssl = SSLContext.getInstance("TLSv1.3");
            ssl.init(keyManagerFactory.getKeyManagers(), null, null);
            var builder = this.copy();
            builder.ssl = ssl;
            return builder;
        } catch (Exception e) {
            throw new WebServerCreationException(e);
        }
    }

    /**
     * Set the port the HTTP server will run on. 80 by default.
     *
     * @param port the HTTP port
     * @return a new builder with the HTTP port configured
     */
    public WebServerBuilder setHttpPort(int port) {
        var builder = this.copy();
        builder.httpPort = port;
        return builder;
    }

    /**
     * Set the port the HTTPS server will run on. 443 by default.
     *
     * @param port the HTTPS port
     * @return a new builder with the HTTPS port configured
     */
    public WebServerBuilder setHttpsPort(int port) {
        var builder = this.copy();
        builder.httpsPort = port;
        return builder;
    }

    /**
     * Create a copy of this builder.
     * <p>
     * Since each method returns a builder instance to enable method chaining,
     * this helper method makes it easy to get a new instance with the same
     * configuration.
     *
     * @return a new builder with the same configuration
     */
    private WebServerBuilder copy() {
        var builder = new WebServerBuilder();
        builder.domain = this.domain;
        builder.isHttp = this.isHttp;
        builder.ssl = this.ssl;
        builder.httpPort = this.httpPort;
        builder.httpsPort = this.httpsPort;
        return builder;
    }
}
