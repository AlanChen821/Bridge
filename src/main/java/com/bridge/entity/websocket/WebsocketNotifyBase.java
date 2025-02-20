package com.bridge.entity.websocket;

import com.bridge.enumeration.WebsocketNotifyType;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@SuperBuilder
@NoArgsConstructor
public abstract class WebsocketNotifyBase {

    protected WebsocketNotifyType type;

    protected String message;

    protected Timestamp createTime = new Timestamp(System.currentTimeMillis());
}
