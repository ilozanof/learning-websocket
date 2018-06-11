/*
 * Common Functions
 * ----------------
 * This file contains commons functions, used by both the "broadcast"
 * and "broadcastAdvanced" examples. Some of these functions rely in turn
 * on some other functions like "getWebSocketUrl()", "encodeMessage()" or
 * "decodeMessage()", which are defined in "messageHandling.js" or
 * "messageAdvancedHAndling.js" (only one of those files is included in
 * the page, depending on the client/HTML we are testing).

 */

// Global Declarations...

// WebSocket channel instance...
var webSocket = null;

// Variable to check whether we use HTTP or SSL:
var useSSL = false;


// Init the WebSocket connection...
function initWebSocket() {
    if ("WebSocket" in window) {
        var serverUrl = getWebSocketUrl(useSSL);
        console.log(" -> init webSocket to: " + serverUrl);
        webSocket = new WebSocket(serverUrl);

        webSocket.onmessage = function(evt) {
            // We show the Message on the Screen...
            console.log("a messsage is coming!");
            var msg = decodeMessage(evt.data);
            $('#messagesBox').append(msg + " </br>");
        }

        webSocket.onclose = function(evt) {
            //alert("Connection Closed, code: " + evt.code + ", reason: " + evt.reason);
            //alert("WebSocket Connection Closed.");
        }

        webSocket.onerror = function(evt) {
            alert("ERROR during WebSocket connection to " + serverUrl + ". Are you sure the Server is up and running?")
        }

    } else alert("WebSocket is NOT Supported oin this browser. This example is NOT going to work.")
}

// On-load Page initialization...
$(document).ready(function() {

    // We show the Websocket URL...
    $('#websocketUrl').html(getWebSocketUrl(useSSL));

    // "onClick" event handler for the Button:
    // it sends the messsage through the Websocket channel

    $('#sendButton').click(function() {
        console.log("Sending message...");
        var message = $('#message').val();
        var encodedMessage = encodeMessage(message);
        webSocket.send(encodedMessage);
        $('#message').val('');
    });

    // "onChange" event handler for the radio Button...
    $("input[name='connectionType']").change(function() {

        // We switch connection:
        useSSL = !useSSL;
        // First we close the Websocket channel and we open another one...
        webSocket.close();
        initWebSocket();

        // Then we update the URL on the web page...
        $('#websocketUrl').html(getWebSocketUrl(useSSL));
    })

    // We init the WebSocket connection:
    initWebSocket();

});

