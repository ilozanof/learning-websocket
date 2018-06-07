# learning-websocket

This project contains different examples of the use of *Websockets* (see below chapters for explanations of each example).

> **NOTE**: 
> All the following examples make use of an embedded web server, the **Netty** server. This server is included as a dependency library in *gradle* (see *build.gradle*), and it's needed for all the tests to work.

> The class *com.ilozanof.learning.websocket.WebsocketServer.java* takes care of running this server, and it's used by all the Tests in this project. We can use this class to "register" in the server the websockets endpoints and launch the sever right before we run the Clients.

## Structure of the  examples

### broadcast
In this example, we have:
 * 1 Endpoint in the Server. 
   It keeps track of all the Sessions opened by different clientes, and for each Message it receives, it sends it back to ALL the clients (including the one who sent it).
 * 1 Java Client that sends messages and also works as a Listener for incoming messags feom the Server.
 * 1 Web Client, implemented in (HTML + CSS + JS + jQuery). The source code is composed of the following files:
   * *broadcastClient.html*: The HTML containing the user interface
   * *clientCommon.css": Commons styles for all the examples in this project.
   * *clientCommon.js": Commons JS functions
   * messageHandling.js": Implementation of fiunctions responsible for encoding/decoding messages from/to the WebSocket.
   
 ### broadcast Advanced
 
 This example provides the same functionality as the previous one, but in this case the information exchange 
 between the client and the server is NOT plain text, but a *Message* object.
 
 All the explanations for the previous example also apply for this one, with some additions in the form of:
  * A new *Message* class
  * Encoders and decoders in the Server side for *Message* objects.
 
 
 
