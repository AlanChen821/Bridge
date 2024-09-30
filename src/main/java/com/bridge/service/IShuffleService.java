package com.bridge.service;

import com.bridge.entity.card.Card;
import com.bridge.entity.user.Player;

import java.util.List;

public interface IShuffleService {

    void shuffle();

    List<Player> shuffle(Long player1Id, Long player2Id, Long player3Id, Long player4Id);
}
