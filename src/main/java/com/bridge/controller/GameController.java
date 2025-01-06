package com.bridge.controller;

import com.bridge.entity.Game;
import com.bridge.service.IGameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("game")
public class GameController {

    private final IGameService gameService;

    public GameController(IGameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestHeader HttpHeaders headers, @RequestBody Game newGame) {
        List<String> tokens = headers.get("token");
        if (null == tokens || tokens.isEmpty()) {
            log.error("Receive request whose token is null/empty.");
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        Game targetGame = gameService.createGame(tokens.get(0), newGame);
        return new ResponseEntity<>(targetGame, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Game>> getGameList() {
//            @RequestBody Object gameIO) {
        List<Game> gameList = gameService.getGameList();
        return new ResponseEntity<>(gameList, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Game> enterGame(@RequestHeader HttpHeaders headers, @RequestBody Game targetGame) {
        List<String> tokens = headers.get("token");
        if (null == tokens || tokens.isEmpty()) {
            log.error("Receive request whose token is null/empty.");
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        List<String> gameIds = headers.get("gameId");
        if (Objects.isNull(gameIds) || gameIds.isEmpty()) {
            log.error("Receive request whose gameId is null/empty.");
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        List<Long> gameId = gameIds.stream().map(Long::parseLong).collect(Collectors.toList());
        Game enteredGame;
        try {
            enteredGame = gameService.enterGame(tokens.get(0), gameId.get(0), targetGame);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(enteredGame, HttpStatus.OK);
    }

    @PutMapping("/{gameId}/status")
    public ResponseEntity<Game> changeStatus(@RequestHeader HttpHeaders headers,
                                             @PathVariable Long gameId,
                                             @RequestBody Game targetGame) {
//        Game targetGame;
        try {
            targetGame = gameService.changeGameStatus(gameId, targetGame.getStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(targetGame, HttpStatus.OK);
    }
}
