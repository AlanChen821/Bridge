package com.bridge.service.impl;

import com.bridge.constant.RedisConstants;
import com.bridge.entity.Call;
import com.bridge.entity.Game;
import com.bridge.entity.Play;
import com.bridge.entity.Round;
import com.bridge.entity.card.CallType;
import com.bridge.entity.user.Player;
import com.bridge.entity.websocket.WebsocketNotifyBegin;
import com.bridge.entity.websocket.WebsocketNotifyCall;
import com.bridge.entity.websocket.WebsocketNotifyEntry;
import com.bridge.entity.websocket.WebsocketNotifyPlay;
import com.bridge.enumeration.GameStatus;
import com.bridge.enumeration.WebsocketNotifyType;
import com.bridge.mapper.GameMapper;
import com.bridge.repository.GameRepository;
import com.bridge.service.IGameService;
import com.bridge.service.IShuffleService;
import com.bridge.utils.JsonUtils;
import com.bridge.utils.LocalDateTimeUtils;
import com.bridge.utils.RedisUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bridge.constant.WebsocketDestination.*;
import static com.bridge.constant.WebsocketDestination.TOPIC_CALL;

@Service
@Slf4j
public class GameServiceImpl implements IGameService {

//    @Autowired
//    private GameRepository gameRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final IShuffleService shuffleService;

    private final GameMapper gameMapper;

    public GameServiceImpl(SimpMessagingTemplate simpMessagingTemplate, IShuffleService shuffleService, GameMapper gameMapper) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.shuffleService = shuffleService;
        this.gameMapper = gameMapper;
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

        List<Game> gameList = gameMapper.getList();

//        gameRepository.findAll();
        return results; 
    }

    @Override
    public Game createGame(String token, Game newGame) {
        Player player = new Player(token);
        String gameKey = RedisConstants.GAME_KEY;
        Game targetGame = this.createNewGame(player, newGame.getRoomName());
        return targetGame;
    }

    @Override
    public Game enterGame(String token, Long gameId) throws Exception {
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
        if (RedisUtils.checkKeyAndField(gameKey, gameId.toString())) {
            enteredGame = RedisUtils.getFromRedis(gameKey, gameId.toString(), Game.class);
            if (null != enteredGame) {
                enteredGame.addNewPlayer(player);
                RedisUtils.insertRedis(gameKey, gameId.toString(), enteredGame);
            } else {
                String message = String.format("Specified game %s doesn't exist.", gameId);
                log.warn(message);
                throw new Exception(message);
            }

//            this.gameRepository.saveAndFlush(enteredGame);
        } else {
            //  otherwise, attend to the existing room.
            enteredGame = this.createNewGame(player);
//            this.gameRepository.saveAndFlush(enteredGame);
        }

//        gameRepository.save(enteredGame);

//        int result = gameParameterJdbcTemplate.queryForObject("SELECT * from game", Game.class);

//        List<Game> list = gameMapper.getList();

        log.info("Player {} has entered game {}.", player.getAccount(), enteredGame.getId());

        WebsocketNotifyEntry websocketNotifyEntry = WebsocketNotifyEntry.builder()
                .type(WebsocketNotifyType.ENTRY)
                .message("new player " + player.getAccount() + " has entered the room " + enteredGame.getRoomName())
                .createTime(new Timestamp(System.currentTimeMillis()))
                .build();
        simpMessagingTemplate.convertAndSend(TOPIC_ENTRY, JsonUtils.serialize(websocketNotifyEntry));

        if (enteredGame.getPlayers().size() == 4) {
            Player roomMaster = enteredGame.getPlayers().get(0);
            WebsocketNotifyEntry websocketNotifyReady = WebsocketNotifyEntry.builder()
                    .type(WebsocketNotifyType.READY)
                    .message("new player " + player.getAccount() + " has entered the room " + enteredGame.getRoomName())
                    .createTime(new Timestamp(System.currentTimeMillis()))
                    .build();
            //  test
            roomMaster.setId(1L);
            simpMessagingTemplate.convertAndSend(TOPIC_BEGIN + "/" + roomMaster.getId(), JsonUtils.serialize(websocketNotifyReady));

        }

        return enteredGame;
    }

    private Game createNewGame(Player firstPlayer) {
        String gameKey = RedisConstants.GAME_KEY;
        Game newGame = new Game(firstPlayer);
        RedisUtils.insertRedis(gameKey, String.valueOf(newGame.getId()), newGame);
        return newGame;
    }

    private Game createNewGame(Player firstPlayer, String gameName) {
        String gameKey = RedisConstants.GAME_KEY;
        Game newGame = new Game(firstPlayer);
        newGame.setRoomName(gameName);
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

        log.info("Change the game {} to status {}", gameId, gameStatus);

        targetGame.setStatus(gameStatus);
        String gameKey = RedisConstants.GAME_KEY;
        if (RedisUtils.checkKeyAndField(gameKey, gameId.toString())) {
            RedisUtils.insertRedis(gameKey, gameId.toString(), targetGame);
        }

        switch (gameStatus) {
            case START:
                targetGame.setStatus(GameStatus.CALLING);
                RedisUtils.insertRedis(gameKey, String.valueOf(targetGame.getId()), targetGame);

                WebsocketNotifyBegin notifyBegin = WebsocketNotifyBegin.builder()
                        .type(WebsocketNotifyType.BEGIN)
                        .message("The room is full, let the game begin!")
                        .build();
                simpMessagingTemplate.convertAndSend(TOPIC_BEGIN, JsonUtils.serialize(notifyBegin));

                log.info("Start to shuffle...");
                shuffleService.shuffle();
                break;
        }

        return targetGame;
    }

    public Game call(Long gameId, Call currentCall) throws Exception {
//        String gameId = currentCall.getGameId();
        Game game;
        List<Call> callHistory;
        String gameKey = RedisConstants.GAME_KEY;
        if (RedisUtils.checkKeyAndField(gameKey, gameId)) {  //  此局遊戲已存在, 查出過去的 bid history 做判斷並更新
            game = RedisUtils.getFromRedis(gameKey, gameId, Game.class);
            callHistory = game.getCallHistory();
            if (null == callHistory) {
                //  is the first call "PASS" valid?
                callHistory = new ArrayList<>(List.of(currentCall));
            } else {
                //  查出上一次的有效叫牌(PASS 以外), 若無此叫牌則拋 exception
                Call lastValidCall = Lists.reverse(callHistory)
                        .stream()
                        .filter(call -> !call.getCallType().isPass())
                        .findFirst()
                        .orElseThrow(Exception::new);
                Boolean isValid = currentCall.validate(lastValidCall);
                if (isValid) {
                    callHistory.add(currentCall);
                    //  check whether the game can begin.
                    if (CallType.PASS.equals(currentCall.getCallType())) {
                        int lastValidBidIndex = callHistory.indexOf(lastValidCall);
                        if (callHistory.size() - lastValidBidIndex > 3) {
                            //  All other players have passed.
                            log.info("All other players have passed, let the game begin!");
                            game.setTrump(lastValidCall.getCallType());
                            game.setLevel(lastValidCall.getNumber());
                            game.setStatus(GameStatus.PLAYING);
                        } else {
                            log.info("Player " + currentCall.getPlayerId() + " has called type " + currentCall.getCallType() + " with number " + currentCall.getNumber());
                        }
                    }
                } else {
                    String errorMessage = "Call " + JsonUtils.serialize(currentCall) + " is invalid.";
                    log.warn(errorMessage);
                    throw new Exception(errorMessage);
                }
            }
        } else {
            //  此局遊戲尚未存在, 為第一次叫牌, 直接新增 bid history. -> 直接回錯誤, 進入叫牌階段的遊戲一定要存在
            String errorMessage = "Can't find the GameId : " + gameId;
            log.error(errorMessage);
            throw new Exception(errorMessage);
        }

        //  write into redis
        game.setCallHistory(callHistory);
        game.setUpdateTime(LocalDateTimeUtils.getStringOfNow());
        RedisUtils.insertRedis(gameKey, gameId, game);

        WebsocketNotifyCall websocketNotifyCall = WebsocketNotifyCall.builder()
                .call(currentCall)
                .createTime(new Timestamp(System.currentTimeMillis()))
                .build();
        simpMessagingTemplate.convertAndSend(TOPIC_CALL, JsonUtils.serialize(websocketNotifyCall));

        if (game.getStatus().equals(GameStatus.PLAYING)) {
            log.info("Game {} has started, trump : {}, level : {}.", game.getRoomName(), game.getTrump(), game.getLevel());
            websocketNotifyCall = WebsocketNotifyCall.builder()
                    .type(WebsocketNotifyType.CALL)
                    .call(currentCall)
                    .message(String.format("Game %s has started, trump : %s, level : %d.", game.getRoomName(), game.getTrump(), game.getLevel()))
                    .createTime(new Timestamp(System.currentTimeMillis()))
                    .build();
            simpMessagingTemplate.convertAndSend(TOPIC_CALL, JsonUtils.serialize(websocketNotifyCall));
        }
        return game;
    }

    @Override
    public Game play(String token, Long gameId, Play currentPlay) throws Exception {
        String gameKey = RedisConstants.GAME_KEY;
//        String gameId = currentPlay.getGameId();
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
                List<Play> originalPlay = lastRound.getPlays();
                List<Play> newPlay = new ArrayList<>(originalPlay);
                newPlay.add(currentPlay);
                if (newPlay.size() == 4) {
                    //  all players have played, start to judge
                    CallType trump = targetGame.getTrump();
                    int trumpLevel = trump.getLevel();
                    Play winner = newPlay.get(0);
                    for (int i = 1; i < 4 ; i++) {
                        Play next = newPlay.get(i);
                        if ((winner.getCard().getSuit().getOrder() == trumpLevel
                                && next.getCard().getSuit().getOrder() == trumpLevel) ||
                                (winner.getCard().getSuit().getOrder() != trumpLevel
                                        && next.getCard().getSuit().getOrder() != trumpLevel)) {
                            //  both are or aren't trump, compare numbers
                            if (winner.getCard().getNumber() < next.getCard().getNumber()) {
                                log.info("current winner : {} is lower than next : {}", winner.getCard(), next.getCard());
                                winner = next;
                            }
                        } else if (winner.getCard().getSuit().getOrder() != trumpLevel) {
                            log.info("current winner : {} is lower than next : {}", winner.getCard(), next.getCard());
                            winner = next;
                        }
                    }
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
