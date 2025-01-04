package com.bridge.service;

import com.bridge.entity.Game;
import com.bridge.enumeration.GameStatus;

import java.util.List;

public interface IGameService {
    List<Game> getGameList();

    Game enterGame(String token, Long gameId, Game targetGame) throws Exception;

    Game getGame(Long gameId) throws Exception;

    Game changeGameStatus(Long gameId, GameStatus gameStatus) throws Exception;
}
