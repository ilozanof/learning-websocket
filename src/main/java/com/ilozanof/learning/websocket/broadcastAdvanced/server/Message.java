package com.ilozanof.learning.websocket.broadcastAdvanced.server;

public class Message {
    private String from;
    private String content;
    private String version;


    public Message() {}

    public Message(String from, String content, String version) {
        this.from = from;
        this.content = content;
        this.version = version;
    }
    @Override
    public String toString() {
        return "[from: " + from + ", content: '" + content + "', version: " + version + "]";
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
