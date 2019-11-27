package com.dylantjohnson.webserver;

import com.sun.net.httpserver.*;
import java.io.*;
import java.security.*;
import javax.net.ssl.*;

/**
 * The HTTPS configurator used by {@link WebServer}.
 */
class DefaultConfigurator extends HttpsConfigurator {
    /**
     * Create a new configurator.
     *
     * @param keystoreFile the keystore file containing the certificate for the server
     * @param keystorePassword the password for the keystore file (empty string if none)
     * @throws SSLCreationException if there is a problem with the given keystore file
     */
    public DefaultConfigurator(File keystoreFile, String keystorePassword)
            throws SslCreationException {
        super(buildSslContext(keystoreFile, keystorePassword));
    }

    /**
     * Build an SSLContext from a keystore file.
     *
     * @param keystoreFile the keystore file
     * @param keystorePassword the password for the keystore file
     * @return a new SSLContext
     * @throws SslCreationException if unable to build the SSLContext
     */
    private static SSLContext buildSslContext(File keystoreFile, String keystorePassword)
            throws SslCreationException {
        try {
            var password = keystorePassword.toCharArray();
            var keystore = KeyStore.getInstance(keystoreFile, password);
            var keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keystore, password);
            var ssl = SSLContext.getInstance("TLSv1.3");
            ssl.init(keyManagerFactory.getKeyManagers(), null, null);
            return ssl;
        } catch (Exception ex) {
            throw new SslCreationException(ex);
        }
    }
}
