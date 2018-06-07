package com.ilozanof.learning.websocket;

import com.ilozanof.learning.websocket.broadcast.server.BroadcastEndpoint;
import com.ilozanof.learning.websocket.broadcastAdvanced.server.BroadcastAdvancedEndpoint;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.server.ServerContainer;

/**
 * This class launches a webserver (a Jetty server) and add some websockets
 * end ooints to it.
 */
public class WebsocketServer {

    /**
     * Initilize the Netty Server, register the websocket endpoints in it,
     * and launches it.
     *
     * @param port                  web server port number
     * @param endpointClasses       Array with the websocket endpoints to
     *                              register (@ServerEndpoint annotated classes)
     */
    public static void startServer(int port, Class ...endpointClasses)  {
        try {
            // We create the Jetty Web Server...
            Server server = new Server();

            // We set up the
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(port);
            server.addConnector(connector);

            // We set up the ROOT applicatiopn context...
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);

            // We add the Websocket endpoints, if any
            if (endpointClasses != null) {
                ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
                for (Class endpointClass : endpointClasses) {
                    wscontainer.addEndpoint(endpointClass);
                }
            }

            // we start the Server...
            server.start();
            server.dumpStdErr();
            server.join();
        } catch (Exception e) {
            System.out.println("EROR starting the Server!!!");
            e.printStackTrace();
        }

    }

    /**
     * Main method. If you want to start the Server out of the scope of a JunitTest, use
     * this method.
     * @param args
     */
    public static void main(String ...args) {
        WebsocketServer.startServer(8080, BroadcastEndpoint.class,
                                                BroadcastAdvancedEndpoint.class);

    }

}
