
package com.bridge.service.impl;

import com.bridge.constant.RedisConstants;
import com.bridge.entity.websocket.WebsocketNotifyCall;
import com.bridge.enumeration.WebsocketNotifyType;
import com.bridge.service.ICallService;
import com.bridge.utils.JsonUtils;
import com.bridge.utils.LocalDateTimeUtils;
import com.bridge.utils.RedisUtils;
import com.bridge.entity.Call;
import com.bridge.entity.Game;
import com.bridge.entity.card.CallType;
import com.bridge.enumeration.GameStatus;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

import static com.bridge.constant.WebsocketDestination.TOPIC_CALL;

@Service
@Slf4j
public class CallServiceImpl implements ICallService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public CallServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public Game call(Call currentCall) throws Exception {
        String gameId = currentCall.getGameId();
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
}