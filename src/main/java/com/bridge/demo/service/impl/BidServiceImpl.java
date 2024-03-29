package com.bridge.demo.service.impl;

import com.bridge.demo.service.IBidService;
import com.bridge.entity.Bid;
import com.bridge.entity.card.BidSuit;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BidServiceImpl implements IBidService {

    private HashMap<String, List<Bid>> records = new HashMap<>();   //  todo : move to redis & db

    @Override
    public List<Bid> bid(Bid currentBid) throws Exception {
        String gameId = currentBid.getGameId();
        List<Bid> bidHistory;
        if (records.containsKey(gameId)) {  //  此局遊戲已存在, 查出過去的 bid history 做判斷並更新
            bidHistory = records.get(gameId);
            //  查出上一次的有效叫牌(PASS 以外), 若無此叫牌則拋 exception
            Bid lastValidBid = Lists.reverse(bidHistory).stream().filter(b -> !b.getBidSuit().isPass()).findFirst().orElseThrow(Exception::new);
            Boolean isValid = currentBid.validate(lastValidBid);
            if (isValid) {
                bidHistory.add(currentBid);
                //  check whether the game can begin.
                if (BidSuit.PASS.equals(currentBid.getBidSuit())) {
                    int lastValidBidIndex = bidHistory.indexOf(lastValidBid);
                    if (bidHistory.size() - lastValidBidIndex > 3) {
                        //  All other players have passed.
                        System.out.println("All other players have passed, let the game begin!");
                    }
                }
            } else {
                throw new Exception();
            }
        } else {    //  此局遊戲尚未存在, 為第一次叫牌, 直接新增 bid history.
            if (currentBid.getBidSuit().isPass()) {
                throw new Exception();
            }
            bidHistory = new ArrayList<>(Arrays.asList(currentBid));
        }
        records.put(gameId, bidHistory);
        return bidHistory;
    }
}
