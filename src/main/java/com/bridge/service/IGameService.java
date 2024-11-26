package com.bridge.service;

import com.bridge.entity.Game;

import java.util.List;

public interface IGameService {
    List<Game> getGameList();

    Game enterGame(String token, Long gameId, Game targetGame) throws Exception;

    Game getGame(Long gameId) throws Exception;
}
