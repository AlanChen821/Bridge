package com.bridge.controller;

import com.bridge.entity.Game;
import com.bridge.service.IGameService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping
    public ResponseEntity<Game> enterGame(@RequestHeader HttpHeaders headers) {
        List<String> tokens = headers.get("token");
        if (null == tokens || tokens.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        Game targetGame = gameService.enterGame(tokens.get(0));
        return new ResponseEntity<>(targetGame, HttpStatus.OK);
    }
}