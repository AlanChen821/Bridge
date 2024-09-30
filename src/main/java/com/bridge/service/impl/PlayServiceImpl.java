package com.bridge.service.impl;

import com.bridge.constant.RedisConstants;
import com.bridge.entity.websocket.WebsocketNotifyPlay;
import com.bridge.enumeration.WebsocketNotifyType;
import com.bridge.service.PlayService;
import com.bridge.utils.JsonUtils;
import com.bridge.utils.RedisUtils;
import com.bridge.entity.Game;
import com.bridge.entity.Play;
import com.bridge.entity.Round;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.bridge.constant.WebsocketDestination.TOPIC_PLAY;

@Service
@Slf4j
public class PlayServiceImpl implements PlayService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public PlayServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public Game play(String token, Play currentPlay) throws Exception {
        String gameKey = RedisConstants.GAME_KEY;
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
                if (newPlay.size() == 4) {

                }
                lastRound.setPlays(newPlay);
            }

            RedisUtils.insertRedis(gameKey, gameId, targetGame);

            WebsocketNotifyPlay websocketNotifyPlay = WebsocketNotifyPlay.builder()
                    .type(WebsocketNotifyType.PLAY)
                    .account(token)
                    .card(currentPlay.getCard())
                    .createTime(new Timestamp(System.currentTimeMillis()))
                    .build();
            simpMessagingTemplate.convertAndSend(TOPIC_PLAY, JsonUtils.serialize(websocketNotifyPlay));

            return targetGame;
        } else {
            log.warn("Game {} doesn't exist.", gameId);
            throw new Exception();
        }
    }
}
