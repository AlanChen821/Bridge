package com.bridge.enumeration;

public enum GameStatus {
    WAITING,    //  等待玩家
    START,
    SHUFFLING,  //  洗牌 & 發牌
    CALLING,    //  叫牌
    PLAYING,    //  出牌
    SETTLING,   //  結算
    FINISHED;   //  結束
}
