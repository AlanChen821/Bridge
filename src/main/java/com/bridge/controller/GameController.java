package com.bridge.controller;

import com.bridge.entity.Call;
import com.bridge.entity.Game;
import com.bridge.entity.Play;
import com.bridge.service.IGameService;
import jakarta.validation.Valid;
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
@RequestMapping("games")
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

    @PutMapping("/{gameId}/players")
    public ResponseEntity<Game> enterGame(@RequestHeader HttpHeaders headers,
                                          @PathVariable Long gameId) {
        List<String> tokens = headers.get("token");
        if (null == tokens || tokens.isEmpty()) {
            log.error("Receive request whose token is null/empty.");
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        Game enteredGame;
        try {
            enteredGame = gameService.enterGame(tokens.get(0), gameId);
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

    @PostMapping("/{gameId}/call")
    public ResponseEntity<Object> call(@RequestHeader HttpHeaders headers,
                                       @PathVariable Long gameId,
                                       @Valid @RequestBody Call call) {
        try {
            List<String> tokens = headers.get("token");
            if (null == tokens || tokens.isEmpty()) {
                return new ResponseEntity<>("No token.", HttpStatus.FORBIDDEN);
            }

            call.setPlayerId(tokens.get(0));
            call.setGameId(gameId);
            Game success = gameService.call(gameId, call);
            return new ResponseEntity<>(success, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("exception : " + e);
            return new ResponseEntity<>("Call failed.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{gameId}/play")
    public ResponseEntity<Object> play(@RequestHeader HttpHeaders headers,
                                       @PathVariable Long gameId,
                                       @RequestBody Play currentPlay) {
        try {
            List<String> tokens = headers.get("token");
            if (null == tokens || tokens.isEmpty()) {
                log.error("Receive request whose token is null/empty.");
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
//            List<String> gameIds = headers.get("gameId");
//            if (Objects.isNull(gameIds) || gameIds.isEmpty()) {
//                log.error("Receive request whose gameId is null/empty.");
//                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
//            }
//            Game currentGame = gameService.get
            Game currentGame = gameService.play(tokens.get(0), gameId, currentPlay);
            return new ResponseEntity<>(currentGame, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Exception : " + e);
            return new ResponseEntity<>("play failed.", HttpStatus.BAD_REQUEST);
        }
    }
}
