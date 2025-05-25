package com.bridge.service;

import com.bridge.entity.user.Player;

import java.util.List;

public interface IPlayerService {
    Player register(Player player);

    Player login(Player loginRequest);

    List<Player> searchPlayers(Long id, String account, String name, Integer type);
}
