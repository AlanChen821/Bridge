package com.bridge.demo.service.impl;

import com.bridge.demo.service.IShuffleService;
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
    public List<Player> shuffle(String player1Id, String player2Id, String player3Id, String player4Id, Boolean canLower) {
        System.out.println("Start new game, shuffling...");
        List<Player> result = new ArrayList<>();
        Player player0 = new Player();
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        do {
            List<Integer> numbers = IntStream.rangeClosed(0, 51).boxed().collect(Collectors.toList());

            Collections.shuffle(numbers);

            List<Card> player0Cards = parseCards(numbers, 0);
            player0.setId("Player0");
            player0.setCards(player0Cards);
            player0.setPoints(countPoints(player0Cards));

            List<Card> player1Cards = parseCards(numbers, 1);
            player1.setId("Player1");
            player1.setCards(player1Cards);
            player1.setPoints(countPoints(player1Cards));

            List<Card> player2Cards = parseCards(numbers, 2);
            player2.setId("Player2");
            player2.setCards(player2Cards);
            player2.setPoints(countPoints(player2Cards));

            List<Card> player3Cards = parseCards(numbers, 3);
            player3.setId("Player3");
            player3.setCards(player3Cards);
            player3.setPoints(countPoints(player3Cards));

            if (canLower || (player0.pass() && player1.pass() && player2.pass() && player3.pass())) {
                System.out.println("Everyone has good cards, let the game begin.");
                break;
            } else {
                System.out.println(String.format("Player1 : %d, Player2 : %d, Player3 : %d, Player4 : %d", player0.getPoints(), player1.getPoints(), player2.getPoints(), player3.getPoints()));
                System.out.println("There're player(s) have cards which lower than 4 points, reshuffle.");
            }
        } while (true);

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
