package com.ilozanof.learning.websocket;

import com.ilozanof.learning.websocket.broadcast.server.BroadcastEndpoint;
import com.ilozanof.learning.websocket.broadcastAdvanced.server.BroadcastAdvancedEndpoint;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.server.ServerContainer;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete impldementation of WebsocketServer, adding a HTTP connector.
 */
public class WebsocketHttpServer extends WebsocketServer {


    // Unique instance (Singleton) for all servers...
    private static WebsocketHttpServer instance = null;

    // port number...
    private int port;


    protected WebsocketHttpServer(int port) {
        this.port = port;
    }

    /**
     * Returns the same instance for all the calls (Singleton)
     * @return
     */
    public static synchronized WebsocketHttpServer getInstance(int port, Class ...endpointClasses) {
        if (instance == null) {
            instance = new WebsocketHttpServer(port);
        }
        return instance;
    }

    protected List<ServerConnector> getConnectors() {
        List<ServerConnector> result = new ArrayList<>();
        // We set up the HTTP Connector...
        ServerConnector connector = new ServerConnector(super.server);
        connector.setPort(port);
        result.add(connector);

        return result;
    }

    /**
     * Main method. If you want to start the Server out of the scope of a JunitTest, use
     * this method.
     * @param args
     */
    public static void main(String ...args) {
        try {
            WebsocketHttpServer httpServer = WebsocketHttpServer.getInstance(8080);
            httpServer.start(BroadcastEndpoint.class, BroadcastAdvancedEndpoint.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
