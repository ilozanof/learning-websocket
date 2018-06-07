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
var webSocket = null;

// On-load Page initialization...
$(document).ready(function() {

    // We attach the events to the HTML components...

    $('#sendButton').click(function() {
        console.log("Sending message...");
        var message = $('#message').val();
        var encodedMessage = encodeMessage(message);
        webSocket.send(encodedMessage);
        $('#message').val('');
    });

    // We check and Open the WebSocket Channel...

    if ("WebSocket" in window) {

        webSocket = new WebSocket(getWebSocketUrl());

        webSocket.onmessage = function(evt) {
            // We show the Message on the Screen...
            console.log("a messsage is coming!");
            var msg = decodeMessage(evt.data);
            $('#messagesBox').append(msg + " </br>");
        }

        webSocket.onclose = function(evt) {
            alert("Connection Closed, code: " + evt.code + ", reason: " + evt.reason);
        }

    } else alert("WebSocket is NOT Supported oin this browser. This example is NOT going to work.")
});
