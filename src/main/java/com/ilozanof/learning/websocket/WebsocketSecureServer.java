package com.ilozanof.learning.websocket;

import com.ilozanof.learning.websocket.broadcast.server.BroadcastEndpoint;
import com.ilozanof.learning.websocket.broadcastAdvanced.server.BroadcastAdvancedEndpoint;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.server.ServerContainer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of an SSL Server. It relies on the WebsocketHttpServer, so
 * this class also includes HTTP configuration.
 */
public class WebsocketSecureServer extends WebsocketHttpServer {

    private static final String KEYSTORE_PASSWD    = "websockets";
    private static final String KEYMANAGER_PASSWD  = "websockets";


    // Unique instance (Singleton) for all servers...
    private static WebsocketSecureServer instance = null;

    // port number...
    private int sslPort;

    private WebsocketSecureServer(int httpPort, int sslPort) {
        super(httpPort);
        this.sslPort = sslPort;
    }

    /**
     * Returns the same instance for all the calls (Singleton)
     * @return
     */
    public static synchronized WebsocketSecureServer getInstance(int httpPort, int sslPort, Class ...endpointClasses) {
        if (instance == null) {
            instance = new WebsocketSecureServer(httpPort, sslPort);
        }
        return instance;
    }

    protected List<ServerConnector> getConnectors() {
        List<ServerConnector> result = super.getConnectors();

        // We setup the SSL Connector...
        URL keystoreURL =  WebsocketSecureServer.class.getResource("/keystore");
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(keystoreURL.toExternalForm());
        sslContextFactory.setKeyStorePassword(KEYSTORE_PASSWD);
        sslContextFactory.setKeyManagerPassword(KEYMANAGER_PASSWD);

        HttpConfiguration httpsConfig = new HttpConfiguration();
        httpsConfig.addCustomizer(new SecureRequestCustomizer());

        ServerConnector sslConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(httpsConfig));
        sslConnector.setPort(sslPort);
        result.add(sslConnector);

        return result;
    }

    
    /**
     * Main method. If you want to start the Server out of the scope of a JunitTest, use
     * this method.
     * @param args
     */
    public static void main(String ...args) {
        try {
            WebsocketSecureServer sslServer = WebsocketSecureServer.getInstance(8080, 8443);
            sslServer.start(BroadcastEndpoint.class, BroadcastAdvancedEndpoint.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
