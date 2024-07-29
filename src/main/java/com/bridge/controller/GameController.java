package com.bridge.controller;

import com.bridge.entity.Game;
import com.bridge.service.IGameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("game")
public class GameController {

    private final IGameService gameService;

    public GameController(IGameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<Game>> getGameList() {
//            @RequestBody Object gameIO) {
        List<Game> gameList = gameService.getGameList();
        return new ResponseEntity<>(gameList, HttpStatus.OK);
    }
}
