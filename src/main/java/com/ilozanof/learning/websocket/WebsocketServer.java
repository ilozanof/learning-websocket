package com.ilozanof.learning.websocket;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.server.ServerContainer;
import java.util.List;

/**
 * This class launches a webserver (a Jetty server) and adds some websockets
 * and ooints to it.
 */
public abstract class WebsocketServer {


    // Jetty Server variables...
    protected Server server = null;
    protected ServletContextHandler context = null;


    /**
     * Initialize the default ROOT context
     */
    private void initContext() {
        context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
    }

    // This method will hage to be implemented by all the subclasses from this one.
    protected abstract List<ServerConnector> getConnectors();

    /**
     * It adds several websocket endpoints to this Server Configuration
     *
     * @param endpointClasses   Classes annotated with @ServerEndpoint
     * @throws Exception
     */
    private void addWebsocketEndpoints(Class ...endpointClasses) throws Exception {
        if (endpointClasses != null) {
            ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
            for (Class endpointClass : endpointClasses) {
                wscontainer.addEndpoint(endpointClass);
            }
        }
    }

    /**
     * Launches the Server, with the Endpoints specified, and keeps it running in the background
     * until this process is killed.
     *
     * @param endpointClasses classes annotated with @ServerEndpoint
     */
    public void start(Class ...endpointClasses) {
        try {
            this.server = new Server();
            List<ServerConnector> connectors = getConnectors();

            for (ServerConnector serverConnector : connectors) {
                server.addConnector(serverConnector);
            }
            this.initContext();
            this.addWebsocketEndpoints(endpointClasses);

            server.start();
            server.dumpStdErr();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
