package com.bridge.enumeration;

public enum WebsocketNotifyType {
    ENTRY("/topic/entry"),
    READY("/topic/begin"),
    BEGIN("/topic/begin"),
    SHUFFLE("/topic/shuffle"),
    CALL("/topic/call"),
    PLAY("/topic/play");

    public final String destination;

    WebsocketNotifyType(String destination) {
        this.destination = destination;
    }
}
