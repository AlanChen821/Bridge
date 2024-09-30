package com.bridge.entity.websocket;

import com.bridge.entity.card.Card;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class WebsocketNotifyShuffle extends WebsocketNotifyBase {

    List<Card> player1Cards;
    List<Card> player2Cards;
    List<Card> player3Cards;
    List<Card> player4Cards;
}
