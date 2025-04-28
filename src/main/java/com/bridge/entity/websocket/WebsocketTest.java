package com.bridge.entity.websocket;

import com.bridge.enumeration.WebsocketNotifyType;
import lombok.Data;

@Data
public class WebsocketTest {

    private WebsocketNotifyType websocketNotifyType;

    private Long userId;
}
