package com.bridge.service.impl;

import com.bridge.RedisConstants;
import com.bridge.entity.Game;
import com.bridge.entity.user.Player;
import com.bridge.enumeration.GameStatus;
import com.bridge.repository.GameRepository;
import com.bridge.service.IGameService;
import com.bridge.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class GameServiceImpl implements IGameService {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public List<Game> getGameList() {
        return gameRepository.findAll();
    }

    @Override
    public Game enterGame(String token) {
        Game targetGame;

//        targetGame = gameRepository.getById(1L);

//        int result = gameParameterJdbcTemplate.queryForObject("SELECT * from game", Game.class);

//        List<Game> list = gameMapper.getList();

//        String account = player.getAccount();
//        String encodedAccount = Base64.getEncoder().encodeToString(account.getBytes());
        Player player = new Player();

        log.info("Player {} has logged in.", player.getAccount());
        //  check whether there's a waiting room, if there isn't, create a new one.
        String gameKey = RedisConstants.GAME_KEY;
        if (RedisUtils.checkKey(gameKey)) {
            Map<String, Game> gameMap = RedisUtils.getFromRedis(gameKey, Game.class);
            Optional<Map.Entry<String, Game>> firstWaitingGame = gameMap.entrySet().stream().filter(entry -> GameStatus.WAITING.equals(entry.getValue().getStatus())).findFirst();
            if (firstWaitingGame.isPresent()) {
                targetGame = firstWaitingGame.get().getValue();
                targetGame.addNewPlayer(player);
                RedisUtils.insertRedis(gameKey, targetGame.getId(), targetGame);
            } else {
                targetGame = this.createNewGame(player);
                this.gameRepository.saveAndFlush(targetGame);
            }
        } else {
            //  otherwise, attend to the existing room.
            targetGame = this.createNewGame(player);
            this.gameRepository.saveAndFlush(targetGame);
        }

        return targetGame;
    }

    private Game createNewGame(Player firstPlayer) {
        String gameKey = RedisConstants.GAME_KEY;
        Game newGame = new Game(firstPlayer);
        RedisUtils.insertRedis(gameKey, newGame.getId(), newGame);
        return newGame;
    }
}
