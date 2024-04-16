package com.bridge.demo.service.impl;

import com.bridge.RedisConstants;
import com.bridge.demo.service.PlayService;
import com.bridge.demo.utils.RedisUtils;
import com.bridge.entity.Game;
import com.bridge.entity.Play;
import com.bridge.entity.Round;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class PlayServiceImpl implements PlayService {

    @Override
    public Game play(Play currentPlay) throws Exception {
        String gameKey = RedisConstants.getGameKey();
        String gameId = currentPlay.getGameId();
        if (RedisUtils.checkKeyAndField(gameKey, gameId)) {
            Game targetGame = RedisUtils.getFromRedis(gameKey, gameId, Game.class);
            List<Round> rounds;
            if (null == targetGame.getRounds()) {
                Round newRound = new Round();
                newRound.setPlays(List.of(currentPlay));
                targetGame.setRounds(List.of(newRound));
            } else {
                rounds = targetGame.getRounds();
                Round lastRound = rounds.get(rounds.size() - 1);
                List<Play> nowPlay = lastRound.getPlays();
                List<Play> newPlay = new ArrayList<>(nowPlay);
                newPlay.add(currentPlay);
                lastRound.setPlays(newPlay);
            }

            RedisUtils.insertRedis(gameKey, gameId, targetGame);
            return targetGame;
        } else {
            log.warn("Game {} doesn't exist.", gameId);
            throw new Exception();
        }
    }
}
