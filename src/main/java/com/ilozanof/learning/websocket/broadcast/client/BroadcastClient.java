package com.ilozanof.learning.websocket.broadcast.client;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class BroadcastClient {

    private String clientId = null;
    private WebSocketContainer container = null;
    private Session session = null;

    public BroadcastClient(String clientId, String endpointURL) throws Exception {
        this.clientId = clientId;
        container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, URI.create(endpointURL));

    }

    @OnMessage
    public void onMessage(String message) {
        // We receive a message from the Server Endpoint...
        System.out.println(clientId + ": Received Message: " + message);
    }

    public void sendMessage(String message) throws Exception {
        // We send a message to the Server Endpoint...
        System.out.println(clientId + ": sending message '" + message + "'...");
        session.getBasicRemote().sendText(message);
    }

    public String getClientId() {
        return clientId;
    }

    public static void main(String ...args) throws Exception {
        BroadcastClient client = new BroadcastClient("test", "ws://localhost:8080/boradcast");
        client.sendMessage("Hi There!");
    }
}
