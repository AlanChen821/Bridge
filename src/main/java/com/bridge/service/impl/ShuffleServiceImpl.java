package com.bridge.service.impl;

import com.bridge.constant.WebsocketDestination;
import com.bridge.entity.Game;
import com.bridge.entity.websocket.WebsocketNotifyShuffle;
import com.bridge.enumeration.WebsocketNotifyType;
import com.bridge.service.IShuffleService;
import com.bridge.entity.card.Card;
import com.bridge.entity.card.PokerSuit;
import com.bridge.entity.user.Player;
import com.bridge.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ShuffleServiceImpl implements IShuffleService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ShuffleServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void shuffle() {
        this.shuffle(0L, 1L, 2L, 3L);
    }

    @Override
    public void shuffle(Game game) {
        this.shuffle(game.getPlayers().get(0).getId(),
                game.getPlayers().get(1).getId(),
                game.getPlayers().get(2).getId(),
                game.getPlayers().get(3).getId());
    }

    @Override
    public List<Player> shuffle(Long player1Id, Long player2Id, Long player3Id, Long player4Id) {
        log.info("Start new game, shuffling...");
        List<Player> result = new ArrayList<>();

        //  generate the cards randomly
        List<Integer> numbers = IntStream.rangeClosed(0, 51).boxed().collect(Collectors.toList());
        Collections.shuffle(numbers);

        List<Card> player1Cards = parseCards(numbers, 0);
        Player player1 = new Player();
        player1.setCards(player1Cards);
        player1.setPoints(countPoints(player1Cards));
        sendCardsByWebSocket(player1Id, player1Cards);

        List<Card> player2Cards = parseCards(numbers, 1);
        Player player2 = new Player();
        player2.setCards(player2Cards);
        player2.setPoints(countPoints(player2Cards));
        sendCardsByWebSocket(player2Id, player2Cards);

        List<Card> player3Cards = parseCards(numbers, 2);
        Player player3 = new Player();
        player3.setCards(player3Cards);
        player3.setPoints(countPoints(player3Cards));
        sendCardsByWebSocket(player3Id, player3Cards);

        List<Card> player4Cards = parseCards(numbers, 3);
        Player player4 = new Player();
        player4.setCards(player4Cards);
        player4.setPoints(countPoints(player4Cards));
        sendCardsByWebSocket(player4Id, player4Cards);

        result.add(player1);
        result.add(player2);
        result.add(player3);
        result.add(player4);

        return result;
    }

    private void sendCardsByWebSocket(Long currentPlayerId, List<Card> currentPlayerCards) {
        WebsocketNotifyShuffle websocketNotifyShuffle = WebsocketNotifyShuffle.builder()
                .type(WebsocketNotifyType.SHUFFLE)
                .currentPlayerCards(currentPlayerCards)
                .createTime(new Timestamp(System.currentTimeMillis()))
                .build();
        simpMessagingTemplate.convertAndSend(WebsocketNotifyType.SHUFFLE.destination + "/" + currentPlayerId, JsonUtils.serialize(websocketNotifyShuffle));
    }

    private List<Card> parseCards(List<Integer> numbers, int player) {
        log.info("Player " + player + "'s cards : ");
        List<Card> playerCards = new ArrayList<>();
        int startIndex = player * 13;
        int endIndex = startIndex + 13;
        for (int i = startIndex; i < endIndex; i++) {
            int number = numbers.get(i);
            PokerSuit suit = PokerSuit.getPokerSuit(number);
            int point = number % 13 + 1;
            Card card = new Card(suit, point);
            playerCards.add(card);
            log.info("number : " + number + ", suit : " + suit + ", point : " + point);
        }
        return playerCards;
    }

    private Integer countPoints(List<Card> cards) {
        int totalPoints = 0;
        for (int i = 0; i < 13; i++) {
            int number = cards.get(i).getNumber();
//                int point = c.getNumber() % 13 + 1;
            switch (number) {
                case 1:
                    totalPoints += 4;
                    break;
                case 13:
                    totalPoints += 3;
                    break;
                case 12:
                    totalPoints += 2;
                    break;
                case 11:
                    totalPoints += 1;
                    break;
            }
        }

        return totalPoints;
    }
}
