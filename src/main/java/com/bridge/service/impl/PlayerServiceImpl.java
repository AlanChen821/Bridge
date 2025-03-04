package com.bridge.service.impl;

import com.bridge.entity.user.Player;
import com.bridge.enumeration.PlayerType;
import com.bridge.mapper.PlayerMapper;
import com.bridge.service.IPlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PlayerServiceImpl implements IPlayerService {

    private final PlayerMapper playerMapper;

    public PlayerServiceImpl(PlayerMapper playerMapper) {
        this.playerMapper = playerMapper;
    }

    @Override
    public Player register(Player player) {
        if (player.getType().equals(PlayerType.USER.getCode())) {
            player.encryptPassword();
        }
        playerMapper.insertPlayer(player);
        return player;
    }

    @Override
    public List<Player> searchPlayers(Long id, String account, String name, Integer type) {
        List<Player> players = playerMapper.searchPlayers(id, account, name, type);
        return players;
    }
}
