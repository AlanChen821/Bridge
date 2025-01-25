package com.bridge.service;

import com.bridge.entity.Call;
import com.bridge.entity.Game;
import com.bridge.entity.Play;
import com.bridge.enumeration.GameStatus;

import java.util.List;

public interface IGameService {
    List<Game> getGameList();

    Game createGame(String token, Game newGame);

    Game enterGame(String token, Long gameId, Game targetGame) throws Exception;

    Game getGame(Long gameId) throws Exception;

    Game changeGameStatus(Long gameId, GameStatus gameStatus) throws Exception;

    Game call(Long gameId, Call call) throws Exception;

    Game play(String token, Long gameId, Play play) throws Exception;
}
