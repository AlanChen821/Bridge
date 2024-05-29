package com.bridge.controller;

import com.bridge.service.PlayService;
import com.bridge.entity.Game;
import com.bridge.entity.Play;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

    private PlayService playService;

    public PlayerController(PlayService playService) {
        this.playService = playService;
    }

    @PostMapping("/play")
    public ResponseEntity<Object> play(@RequestBody Play currentPlay) {
        try {
            Game currentGame = playService.play(currentPlay);
            return new ResponseEntity<>(currentGame, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Exception : " + e);
            return new ResponseEntity<>("play failed.", HttpStatus.BAD_REQUEST);
        }
    }
}
