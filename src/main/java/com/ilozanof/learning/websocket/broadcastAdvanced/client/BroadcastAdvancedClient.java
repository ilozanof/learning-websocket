package com.ilozanof.learning.websocket.broadcastAdvanced.client;

import com.ilozanof.learning.websocket.broadcastAdvanced.server.Message;
import com.ilozanof.learning.websocket.broadcastAdvanced.server.MessageDecoder;
import com.ilozanof.learning.websocket.broadcastAdvanced.server.MessageEncoder;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint(encoders = {MessageEncoder.class},
                decoders = {MessageDecoder.class})
public class BroadcastAdvancedClient {

    private String clientId = null;
    private WebSocketContainer container = null;
    private Session session = null;

    public BroadcastAdvancedClient(String clientId, String endpointURL) throws Exception {
        this.clientId = clientId;
        container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, URI.create(endpointURL));

    }

    @OnMessage
    public void onMessage(Message message) {
        // We receive a message from the Server Endpoint...
        System.out.println(clientId + ": Received Message: " + message);
    }

    public void sendMessage(Message message) throws Exception {
        // We send a message to the Server Endpoint...
        System.out.println(clientId + ": sending message '" + message + "'...");
        session.getBasicRemote().sendObject(message);
    }

    public String getClientId() {
        return clientId;
    }

    public static void main(String ...args) throws Exception {
        BroadcastAdvancedClient client = new BroadcastAdvancedClient("test", "ws://localhost:8080/boradcast");
        client.sendMessage(new Message("a Java client", "Hi There!", "1.0"));
    }
}
