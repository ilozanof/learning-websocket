/*
 * Implementation of the functions for the broadcast Example.
 */

function getWebSocketUrl(useSSL) {
    if (useSSL)
        return "wss://127.0.0.1:8443/broadcast";
    else
        return "ws://127.0.0.1:8080/broadcast";
}

function encodeMessage(msg) {
    return msg;
}

function decodeMessage(msg) {
    return msg;
}