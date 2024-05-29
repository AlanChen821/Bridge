package com.bridge.service.impl;

import com.bridge.service.IShuffleService;
import com.bridge.entity.card.Card;
import com.bridge.entity.card.PokerSuit;
import com.bridge.entity.user.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ShuffleServiceImpl implements IShuffleService {
    @Override
    public List<Player> shuffle(String player1Id, String player2Id, String player3Id, String player4Id) {
        System.out.println("Start new game, shuffling...");
        List<Player> result = new ArrayList<>();
//        do {
        List<Integer> numbers = IntStream.rangeClosed(0, 51).boxed().collect(Collectors.toList());

        Collections.shuffle(numbers);

        List<Card> player0Cards = parseCards(numbers, 0);
        Player player0 = new Player();
        player0.setId("Player0");
        player0.setCards(player0Cards);
        player0.setPoints(countPoints(player0Cards));

        List<Card> player1Cards = parseCards(numbers, 1);
        Player player1 = new Player();
        player1.setId("Player1");
        player1.setCards(player1Cards);
        player1.setPoints(countPoints(player1Cards));

        List<Card> player2Cards = parseCards(numbers, 2);
        Player player2 = new Player();
        player2.setId("Player2");
        player2.setCards(player2Cards);
        player2.setPoints(countPoints(player2Cards));

        List<Card> player3Cards = parseCards(numbers, 3);
        Player player3 = new Player();
        player3.setId("Player3");
        player3.setCards(player3Cards);
        player3.setPoints(countPoints(player3Cards));

//        } while (true);

        result.add(player0);
        result.add(player1);
        result.add(player2);
        result.add(player3);

        return result;
    }

    public List<Card> parseCards(List<Integer> numbers, int player) {
        System.out.println("Player " + player + "'s cards : ");
        List<Card> playerCards = new ArrayList<>();
        int startIndex = player * 13;
        int endIndex = startIndex + 13;
        for (int i = startIndex; i < endIndex; i++) {
            int number = numbers.get(i);
            PokerSuit suit = PokerSuit.getPokerSuit(number);
            int point = number % 13 + 1;
            Card card = new Card(suit, point);
            playerCards.add(card);
            System.out.println("number : " + number + ", suit : " + suit + ", point : " + point);
        }
        return playerCards;
    }

    public Integer countPoints(List<Card> cards) {
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
