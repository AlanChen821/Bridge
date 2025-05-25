package com.bridge.enumeration;

import java.util.Optional;

public enum GameStatus {
    WAITING(0),    //  等待玩家
    START(1),      //  已達房間人數要求, 等待房主開始
    SHUFFLING(2),  //  洗牌 & 發牌
    CALLING(3),    //  叫牌
    PLAYING(4),    //  出牌
    SETTLING(5),   //  結算
    FINISHED(6);   //  結束

    private final Integer code;

    GameStatus(Integer code) {
        this.code = code;
    }

    public static Optional<GameStatus> getByCode(Integer code) {
        for (GameStatus status : values()) {
            if (status.code == code) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }

    public Integer getCode() {
        return this.code;
    }
}
