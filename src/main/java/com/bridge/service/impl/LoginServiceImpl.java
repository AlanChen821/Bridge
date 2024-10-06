package com.bridge.service.impl;

import com.bridge.entity.user.Player;
//import com.bridge.mapper.GameMapper;
//import com.bridge.repository.GameRepository;
import com.bridge.service.ILoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginServiceImpl implements ILoginService {

//    @Autowired
//    private GameMapper gameMapper;

    private final SimpMessagingTemplate simpMessagingTemplate;

//    @Autowired
//    private NamedParameterJdbcTemplate gameParameterJdbcTemplate;

//    @Autowired
//    private GameRepository gameRepository;

    public LoginServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public Player loginAsGuest(String account) {
        log.info("Player : " + account + " has logged in.");
//        simpMessagingTemplate.convertAndSend("/topic/greetings", "{\"message\":\"start\"}");

        return new Player(account);
    }


}
