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
 * The core webserver.
 * <p>
 * This actually runs 2 separate servers. One runs on port 443 using HTTPS
 * while the other runs on port 80 using regular HTTP. The HTTP server does
 * nothing but redirect requests, intended to send them to the HTTPS server.
 */
public class WebServer {
    private HttpsServer httpsServer;
    private HttpServer httpServer;

    /**
     * Create a new webserver.
     *
     * @param domain the domain to redirect HTTP requests to
     * @param keystoreFile the keystore file holding the SSL certificates
     * @param keystorePass the password for the keystore
     * @throws WebServerCreationException if the server fails to initialize
     */
    public WebServer(String domain, File keystoreFile, String keystorePass)
        throws WebServerCreationException {
        // TODO(dylan): Watch the keystore file for changes and restart the 
        // HTTPS server to automatically update certificates.
        try {
            var password = keystorePass.toCharArray();
            var keystore = KeyStore.getInstance(keystoreFile, password);
            var keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keystore, password);

            var ssl = SSLContext.getInstance("TLSv1.3");
            ssl.init(keyManagerFactory.getKeyManagers(), null, null);

            var executor = new MultiThreadExecutor();
            var address = InetAddress.getLocalHost();

            var httpsSocket = new InetSocketAddress(address, 443);
            this.httpsServer = HttpsServer.create(httpsSocket, 0);
            this.httpsServer.setExecutor(executor);
            this.httpsServer.setHttpsConfigurator(new DefaultConfigurator(ssl));
            this.httpsServer.createContext("/", new DefaultHandler());

            var httpSocket = new InetSocketAddress(address, 80);
            this.httpServer = HttpServer.create(httpSocket, 0);
            this.httpServer.setExecutor(executor);
            this.httpServer.createContext("/", new RedirectHandler(domain));
        } catch (Exception e) {
            throw new WebServerCreationException(e);
        }
    }

    /**
     * Start the webserver.
     */
    public void start() {
        System.out.println(String.format("Running HTTPS server on %s",
            this.httpsServer.getAddress()));
        System.out.println(String.format("Running HTTP server on %s",
            this.httpServer.getAddress()));
        this.httpsServer.start();
        this.httpServer.start();
    }
}
