package com.bridge.entity.websocket;

import com.bridge.entity.Call;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class WebsocketNotifyCall extends WebsocketNotifyBase {

    private String account;
    private Call call;

}
