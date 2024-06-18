package com.bridge.service.impl;

import com.bridge.RedisConstants;
import com.bridge.entity.Game;
import com.bridge.entity.user.Player;
import com.bridge.enumeration.GameStatus;
import com.bridge.mapper.GameMapper;
import com.bridge.service.ILoginService;
import com.bridge.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private GameMapper gameMapper;

    @Override
    public Game loginAsGuest(Player player) {
        Game targetGame;

        List<Game> list = gameMapper.getList();

//        String account = player.getAccount();
//        String encodedAccount = Base64.getEncoder().encodeToString(account.getBytes());
        log.info("Player {} has logged in.", player.getAccount());
        //  check whether there's a waiting room, if there isn't, create a new one.
        String gameKey = RedisConstants.GAME_KEY;
        if (RedisUtils.checkKey(gameKey)) {
            Map<String, Game> gameMap = RedisUtils.getFromRedis(gameKey, Game.class);
            Optional<Map.Entry<String, Game>> firstWaitingGame = gameMap.entrySet().stream().filter(entry -> GameStatus.WAITING.equals(entry.getValue().getStatus())).findFirst();
            if (firstWaitingGame.isPresent()) {
                targetGame = firstWaitingGame.get().getValue();
                targetGame.addNewPlayer(player);
                RedisUtils.insertRedis(gameKey, targetGame.getGameId(), targetGame);
            } else {
                 targetGame = this.createNewGame(player);
            }
        } else {
            //  otherwise, attend to the existing room.
            targetGame = this.createNewGame(player);
        }

        return targetGame;
    }

    private Game createNewGame(Player firstPlayer) {
        String gameKey = RedisConstants.GAME_KEY;
        Game newGame = new Game(firstPlayer);
        RedisUtils.insertRedis(gameKey, newGame.getGameId(), newGame);
        return newGame;
    }
}
