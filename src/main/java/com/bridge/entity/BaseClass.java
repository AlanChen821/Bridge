package com.bridge.entity;

import lombok.Data;

@Data
public class BaseClass {
    private String gameId; //  todo : both of gameId & playerId will be moved into token or other place.

    private String playerId;
}
