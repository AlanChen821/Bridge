package com.bridge.enumeration;

public enum WebsocketNotifyType {
    ENTRY("/topic/entry"),
    READY("/topic/begin"),  //  房主專用, 通知可以開始遊戲
    BEGIN("/topic/begin"),  //  實際開始遊戲, 房內所有玩家都會收到
    SHUFFLE("/topic/shuffle"),
    CALL("/topic/call"),
    PLAY("/topic/play");

    public final String destination;

    WebsocketNotifyType(String destination) {
        this.destination = destination;
    }
}
