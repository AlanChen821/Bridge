package com.bridge.service;

import com.bridge.entity.card.Card;
import com.bridge.entity.user.Player;

import java.util.List;

public interface IShuffleService {

    List<Player> shuffle(String player1Id, String player2Id, String player3Id, String player4Id);
}
