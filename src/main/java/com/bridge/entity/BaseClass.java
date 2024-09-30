package com.bridge.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BaseClass {
    private String gameId; //  todo : both of gameId & playerId will be moved into token or other place.

    private String playerId;

    private Long id;

    private Timestamp createTime;

    private Timestamp updateTime;
}
