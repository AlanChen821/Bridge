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
    public Game enterGame(String token, Game targetGame) throws Exception {
        Optional<Game> game = gameRepository.findById(Long.valueOf(targetGame.getId()));

        //  check whether the game exists or not.
        if (game.isEmpty()) {
            String message = String.format("Specified game %s doesn't exist.", targetGame.getId());
            log.warn(message);
            throw new Exception(message);
        }

        Game enteredGame = game.get();
        gameRepository.save(enteredGame);

//        int result = gameParameterJdbcTemplate.queryForObject("SELECT * from game", Game.class);

//        List<Game> list = gameMapper.getList();

//        String account = player.getAccount();
//        String encodedAccount = Base64.getEncoder().encodeToString(account.getBytes());
        Player player = new Player();

        log.info("Player {} try to enter game {}.", player.getAccount(), enteredGame.getId());
        enteredGame.addNewPlayer(player);

        //  redis part
//        String gameKey = RedisConstants.GAME_KEY;
//        if (RedisUtils.checkKey(gameKey)) {
//            Map<String, Game> gameMap = RedisUtils.getFromRedis(gameKey, Game.class);
//            Optional<Map.Entry<String, Game>> firstWaitingGame = gameMap.entrySet().stream().filter(entry -> GameStatus.WAITING.equals(entry.getValue().getStatus())).findFirst();
//            if (firstWaitingGame.isPresent()) {
//                enteredGame = firstWaitingGame.get().getValue();
//                enteredGame.addNewPlayer(player);
//                RedisUtils.insertRedis(gameKey, enteredGame.getId(), enteredGame);
//            } else {
//                enteredGame = this.createNewGame(player);
//                this.gameRepository.saveAndFlush(enteredGame);
//            }
//        } else {
//            //  otherwise, attend to the existing room.
//            enteredGame = this.createNewGame(player);
//            this.gameRepository.saveAndFlush(enteredGame);
//        }

        return enteredGame;
    }

    private Game createNewGame(Player firstPlayer) {
        String gameKey = RedisConstants.GAME_KEY;
        Game newGame = new Game(firstPlayer);
        RedisUtils.insertRedis(gameKey, newGame.getId(), newGame);
        return newGame;
    }
}
