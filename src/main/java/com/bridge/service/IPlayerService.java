package com.bridge.service;

import com.bridge.entity.user.Player;

public interface IPlayerService {
    Player register(Player player);

    Player searchPlayer(Long id, String account, String name);
}
