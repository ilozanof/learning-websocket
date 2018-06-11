/*
 * Implementation of the functions for the broadcast Advanced Example.
 */

function getWebSocketUrl(useSSL) {
    if (useSSL)
        return "wss://127.0.0.1:8443/broadcastAdvanced";
    else
        return "ws://127.0.0.1:8080/broadcastAdvanced";
}

function encodeMessage(msg) {
    var result = {
        from: "An HTML Page",
        content: msg,
        version: "1.0"
    };
    return JSON.stringify(result);
}

function decodeMessage(msg) {
    var obj = JSON.parse(msg);
    var result = "<div class='messageBox'>"
        + "<ul>Message:"
        + "<li>from:    " + obj.from + "</li>"
        + "<li>content: " + obj.content + "</li>"
        + "<li>version: " + obj.version + "</li>"
        + "</ul>"
        + "</div>";
    return result;
}