package com.bridge.service.impl;

import com.bridge.entity.user.Player;
import com.bridge.enumeration.PlayerType;
import com.bridge.mapper.PlayerMapper;
import com.bridge.service.IPlayerService;
import com.bridge.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PlayerServiceImpl implements IPlayerService {

    private final PlayerMapper playerMapper;

    private final JwtUtil jwtUtil;

    public PlayerServiceImpl(PlayerMapper playerMapper, JwtUtil jwtUtil) {
        this.playerMapper = playerMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Player register(Player player) {
        if (player.getType().equals(PlayerType.USER.getCode())) {
            //  only when a normal user registers need to encrypt the password and insert into db
            player.encryptPassword();
            playerMapper.insertPlayer(player);
            String token = jwtUtil.generateToken(player);
            log.info("Player {} token : {}", player.getAccount(), token);
            player.setToken(token);
            return player;
        } else {
            //  TEST user shouldn't be able to register
            return null;
        }
    }

    @Override
    public List<Player> searchPlayers(Long id, String account, String name, Integer type) {
        List<Player> players = playerMapper.searchPlayers(id, account, name, type);
        return players;
    }
}
