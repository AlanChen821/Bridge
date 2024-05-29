
package com.bridge.service.impl;

import com.bridge.RedisConstants;
import com.bridge.service.ICallService;
import com.bridge.utils.LocalDateTimeUtils;
import com.bridge.utils.RedisUtils;
import com.bridge.entity.Call;
import com.bridge.entity.Game;
import com.bridge.entity.card.CallType;
import com.bridge.enumeration.GameStatus;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CallServiceImpl implements ICallService {

    @Override
    public Game call(Call currentCall) throws Exception {
        String gameId = currentCall.getGameId();
        Game game;
        List<Call> callHistory;
        String gameKey = RedisConstants.getGameKey();
        if (RedisUtils.checkKeyAndField(gameKey, gameId)) {  //  此局遊戲已存在, 查出過去的 bid history 做判斷並更新
            game = RedisUtils.getFromRedis(gameKey, gameId, Game.class);
            callHistory = game.getCallHistory();

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
                    }
                }
            } else {
                log.warn("Call {} is invalid.", currentCall);
                throw new Exception();
            }
        } else {    //  此局遊戲尚未存在, 為第一次叫牌, 直接新增 bid history.
            if (currentCall.getCallType().isPass()) {
                throw new Exception();
            }
            callHistory = new ArrayList<>(Arrays.asList(currentCall));
            game = Game.builder()
                    .gameId(gameId)
                    .callHistory(callHistory)
                    .status(GameStatus.CALLING)
                    .createTime(LocalDateTimeUtils.getStringOfNow())
                    .build();
        }
        //  write into redis
        game.setUpdateTime(LocalDateTimeUtils.getStringOfNow());
        RedisUtils.insertRedis(gameKey, gameId, game);
        return game;
    }
}