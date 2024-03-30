package com.bridge.demo.service.impl;

import com.bridge.demo.service.PlayService;
import com.bridge.entity.Game;
import com.bridge.entity.Play;
import com.bridge.entity.Round;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class PlayServiceImpl implements PlayService {

    private HashMap<String, Game> gameMaps = new HashMap<>();  //  todo : move to redis & db afterwards.

    @Override
    public Game play(Play currentPlay) {
        String gameId = currentPlay.getGameId();
        if (gameMaps.containsKey(gameId)) {
            Game targetGame = gameMaps.get(gameId);
            List<Round> rounds = targetGame.getRounds();
            Round lastRound = rounds.get(rounds.size() - 1);
            List<Play> nowPlay = lastRound.getPlays();
            List<Play> newPlay = new ArrayList<>(nowPlay);
            newPlay.add(currentPlay);
            lastRound.setPlays(newPlay);
            return targetGame;
        } else {
            Round newRound = new Round();
            newRound.setPlays(Arrays.asList(currentPlay));
            Game newGame = new Game();
            newGame.setRounds(Arrays.asList(newRound));
            gameMaps.put(gameId, newGame);
            return newGame;
        }
    }
}
