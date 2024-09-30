package com.bridge.entity.websocket;

import com.bridge.entity.Call;
import com.bridge.entity.card.Card;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class WebsocketNotifyPlay extends WebsocketNotifyBase {

    private String account;
    private Card card;

}
