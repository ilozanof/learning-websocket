package com.ilozanof.learning.websocket.broadcastAdvanced.server;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Implementatiopn of a Server endpoint for a "Broadcast" example.
 * Every time a client send a message, that message is broadcasted
 * to ALL the Clients connected to this endpoint, including the one
 * whoe sent the message in the first place.
 */

@ServerEndpoint(value = "/broadcastAdvanced",
                decoders = MessageDecoder.class,
                encoders = MessageEncoder.class)
public class BroadcastAdvancedEndpoint {

    // A queue to store all the Sessions coming from all the clients
    // connected to this endpoint...
    private static Queue<Session> sessionsQueue = new ConcurrentLinkedQueue<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        // A client opens a connection to this EndPoint...
        // We store the Session into the Queue, to be included in the broadcast
        System.out.println("Server: Opening Connection " + session.getId());
        sessionsQueue.add(session);
    }

    @OnMessage
    public void onMessage(Session session, Message msg) throws IOException {
        // We handle the arriving of a Message, a String in this case
        // We broadcast the message to ALL the clients connected to this
        // endpoint...
        System.out.println("Server: Message received: " + msg + "' from Session " + session.getId());
        sendAll(msg);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // The client closes the connection to this endPoint.
        // we remove the session from the Queue, so it's not included in any
        // broadcast anymore...
        System.out.println("Server: Closing Session " + session.getId());
        sessionsQueue.remove(session);
    }

    public void onError(Session session, Throwable throwable) {
        // Error Handling...
        // We remove this session from the Queue...
        System.out.println("Server: Error detected in Session " + session.getId() + "!");
        sessionsQueue.remove(session);
    }

    public static synchronized void sendAll(Message msg) {

        sessionsQueue.forEach(s -> {
            try {
                if (s.isOpen()) {
                    System.out.println("Server: [" + sessionsQueue.size() + " sessions in queue]: Sending message '" + msg + "' to Session " + s.getId());
                    s.getBasicRemote().sendObject(msg);
                } else {
                    System.out.println("Server: [" + sessionsQueue.size() + " sessions in queue]: Session " + s.getId() + " closed!. Removed from the queue.");
                    sessionsQueue.remove(s);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (EncodeException ee) {
                ee.printStackTrace();
            }
        });
    }


}
