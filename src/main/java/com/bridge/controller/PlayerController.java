package com.bridge.controller;

import com.bridge.entity.user.Player;
import com.bridge.service.IPlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("players")
public class PlayerController {

    private final IPlayerService playerService;

    public PlayerController(IPlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestHeader HttpHeaders headers, @RequestBody Player player) {
        playerService.register(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Player> searchPlayer(@RequestParam(required = false) Long id,
                                               @RequestParam(required = false) String account,
                                               @RequestParam(required = false) String name) {
        Player player = playerService.searchPlayer(id, account, name);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

}
