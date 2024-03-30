package com.bridge.entity;

import com.bridge.entity.card.BidSuit;
import com.bridge.entity.user.Player;
import lombok.Data;

import java.util.List;

@Data
public class Game {
    private String gameId;
    private BidSuit trump;
    private List<Player> players;
    private List<Round> rounds;
}
