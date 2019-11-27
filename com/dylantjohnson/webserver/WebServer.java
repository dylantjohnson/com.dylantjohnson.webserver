package com.dylantjohnson.webserver;

import com.sun.net.httpserver.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.security.*;
import java.util.concurrent.*;
import java.util.function.*;
import javax.net.ssl.*;

/**
 * The core web server class.
 * <p>
 * This is a simple wrapper class for an {@link HttpsServer}. It hides a lot of the boilerplate
 * involved with configuring a server to use TLS. This server will also watch its configured
 * keystore file for changes and automatically restart itself so certificate updates can happen
 * automatically.
 * <p>
 * This class is thread-safe.
 */
public class WebServer {
    private final int STOP_DELAY_SEC = 5;
    
    private File mKeystoreFile;
    private String mKeystorePassword;
    private int mPort;
    private Map<String, RouteHandler> mRoutes;
    private HttpsServer mServer;
    private FileWatcher mKeystoreWatcher;
    private Supplier<InputStream> mErrorBody;

    /**
     * Construct a WebServer.
     * <p>
     * Users of this class should not call this constructor. Instead, they should use the
     * {@link WebServerBuilder} class to build a WebServer.
     *
     * @param keystoreFile the keystore file containing the certificate for this server
     * @param keystorePassword the password for the keystore file (empty string if none)
     * @param routes a map of server routes and their {@link RouteHandler}s.
     * @param port the port this server will bind to
     * @param errorBody a function that can be called to generate the body for an error page
     * @throws FileHashingException if unable to watch the configured keystore file
     */
    WebServer(File keystoreFile, String keystorePassword, Map<String, RouteHandler> routes,
            int port, Supplier<InputStream> errorBody) throws FileHashingException {
        mKeystoreFile = keystoreFile;
        mKeystorePassword = keystorePassword;
        mRoutes = routes;
        mPort = port;
        mErrorBody = errorBody;
        mKeystoreWatcher = new FileWatcher(mKeystoreFile);
        
        mKeystoreWatcher.addListener(() -> {
            try {
                restart();
            } catch (Exception ex) {
                ex.printStackTrace();
                stop();
            }
        });
    }

    /**
     * Start the server.
     *
     * @throws IOException if the server is unable to bind to the local host and port
     * @throws UnknownHostException if unable to resolve the localhost address
     * @throws SslCreationException if there is a problem with the configured keystore file
     */
    public synchronized void start() throws IOException, UnknownHostException,
            SslCreationException {
        startServer();
    }

    /**
     * Stop the server.
     */
    public synchronized void stop() {
        stopServer();
    }

    /**
     * Restart the server.
     *
     * @throws IOException if the server is unable to bind to the local host and port
     * @throws UnknownHostException if unable to resolve the localhost address
     * @throws SslCreationException if there is a problem with the configured keystore file
     */
    public synchronized void restart() throws IOException, UnknownHostException,
            SslCreationException {
        stopServer();
        startServer();
    }

    /**
     * Start a new HttpsServer and begin watching the keystore file for changes.
     * <p>
     * If a server is already started, this does nothing.
     *
     * @throws IOException if the server is unable to bind to the local host and port
     * @throws UnknownHostException if unable to resolve the localhost address
     * @throws SslCreationException if there is a problem with the configured keystore file
     */
    private void startServer() throws IOException, UnknownHostException, SslCreationException {
        if (mServer == null) {
            mServer = buildServer();
            System.out.println(String.format("Starting server, %s...", mServer.getAddress()));
            mServer.start();
            mKeystoreWatcher.start();
        }
    }

    /**
     * Stop the currently running server and stop watching the keystore file for changes.
     * <p>
     * If the server is already stopped, this does nothing.
     */
    private void stopServer() {
        if (mServer != null) {
            mServer.stop(STOP_DELAY_SEC);
            mKeystoreWatcher.stop();
            mServer = null;
        }
    }

    /**
     * Generate a new HttpsServer.
     * <p>
     * This method also creates a new SSLContext. So if the keystore file has been updated with a
     * new certificate, it will be reflected in the new server.
     *
     * @return a new HttpsServer
     * @throws IOException if the server is unable to bind to the local host and port
     * @throws UnknownHostException if unable to resolve the localhost address
     * @throws SslCreationException if there is a problem with the configured keystore file
     */
    private HttpsServer buildServer() throws IOException, UnknownHostException,
            SslCreationException {
        var cores = Runtime.getRuntime().availableProcessors();
        var executor = new ThreadPoolExecutor(cores, cores, 0, TimeUnit.NANOSECONDS,
            new LinkedBlockingQueue<>());
        var configurator = new DefaultConfigurator(mKeystoreFile, mKeystorePassword);
        var address = InetAddress.getLocalHost();
        var socket = new InetSocketAddress(address, mPort);
        var server = HttpsServer.create(socket, 0);
        server.setExecutor(executor);
        server.setHttpsConfigurator(configurator);
        for (var route : mRoutes.keySet()) {
            server.createContext(route, new RequestProcessor(mRoutes.get(route), mErrorBody));
        }
        return server;
    }
}
