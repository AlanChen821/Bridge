package com.bridge.service.impl;

import com.bridge.constant.RedisConstants;
import com.bridge.entity.Game;
import com.bridge.entity.user.Player;
import com.bridge.entity.websocket.WebsocketNotifyBegin;
import com.bridge.entity.websocket.WebsocketNotifyEntry;
import com.bridge.enumeration.GameStatus;
import com.bridge.enumeration.WebsocketNotifyType;
import com.bridge.repository.GameRepository;
import com.bridge.service.IGameService;
import com.bridge.service.IShuffleService;
import com.bridge.utils.JsonUtils;
import com.bridge.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bridge.constant.WebsocketDestination.TOPIC_BEGIN;
import static com.bridge.constant.WebsocketDestination.TOPIC_ENTRY;

@Service
@Slf4j
public class GameServiceImpl implements IGameService {

//    @Autowired
//    private GameRepository gameRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final IShuffleService shuffleService;

    public GameServiceImpl(SimpMessagingTemplate simpMessagingTemplate, IShuffleService shuffleService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.shuffleService = shuffleService;
    }

    @Override
    public List<Game> getGameList() {
        List<Game> results = null;
        
        //  redis part
        String gameKey = RedisConstants.GAME_KEY;
        if (RedisUtils.checkKey(gameKey)) {
            Map<String, Game> gameMap = RedisUtils.getFromRedis(gameKey, Game.class);
            results = new ArrayList<Game>(gameMap.values());
            
        }
//        gameRepository.findAll();
        return results; 
    }

    @Override
    public Game enterGame(String token, Long gameId, Game targetGame) throws Exception {
        //  db part
//        Optional<Game> game = gameRepository.findById(targetGame.getId());

        //  check whether the game exists or not.
//        if (game.isEmpty()) {
//            String message = String.format("Specified game %s doesn't exist.", targetGame.getId());
//            log.warn(message);
//            throw new Exception(message);
//        }

        Game enteredGame;
//                = game.get();
        Player player = new Player(token);

//          redis part
        String gameKey = RedisConstants.GAME_KEY;
        if (RedisUtils.checkKey(gameKey)) {
            Map<String, Game> gameMap = RedisUtils.getFromRedis(gameKey, Game.class);
            Optional<Map.Entry<String, Game>> firstWaitingGame = gameMap.entrySet().stream().filter(entry -> GameStatus.WAITING.equals(entry.getValue().getStatus())).findFirst();
            if (firstWaitingGame.isPresent()) {
                enteredGame = firstWaitingGame.get().getValue();
                enteredGame.addNewPlayer(player);
                RedisUtils.insertRedis(gameKey, enteredGame.getId().toString(), enteredGame);
            } else {
                enteredGame = this.createNewGame(player);
//                this.gameRepository.saveAndFlush(enteredGame);
            }
        } else {
            //  otherwise, attend to the existing room.
            enteredGame = this.createNewGame(player);
//            this.gameRepository.saveAndFlush(enteredGame);
        }

//        gameRepository.save(enteredGame);

//        int result = gameParameterJdbcTemplate.queryForObject("SELECT * from game", Game.class);

//        List<Game> list = gameMapper.getList();

//        String account = player.getAccount();
//        String encodedAccount = Base64.getEncoder().encodeToString(account.getBytes());

        log.info("Player {} has entered game {}.", player.getAccount(), enteredGame.getId());
//        enteredGame.addNewPlayer(player);

        WebsocketNotifyEntry websocketNotifyEntry = WebsocketNotifyEntry.builder()
                .type(WebsocketNotifyType.ENTRY)
                .message("new player " + player.getAccount() + " has entered the room " + enteredGame.getRoomName())
                .createTime(new Timestamp(System.currentTimeMillis()))
                .build();
        simpMessagingTemplate.convertAndSend(TOPIC_ENTRY, JsonUtils.serialize(websocketNotifyEntry));

        if (enteredGame.getPlayers().size() == 4) {
            log.info("The room is full, let the game begin!");
            enteredGame.setStatus(GameStatus.CALLING);
            RedisUtils.insertRedis(gameKey, String.valueOf(enteredGame.getId()), enteredGame);

            WebsocketNotifyBegin notifyBegin = WebsocketNotifyBegin.builder()
                    .type(WebsocketNotifyType.BEGIN)
                    .message("The room is full, let the game begin!")
                    .build();
            simpMessagingTemplate.convertAndSend(TOPIC_BEGIN, JsonUtils.serialize(notifyBegin));

            log.info("Start to shuffle...");
            shuffleService.shuffle();
        }

        return enteredGame;
    }

    private Game createNewGame(Player firstPlayer) {
        String gameKey = RedisConstants.GAME_KEY;
        Game newGame = new Game(firstPlayer);
        RedisUtils.insertRedis(gameKey, String.valueOf(newGame.getId()), newGame);
        return newGame;
    }

    @Override
    public Game getGame(Long gameId) throws Exception {
        String gameKey = RedisConstants.GAME_KEY;
        if (RedisUtils.checkKeyAndField(gameKey, gameId.toString())) {
            Game targetGame = RedisUtils.getFromRedis(gameKey, gameId.toString(), Game.class);
            if (null == targetGame) {
                log.warn("Target game : {} isn't found.", gameId);
                throw new Exception("Target game : " + gameId + " isn't found.");
            }
            log.info("Target game : {} is found.", gameId);
            return targetGame;
        }
        return null;
    }

    @Override
    public Game changeGameStatus(Long gameId, GameStatus gameStatus) throws Exception {
        Game targetGame = this.getGame(gameId);

        if (null == targetGame) {
            throw new Exception("Target game : " + gameId + " isn't found.");
        }

        targetGame.setStatus(gameStatus);
        String gameKey = RedisConstants.GAME_KEY;
        if (RedisUtils.checkKeyAndField(gameKey, gameId.toString())) {
            RedisUtils.insertRedis(gameKey, gameId.toString(), targetGame);
        }

        return targetGame;
    }

}
