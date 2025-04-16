package com.bridge.controller;

import com.bridge.entity.user.Player;
import com.bridge.service.IPlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

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
    public ResponseEntity<List<Player>> searchPlayer(Authentication auth,
                            @RequestParam(required = false) Long id,
                                                     @RequestParam(required = false) String account,
                                                     @RequestParam(required = false) String name,
                                                     @RequestParam(required = false) Integer type) {
        String accountInToken = (String) auth.getPrincipal();
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();

        List<Player> players = playerService.searchPlayers(id, account, name, type);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

}
