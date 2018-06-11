package com.ilozanof.learning.websocket.broadcast.client;

import com.ilozanof.learning.websocket.WebsocketHttpServer;
import com.ilozanof.learning.websocket.WebsocketServer;
import com.ilozanof.learning.websocket.broadcast.server.BroadcastEndpoint;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Test class for the Broadcastclient class
 */
public class BroadcastClientTest {

    private final int SERVER_PORT = 8080;

    @Before
    public void init() throws Exception {
        System.out.println("> Initializing...");
    }


    private void startServer() {
        try {
            new Thread(() -> {
                WebsocketHttpServer httpServer = WebsocketHttpServer.getInstance(SERVER_PORT);
                httpServer.start(BroadcastEndpoint.class);
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * We use one single client to send a message to the server endpoint, we put the
     * client on hold for 2 seconds (2000 milisecs), so it has tioe to proccess the
     * response from the Server, and then we finish.
     */
    @Test
    public void testOneClient() {
        BroadcastClient client = null;
        try {
            // We start the server, ready to receive websocket requests...
            startServer();

            client = new BroadcastClient("Single Client", "ws://localhost:8080/broadcast");
            client.sendMessage("Hi there!");

            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMultipleClients() {
        final int NUM_CLIENTS = 2;
        List<BroadcastClient> clients = new ArrayList<>();
        try {
            // We start the server, ready to receive websocket requests...
            startServer();

            // We initialize the clients...
            IntStream.range(0,NUM_CLIENTS).forEach( i -> {
                try {
                    clients.add(new BroadcastClient("Client-" + i, "ws://localhost:8080/broadcast"));
                } catch (Exception e) {e.printStackTrace();}
            });

            // We send one message by one of the clients:
            // Since all the messages received at the server are broadcasted, each
            // message should be received NUM_CLIENTES * NUM_CLIENTES times

            IntStream.range(0,NUM_CLIENTS).forEach( i -> {
                try {
                    BroadcastClient client = clients.get(i);
                    client.sendMessage("Greetings from " + client.getClientId());
                } catch (Exception e) {e.printStackTrace();}
            });

            Thread.sleep(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
