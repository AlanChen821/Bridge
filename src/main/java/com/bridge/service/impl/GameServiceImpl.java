package com.bridge.service.impl;

import com.bridge.entity.Game;
import com.bridge.repository.GameRepository;
import com.bridge.service.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServiceImpl implements IGameService {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public List<Game> getGameList() {
        return gameRepository.findAll();
    }
}
