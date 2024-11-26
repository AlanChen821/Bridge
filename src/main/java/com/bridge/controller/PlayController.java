package com.bridge.controller;

import com.bridge.service.IGameService;
import com.bridge.service.IPlayService;
import com.bridge.entity.Game;
import com.bridge.entity.Play;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
public class PlayController {

    private final IGameService gameService;

    private final IPlayService playService;

    public PlayController(IGameService gameService, IPlayService playService) {
        this.gameService = gameService;
        this.playService = playService;
    }

    @PostMapping("/play")
    public ResponseEntity<Object> play(@RequestHeader HttpHeaders headers, @RequestBody Play currentPlay) {
        try {
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
//            Game currentGame = gameService.get
            Game currentGame = playService.play(tokens.get(0), currentPlay);
            return new ResponseEntity<>(currentGame, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Exception : " + e);
            return new ResponseEntity<>("play failed.", HttpStatus.BAD_REQUEST);
        }
    }
}
