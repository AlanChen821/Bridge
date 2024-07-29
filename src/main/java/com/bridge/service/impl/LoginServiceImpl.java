package com.bridge.service.impl;

import com.bridge.RedisConstants;
import com.bridge.entity.Game;
import com.bridge.entity.user.Player;
import com.bridge.enumeration.GameStatus;
//import com.bridge.mapper.GameMapper;
import com.bridge.repository.GameRepository;
import com.bridge.service.ILoginService;
import com.bridge.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class LoginServiceImpl implements ILoginService {

//    @Autowired
//    private GameMapper gameMapper;

    @Autowired
    private NamedParameterJdbcTemplate gameParameterJdbcTemplate;

    @Autowired
    private GameRepository gameRepository;

    @Override
    public Player loginAsGuest(String account) {
        return new Player(account);
    }


}
