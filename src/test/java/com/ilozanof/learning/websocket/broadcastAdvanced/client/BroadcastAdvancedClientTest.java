package com.ilozanof.learning.websocket.broadcastAdvanced.client;

import com.ilozanof.learning.websocket.WebsocketServer;
import com.ilozanof.learning.websocket.broadcast.client.BroadcastClient;
import com.ilozanof.learning.websocket.broadcast.server.BroadcastEndpoint;
import com.ilozanof.learning.websocket.broadcastAdvanced.server.BroadcastAdvancedEndpoint;
import com.ilozanof.learning.websocket.broadcastAdvanced.server.Message;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Test class for the Broadcastclient class
 */
public class BroadcastAdvancedClientTest {

    private final int SERVER_PORT = 8080;

    @Before
    public void init() throws Exception {
        System.out.println("> Initializing...");
    }


    private void startServer() {
        try {
            new Thread(() -> {
                WebsocketServer.startServer(SERVER_PORT, BroadcastAdvancedEndpoint.class);
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
        BroadcastAdvancedClient client = null;
        try {
            // We start the server, ready to receive websocket requests...
            startServer();

            client = new BroadcastAdvancedClient("Single Client", "ws://localhost:8080/broadcast");
            client.sendMessage(new Message("a Java client", "Hi There!", "1.0"));

            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMultipleClients() {
        final int NUM_CLIENTS = 2;
        List<BroadcastAdvancedClient> clients = new ArrayList<>();
        try {
            // We start the server, ready to receive websocket requests...
            startServer();

            // We initialize the clients...
            IntStream.range(0,NUM_CLIENTS).forEach( i -> {
                try {
                    clients.add(new BroadcastAdvancedClient("Client-" + i, "ws://localhost:8080/broadcast"));
                } catch (Exception e) {e.printStackTrace();}
            });

            // We send one message by one of the clients:
            // Since all the messages received at the server are broadcasted, each
            // message should be received NUM_CLIENTES * NUM_CLIENTES times

            IntStream.range(0,NUM_CLIENTS).forEach( i -> {
                try {
                    BroadcastAdvancedClient client = clients.get(i);
                    client.sendMessage(new Message("a Java client", "Greetings from " + client.getClientId(), "1.0"));
                } catch (Exception e) {e.printStackTrace();}
            });

            Thread.sleep(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
