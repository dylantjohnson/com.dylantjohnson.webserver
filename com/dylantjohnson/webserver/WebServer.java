package com.dylantjohnson.webserver;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class WebServer {
    private HttpsServer httpsServer;
    private HttpServer httpServer;

    public WebServer(String domain, File keystoreFile, String keystorePass)
        throws GeneralSecurityException, IOException, UnknownHostException {
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
    }

    public void start() {
        System.out.println(String.format("Running HTTPS server on %s",
            this.httpsServer.getAddress()));
        System.out.println(String.format("Running HTTP server on %s",
            this.httpServer.getAddress()));
        this.httpsServer.start();
        this.httpServer.start();
    }
}
